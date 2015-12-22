package com.ahliu.test.mahjong.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.ahliu.test.mahjong.controller.PlayerController;
import com.ahliu.test.mahjong.controller.Response;
import com.ahliu.test.mahjong.util.CommandCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExecutionWorker implements Runnable {

	private byte[] input;
	private int byteRead;
	private ApplicationContext context;
	private SocketChannel socketChannel;

	public ExecutionWorker(final ApplicationContext context, final SocketChannel socketChannel, final byte[] input, final int byteRead) {
		this.byteRead = byteRead - 1;
		this.input = new byte[this.byteRead];
		System.arraycopy(input, 0, this.input, 0, this.byteRead);
		this.context = context;
		this.socketChannel = socketChannel;
	}

	@Override
	public void run() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(this.input);
			Response response = null;
			switch (jsonNode.at("/cmd/id").asInt()) {
			case CommandCode.REGISTER_PLAYER:
				response = this.context.getBean(PlayerController.class).register(
						jsonNode.at("/cmd/login").asText(), jsonNode.at("/cmd/password").asText());
				break;
			}

			this.context.getBean(SessionManager.class).addWriteRequest(this.socketChannel, ByteBuffer.wrap(response.array()));

		} catch (IOException | BeansException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
