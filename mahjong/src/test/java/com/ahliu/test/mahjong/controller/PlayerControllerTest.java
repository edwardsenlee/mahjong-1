package com.ahliu.test.mahjong.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ahliu.test.mahjong.util.ResponseCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PlayerControllerTest {

	@Autowired
	private PlayerController playerController;

	@Test
	public void testRegisterPlayer() {
		// positive case
		Response response = this.playerController.register("test-login", "test-password");
		Assert.assertEquals("{\"code\":1,\"dataGraph\":{\"player\":{\"id\":1}}}", response.toString());

		// negative case
		response = this.playerController.register("test-login", "test-password");
		Assert.assertEquals("{\"code\":10001,\"dataGraph\":{}}", response.toString());
	}

	@Test
	public void testLogin() {
		this.playerController.register("test-login", "test-password");
		ObjectMapper om = new ObjectMapper();

		// positive case
		Response response = this.playerController.login("test-login", "test-password");
		JsonNode jsonNode = om.valueToTree(response);
		Assert.assertEquals(1, jsonNode.at("/code").asInt());
		Assert.assertEquals(1, jsonNode.at("/dataGraph/player/id").asInt());
		Assert.assertTrue(jsonNode.at("/dataGraph/player/sessid").textValue().startsWith("1-"));

		// negative case (login not existed)
		response = this.playerController.login("test-login2", "test-password");
		jsonNode = om.valueToTree(response);
		Assert.assertEquals(ResponseCode.ERR_LOGIN_NOT_EXISTED, jsonNode.at("/code").asInt());

		// negative case (password not matched)
		response = this.playerController.login("test-login", "test-password2");
		jsonNode = om.valueToTree(response);
		Assert.assertEquals(ResponseCode.ERR_LOGIN_FAILED, jsonNode.at("/code").asInt());
	}
}
