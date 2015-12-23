package com.ahliu.test.mahjong.network;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.ahliu.test.mahjong.controller.Response;
import com.ahliu.test.mahjong.util.ResponseCode;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class CommandManager implements ApplicationContextAware {

	@Autowired
	private ApplicationContext context;

	@Resource(name="commandMappings")
	private Properties commandMappingProperties;

	private Map<Integer,Command> commandMap = new HashMap<Integer,Command>();

	public Object invoke(final int commandCode, final JsonNode rootNode) {
		try {
			return this.commandMap.get(commandCode).invoke(rootNode);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return Response.fail(ResponseCode.ERR_INVOKE_FAILED);
		}
	}

	@PostConstruct
	public void initCommandMappings() {

		this.commandMappingProperties.forEach((commandCode,command)->{

			String className = null;
			String methodName = null;
			String[] arguments = null;

			// fetch the controller class
			String commandStr = (String)command;
			int classNameDelim = commandStr.indexOf('.');
			if (classNameDelim > 0) {
				className = commandStr.substring(0, classNameDelim);
				int methodDelim = commandStr.indexOf('(', classNameDelim + 1);
				if (methodDelim > 0) {
					methodName = commandStr.substring(classNameDelim + 1, methodDelim);
					int paramDelim = commandStr.indexOf(')', methodDelim + 1);
					arguments = commandStr.substring(methodDelim + 1, paramDelim).split(",");
				} else { // no argument
					methodName = commandStr.substring(classNameDelim + 1, commandStr.length());
				}
			}

			try {
				this.commandMap.put(Integer.valueOf((String)commandCode), new Command(this.context, className, methodName, arguments));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}
