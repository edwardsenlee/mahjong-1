package com.ahliu.test.mahjong.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Keep track of all sessions and distribute async messages
 * @author Edmund
 *
 */
@Service
public class SessionManager {

	private static final int READ_BUFFER_SIZE = 8192;
	private static final byte TERMINATING_BYTE = '\0';

	@Autowired
	private CommandManager commandManager;

	private Selector socketSelector;

	private static ExecutorService executorService = Executors.newFixedThreadPool(10);
	private static Map<SocketChannel,ByteBuffer> tempReadBufferMap = new HashMap<SocketChannel,ByteBuffer>();
	private static Map<SocketChannel,Integer> tempReadSizeMap = new HashMap<SocketChannel,Integer>();

	private ByteBuffer readBuffer;
	private Map<SocketChannel,BlockingQueue<ByteBuffer>> writeQueueMap = new HashMap<SocketChannel,BlockingQueue<ByteBuffer>>();

	/**
	 * @throws IOException
	 *
	 */
	public void startAcceptIncomingConnections(final String hostAddress, final int port) throws IOException {

		// init selector
		this.socketSelector = SelectorProvider.provider().openSelector();

		// init server socket
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		serverSocketChannel.configureBlocking(false);
		InetSocketAddress socketAddress = new InetSocketAddress(hostAddress, port);
		serverSocketChannel.socket().bind(socketAddress);
		serverSocketChannel.register(this.socketSelector, SelectionKey.OP_ACCEPT);

		this.readBuffer = ByteBuffer.wrap(new byte[READ_BUFFER_SIZE]);

		while(true) {
			// select
			this.socketSelector.select();

			Iterator<SelectionKey> selectedKeys = this.socketSelector.selectedKeys().iterator();
			while (selectedKeys.hasNext()) {
				//				Runtime runtime = Runtime.getRuntime();
				//				System.out.println(String.format("1) max mem = %d, allocated = %d, free = %d", runtime.maxMemory(), runtime.totalMemory(), runtime.freeMemory()));
				SelectionKey key = selectedKeys.next();
				selectedKeys.remove();

				if (!key.isValid()) {
					continue;
				}

				if (key.isAcceptable()) {
					this.doAccept(key);
				} else if (key.isReadable()) {
					try {
						this.doRead(key);
					} catch (IOException e) {
						key.cancel();
						key.channel().close();
					}
				} else if (key.isWritable()) {
					this.doWrite(key);
				}
			}
		}
	}

	private void doAccept(final SelectionKey key) throws IOException {
		// accept connection
		ServerSocketChannel  currentServerSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel incomingSocketChannel = currentServerSocketChannel.accept();
		if (incomingSocketChannel != null) {
			// configure socket behaviour
			incomingSocketChannel.configureBlocking(false);
			incomingSocketChannel.register(this.socketSelector, SelectionKey.OP_READ);

			// configure read/write buffers
			this.writeQueueMap.put(incomingSocketChannel, new ArrayBlockingQueue<ByteBuffer>(10));
		}
	}

	private void doRead(final SelectionKey key) throws IOException {
		// read data
		SocketChannel currentSocketChannel = (SocketChannel) key.channel();

		// clear previous data
		this.readBuffer.clear();
		int byteRead = currentSocketChannel.read(this.readBuffer);

		if (byteRead == -1) { // invalid socket read
			key.cancel();
			currentSocketChannel.close();
			return;
		}

		// a completed command is read
		if (this.readBuffer.get(byteRead - 1) == TERMINATING_BYTE) {

			// check if older bytes existed
			if (tempReadBufferMap.containsKey(currentSocketChannel)) {
				// get and merge with existing read buffer
				ByteBuffer existingReadBuffer = tempReadBufferMap.get(currentSocketChannel);
				int existingSize = tempReadSizeMap.get(currentSocketChannel);

				// append to existing buffer
				existingReadBuffer.put(this.readBuffer.array(), 0, byteRead);

				// remove from the cache map
				tempReadBufferMap.remove(currentSocketChannel);
				tempReadSizeMap.remove(currentSocketChannel);

				// submit to executor
				executorService.execute(new ExecutionWorker(this, this.commandManager, currentSocketChannel, existingReadBuffer.array(), byteRead + existingSize));
			} else {
				// all read, no old bytes, submit to executor
				executorService.execute(new ExecutionWorker(this, this.commandManager, currentSocketChannel, this.readBuffer.array(), byteRead));
			}

		} else { // incompleted command is read, more bytes are coming

			if (tempReadBufferMap.containsKey(currentSocketChannel)) {
				// some older bytes are loaded, combine the currently loaded bytes
				tempReadBufferMap.get(currentSocketChannel).put(this.readBuffer);
				tempReadSizeMap.put(currentSocketChannel, tempReadSizeMap.get(currentSocketChannel) + byteRead);
			} else {
				// create entry to hold earlier bytes for later use
				ByteBuffer newTempReadBuffer = ByteBuffer.wrap(new byte[READ_BUFFER_SIZE]);
				newTempReadBuffer.put(this.readBuffer.array(), 0, byteRead);
				tempReadBufferMap.put(currentSocketChannel, newTempReadBuffer);
				tempReadSizeMap.put(currentSocketChannel, byteRead);
			}
		}
	}

	private void doWrite(final SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		BlockingQueue<ByteBuffer> requestQueue = this.writeQueueMap.get(socketChannel);

		while (!requestQueue.isEmpty()) {
			ByteBuffer writeBuffer = requestQueue.peek();
			if (writeBuffer != null) {
				System.out.println("[DEBUG2] " + new String(writeBuffer.array()));
				socketChannel.write(writeBuffer);
				if (writeBuffer.remaining() > 0) {
					System.out.println("[DEBUG3] haha~");
					break;
				}

				// change back to read
				socketChannel.keyFor(this.socketSelector).interestOps(SelectionKey.OP_READ);

				// remove the request from queue
				requestQueue.poll();
			}
		}
	}

	/**
	 *
	 * @param writeRequest
	 * @throws InterruptedException
	 */
	public void addWriteRequest(final SocketChannel socketChannel, final ByteBuffer byteBuffer) throws InterruptedException {
		// append to queue of that socket channel
		this.writeQueueMap.get(socketChannel).put(byteBuffer);

		// prepare socket channel for write
		socketChannel.keyFor(this.socketSelector).interestOps(SelectionKey.OP_WRITE);

		this.socketSelector.wakeup();
	}
}