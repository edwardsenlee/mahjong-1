package com.ahliu.test.mahjong.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahliu.test.mahjong.dao.GameDao;
import com.ahliu.test.mahjong.model.Game;
import com.ahliu.test.mahjong.model.Player;

@Service
public class GameManager {

	@Autowired
	private GameDao gameDao;

	private Map<String,Game> sessionGameMap = new HashMap<String,Game>();
	private ReadWriteLock sessionGameMapLock = new ReentrantReadWriteLock();

	/**
	 *
	 * @param gameId
	 * @return
	 */
	public Game getGame(final String gameId) {
		return this.gameDao.getGame(gameId);
	}

	/**
	 *
	 * @return
	 */
	public List<Game> getAllGames() {
		return this.gameDao.getAllGames();
	}

	/**
	 *
	 * @param sessionId
	 * @return
	 */
	public Game getGameBySessionId(final String sessionId) {
		this.sessionGameMapLock.readLock().lock();
		try {
			return this.sessionGameMap.get(sessionId);
		} finally {
			this.sessionGameMapLock.readLock().unlock();
		}
	}

	/**
	 *
	 * @param player
	 * @param game
	 */
	public void occupyGame(final Player dealer, final Game game) {
		synchronized(game) {
			// set game to occupied
			game.setStatus(Game.Status.WAITING_FOR_PLAYERS);
		}
	}

	public void unoccupyGame(final Game game) {
		synchronized(game) {

		}
	}
}
