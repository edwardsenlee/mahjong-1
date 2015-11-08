package com.ahliu.test.mahjong.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.ahliu.test.mahjong.model.Game;
import com.ahliu.test.mahjong.model.Player;

public class GameManager {

	private final Map<String,Game> gameIdMap = new HashMap<String,Game>();
	private final ReadWriteLock gameIdMapLock = new ReentrantReadWriteLock();

	/**
	 *
	 * @return
	 */
	public Game createGame() {

		// create objects
		final Game game = new Game();
		final String uuid = UUID.randomUUID().toString();
		game.setUuid(uuid);
		game.setStatus(Game.Status.RAW);

		// cache the game
		this.gameIdMapLock.writeLock().lock();
		try {
			this.gameIdMap.put(uuid, game);
		} finally {
			this.gameIdMapLock.writeLock().unlock();
		}

		return game;
	}

	/**
	 *
	 * @param uuid
	 * @return
	 */
	public Game getGame(String uuid) {
		this.gameIdMapLock.readLock().lock();
		try {
			return this.gameIdMap.get(uuid);
		} finally {
			this.gameIdMapLock.readLock().unlock();
		}
	}

	/**
	 *
	 * @param game
	 * @param player
	 */
	public void addPlayer(Game game, Player player) {

		if (game == null) {
			// TODO: error handling
			return;
		}
		if (player == null) {
			// TODO: error handling
			return;
		}

		if (game.getPlayers().contains(player)) {
			// TODO: error handling
			return;
		}

		if (game.getStatus() != Game.Status.WAITING_FOR_PLAYERS) {
			// TODO: error handling
			return;
		}

		if (game.getPlayers().size() >= 4) {
			// TODO: error handling
			return;
		}

		synchronized(game) {
			if (game.getPlayers().contains(player)) {
				// TODO: error handling
				return;
			}

			// add player to game
			game.getPlayers().add(player);

			// change to PENDING_ROUND_START if all players are ready
			if (game.getPlayers().size() == 4 && game.getStatus() == Game.Status.WAITING_FOR_PLAYERS) {
				game.setStatus(Game.Status.PENDING_ROUND_START);
			}
		}
	}
}
