package com.ahliu.test.mahjong.controller;

import java.util.HashMap;
import java.util.Map;

import com.ahliu.test.mahjong.util.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Response {

	private static ThreadLocal<ObjectMapper> objectMapperHolder = new ThreadLocal<ObjectMapper>() {
		@Override
		public ObjectMapper initialValue() {
			return new ObjectMapper();
		}
	};

	private final Map<String,Object> data = new HashMap<String,Object>();
	private int code;

	/**
	 *
	 * @return
	 */
	public static Response success() {
		final Response response = new Response();
		response.setCode(ResponseCode.SUCCESS);
		return response;
	}

	/**
	 *
	 * @param errorMessage
	 * @return
	 */
	public static Response fail(final int errorCode) {
		final Response response = new Response();
		response.setCode(errorCode);
		return response;
	}

	public Response set(final String nodeExpr, final Object value) {
		if (nodeExpr == null || nodeExpr.trim().isEmpty()) {
			throw new RuntimeException("Unexpected null or empty node expression");
		}

		Map<String,Object> currentDataGraphNode = this.data;
		final String[] nodeExprPath = nodeExpr.split("\\.", -1);
		final int nodePathLen = nodeExprPath.length;
		int cnt = 0;
		for (final String nodeName : nodeExprPath) {

			cnt++;

			if (nodeName == null || nodeName.trim().isEmpty()) {
				throw new RuntimeException("Unexpected empty node name found in " + nodeExpr);
			}

			if (cnt == nodePathLen) { // leaf node
				currentDataGraphNode.put(nodeName, value);
			} else { // parent node
				if (!currentDataGraphNode.containsKey(nodeName)) {
					// create a new node and append to the graph
					final Map<String,Object> newNode = new HashMap<String,Object>();
					currentDataGraphNode.put(nodeName, newNode);

					// move the current node to such node(or new node) and ready for next iteration
					currentDataGraphNode = newNode;
				} else {
					currentDataGraphNode = (Map<String,Object>)currentDataGraphNode.get(nodeName);
				}
			}
		}

		return this;
	}

	@Override
	public String toString() {
		try {
			return objectMapperHolder.get().writeValueAsString(this) + '\n';
		} catch (final JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 * @return
	 */
	public byte[] array() {
		return this.toString().getBytes();
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(final int code) {
		this.code = code;
	}

	public Map<String, Object> getResult() {
		return this.data;
	}
}
