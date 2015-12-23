package com.ahliu.test.mahjong.network;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;

public class Command {

	private Object classInstance;
	private Method method;
	private String[] arguments;

	public Command(final ApplicationContext context, final String className, final String methodName, final String[] arguments) throws NoSuchMethodException, SecurityException {
		this.classInstance = context.getBean(className);
		for (Method method : this.classInstance.getClass().getMethods()) {
			if (method.getName().equals(methodName)) {
				this.method = method;
			}
		}
		this.arguments = arguments;
	}

	/**
	 *
	 * @param args
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Object invoke(final JsonNode rootNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (this.arguments != null) {
			Object[] values = new String[this.arguments.length];
			int cnt = 0;
			for (String arg : this.arguments) {
				values[cnt++] = rootNode.at(arg).asText();
			}
			return this.method.invoke(this.classInstance, values);
		} else {
			return this.method.invoke(this.classInstance);
		}
	}
}
