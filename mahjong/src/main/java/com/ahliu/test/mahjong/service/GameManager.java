package com.ahliu.test.mahjong.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Service;

import com.ahliu.test.mahjong.model.Game;
import com.ahliu.test.mahjong.model.Player;

@Service
public class GameManager {

	private final Map<String,Game> gameIdMap = new HashMap<String,Game>();
	private final ReadWriteLock gameIdMapLock = new ReentrantReadWriteLock();

	/**
	 *
	 * @return
	 */
	public Game createGame(Player captain) {

		// create objects
		final Game game = new Game();
		final String uuid = UUID.randomUUID().toString();
		game.setUuid(uuid);
		game.setStatus(Game.Status.RAW);
		game.setCaptain(captain);
		game.getPlayers().add(captain);

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
				game.setStatus(Game.Status.PENDING_GAME_START);
			}
		}
	}

	/**
	 *
	 * @param game
	 * @param player
	 */
	public void removePlayer(Game game, Player player) {
		if (game.getPlayers().contains(player)) {
			synchronized(game) {
				if (game.getPlayers().contains(player)) {
					// remove player from the game
					game.getPlayers().remove(player);

					// change status
					if (game.getStatus() == Game.Status.PENDING_GAME_START) {
						// TODO: unexpected player dropped off handling
						game.setStatus(Game.Status.WAITING_FOR_PLAYERS);
					}
				}
			}
		}
	}

	public void startGame(Game game) {
		synchronized(game) {
			if (game.getStatus() == Game.Status.PENDING_GAME_START) {
				// randomize position
				Collections.shuffle(game.getPlayers());

				// jump state
				game.setStatus(Game.Status.POS_INITIALIZED);
			}
		}
	}
}
