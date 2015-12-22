package com.ahliu.test.mahjong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ahliu.test.mahjong.model.Game;
import com.ahliu.test.mahjong.model.Player;
import com.ahliu.test.mahjong.service.GameManager;
import com.ahliu.test.mahjong.service.PlayerManager;
import com.ahliu.test.mahjong.util.ResponseCode;

@Controller
public class GameController {

	@Autowired
	private GameManager gameManager;

	@Autowired
	private PlayerManager playerManager;

	public Response listAllGames() {
		return Response.success().set("games", this.gameManager.getAllGames());
	}

	public Response selectGame(final String sessionId, final String gameId) {
		Player player = this.playerManager.getPlayerBySessionId(sessionId);
		if (player == null) {
			return Response.fail(ResponseCode.ERR_NOT_LOGGED_IN);
		}

		Game game = this.gameManager.getGame(gameId);
		if (game == null) {
			return Response.fail(ResponseCode.ERR_GAME_NOT_EXISTED);
		}

		synchronized(game) {
			if (!game.isOccupied()) {
				this.gameManager.occupyGame(player, game);
				return Response.success();
			} else {
				return Response.fail(ResponseCode.ERR_GAME_OCCUPIED);
			}
		}
	}

	//	public Response unselectGame(final String sessionId) {
	//		Player player = this.playerManager.getPlayerBySessionId(sessionId);
	//		if (player == null) {
	//			return Response.fail(ResponseCode.ERR_NOT_LOGGED_IN);
	//		}
	//
	//		Game game = this.gameManager.getGameBySessionId(sessionId);
	//		if (game == null) {
	//			return Response.fail(ResponseCode.ERR_GAME_NOT_SELECTED);
	//		}
	//	}

}
