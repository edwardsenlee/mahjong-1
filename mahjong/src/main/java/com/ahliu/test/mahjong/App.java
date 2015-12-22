package com.ahliu.test.mahjong;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ahliu.test.mahjong.network.SessionManager;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(final String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
		SessionManager sm = context.getBean(SessionManager.class);
		sm.startAcceptIncomingConnections("0.0.0.0", 12345);
	}
}
