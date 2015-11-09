package com.ahliu.test.mahjong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ahliu.test.mahjong.model.Game;
import com.ahliu.test.mahjong.model.Player;
import com.ahliu.test.mahjong.service.GameManager;
import com.ahliu.test.mahjong.service.PlayerManager;

@Controller
public class GameController {

	@Autowired
	private GameManager gameManager;

	@Autowired
	private PlayerManager playerManager;

	public Game createGame(String captainName) {
		final Player captain = this.playerManager.getPlayer(captainName);
		return this.gameManager.createGame(captain);
	}

	public void addPlayerToGame(String gameUuid, String playerName) {

		/**
		 * Part 1: validation
		 */
		// (a) check game existed
		final Game game = this.gameManager.getGame(gameUuid);
		if (game == null) {
			// TODO: error handling
			return;
		}
		// (b) check game status
		if (game.getStatus() != Game.Status.WAITING_FOR_PLAYERS) {
			// TODO: error handling
			return;
		}
		// (c) check player already added
		final Player player = this.playerManager.getPlayer(playerName);
		if (player == null) {
			// TODO: error handling
			return;
		}
		if (game.getPlayers().contains(player)) {
			// TODO: error handling
			return;
		}

		this.gameManager.addPlayer(game, player);
	}

	/**
	 *
	 * @param gameUuid
	 * @param playerName
	 */
	public void removePlayerFromGame(String gameUuid, String playerName) {
		final Game game = this.gameManager.getGame(gameUuid);
		final Player player = this.playerManager.getPlayer(playerName);
		this.gameManager.removePlayer(game, player);
	}

	/**
	 * Start the game flow, can only be triggered by the table captain
	 * @param gameUuid
	 * @param requestPlayerName
	 */
	public void startGame(String gameUuid, String requestPlayerName) {
		final Game game = this.gameManager.getGame(gameUuid);
		final Player player = this.playerManager.getPlayer(requestPlayerName);

		if (game.getCaptain().equals(player)) {
			this.gameManager.startGame(game);
		}
	}
}
