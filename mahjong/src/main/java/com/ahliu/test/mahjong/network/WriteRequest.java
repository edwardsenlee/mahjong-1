package com.ahliu.test.mahjong.network;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WriteRequest {
	private SocketChannel socketChannel;
	private ByteBuffer byteBuffer;

	public WriteRequest(final SocketChannel socketChannel, final byte[] output) {
		this.socketChannel = socketChannel;
		this.byteBuffer= ByteBuffer.wrap(output);
	}

	public SocketChannel getSocketChannel() {
		return this.socketChannel;
	}
	public ByteBuffer getByteBuffer() {
		return this.byteBuffer;
	}
}
