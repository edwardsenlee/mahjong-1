package com.ahliu.test.mahjong.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahliu.test.mahjong.dao.PlayerDao;
import com.ahliu.test.mahjong.model.Player;

@Service
public class PlayerManager {

	@Autowired
	private PlayerDao playerDao;

	private Map<String,Player> sessionPlayerMap = new HashMap<String,Player>();
	private ReadWriteLock sessionPlayerMapLock = new ReentrantReadWriteLock();


	/**
	 *
	 * @param login
	 * @return
	 */
	public boolean isPlayerExisted(final String login) {
		return this.playerDao.getPlayer(login) != null;
	}

	/**
	 *
	 * @param player
	 * @return
	 */
	public boolean isLoggedIn(final Player player) {
		this.sessionPlayerMapLock.readLock().lock();
		try {
			return this.sessionPlayerMap.containsValue(player);
		} finally {
			this.sessionPlayerMapLock.readLock().unlock();
		}
	}

	/**
	 *
	 * @param sessionId
	 * @return
	 */
	public boolean isSessionExisted(final String sessionId) {
		this.sessionPlayerMapLock.readLock().lock();
		try {
			return this.sessionPlayerMap.containsKey(sessionId);
		} finally {
			this.sessionPlayerMapLock.readLock().unlock();
		}
	}

	/**
	 *
	 * @param login
	 * @param password
	 * @return
	 */
	public Player registerPlayer(final String login, final String password) {

		if (login == null || login.isEmpty()) {
			throw new IllegalArgumentException("Argument `login` should not be null or empty");
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("Argument `password` should not be null or empty");
		}

		// check if player already existed
		if (this.isPlayerExisted(login)) {
			throw new RuntimeException(String.format("Login %s already existed", login));
		}

		final Player player = new Player();
		player.setLogin(login);
		player.setPassword(password);
		player.setStatus(Player.Status.NOT_LOGGED_IN);
		player.setSessionId(null);
		this.playerDao.addPlayer(player);

		return player;
	}

	/**
	 *
	 * @param login
	 * @return
	 */
	public Player getPlayer(final String login) {
		if (login == null) {
			throw new IllegalArgumentException("Argument `login` should not be null or empty");
		}
		return this.playerDao.getPlayer(login);
	}

	/**
	 *
	 * @param sessionId
	 * @return
	 */
	public Player getPlayerBySessionId(final String sessionId) {
		this.sessionPlayerMapLock.readLock().lock();
		try {
			return this.sessionPlayerMap.get(sessionId);
		} finally {
			this.sessionPlayerMapLock.readLock().unlock();
		}
	}

	/**
	 * Return the player with session UUID if password matched, return null if not matched
	 * @param login
	 * @param password
	 * @return
	 */
	public String login(final Player player) {
		this.sessionPlayerMapLock.writeLock().lock();
		try {
			synchronized(player) {

				player.setStatus(Player.Status.LOGGED_IN);

				// create new session UUID
				String newSessionId = String.format("%d-%d", player.getId(), System.nanoTime());
				player.setSessionId(newSessionId);

				// cache session player mapping
				this.sessionPlayerMap.put(newSessionId, player);

				return newSessionId;
			}
		} finally {
			this.sessionPlayerMapLock.writeLock().unlock();
		}
	}

	/**
	 *
	 * @param sessionId
	 */
	public void logout(final Player player) {
		this.sessionPlayerMapLock.writeLock().lock();
		try {
			player.setStatus(Player.Status.NOT_LOGGED_IN);
			player.setSessionId(null);
			this.sessionPlayerMap.remove(player.getSessionId());
		} finally {
			this.sessionPlayerMapLock.writeLock().unlock();
		}
	}
}
