package com.ahliu.test.mahjong.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.springframework.beans.BeansException;

import com.ahliu.test.mahjong.controller.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExecutionWorker implements Runnable {

	private byte[] input;
	private int byteRead;
	private CommandManager commandManager;
	private SessionManager sessionManager;

	private SocketChannel socketChannel;

	public ExecutionWorker(final SessionManager sessionManager, final CommandManager commandManager, final SocketChannel socketChannel, final byte[] input, final int byteRead) {
		this.byteRead = byteRead - 1;
		this.input = new byte[this.byteRead];
		System.arraycopy(input, 0, this.input, 0, this.byteRead);
		this.sessionManager = sessionManager;
		this.commandManager = commandManager;
		this.socketChannel = socketChannel;
	}

	@Override
	public void run() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println("[DEBUG1] " + new String(this.input));
			JsonNode rootNode = mapper.readTree(this.input);
			Response response = (Response)this.commandManager.invoke(rootNode.at("/cmd/id").asInt(), rootNode);
			this.sessionManager.addWriteRequest(this.socketChannel, ByteBuffer.wrap(response.array()));
		} catch (IOException | BeansException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
