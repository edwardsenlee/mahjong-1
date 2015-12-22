package com.ahliu.test.mahjong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ahliu.test.mahjong.model.Player;
import com.ahliu.test.mahjong.service.PlayerManager;
import com.ahliu.test.mahjong.util.ResponseCode;

@Controller
public class PlayerController {

	@Autowired
	private PlayerManager playerManager;

	public Response register(final String login, final String password) {
		// check if player existed
		if (!this.playerManager.isPlayerExisted(login)) {
			Player player = this.playerManager.registerPlayer(login, password);
			return Response.success().set("player.id", player.getId());
		} else {
			return Response.fail(ResponseCode.ERR_LOGIN_ALREADY_REGISTERED);
		}
	}

	public Response login(final String login, final String password) {
		// check existence
		Player player = this.playerManager.getPlayer(login);
		if (player == null) {
			return Response.fail(ResponseCode.ERR_LOGIN_NOT_EXISTED);
		}

		// check existing login session
		if (this.playerManager.isLoggedIn(player)) {
			return Response.fail(ResponseCode.ERR_ALREADY_LOGIN);
		}

		// check password
		if (!player.getPassword().equals(password)) {
			return Response.fail(ResponseCode.ERR_LOGIN_FAILED);
		}

		String sessionId = this.playerManager.login(player);

		return Response.success().set("player.id", player.getId()).set("player.sessid", sessionId);
	}

	public Response logout(final String sessionId) {
		// check session existence
		Player player = this.playerManager.getPlayerBySessionId(sessionId);
		if (player == null) {
			return Response.fail(ResponseCode.ERR_NOT_LOGGED_IN);
		}

		// TODO: lots of things to check, e.g. quit existing game?
		this.playerManager.logout(player);

		return Response.success();
	}
}
