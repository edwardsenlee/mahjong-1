package com.ahliu.test.mahjong.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Repository;

import com.ahliu.test.mahjong.model.Player;

@Repository
public class PlayerDao {

	private final AtomicLong idSequence = new AtomicLong(1);

	private final Map<Long,Player> playerIdMap = new HashMap<Long,Player>();
	private final Map<String,Player> playerLoginMap = new HashMap<String,Player>();
	private final ReadWriteLock playerMapLock = new ReentrantReadWriteLock();

	/**
	 *
	 * @param login
	 * @return
	 */
	public Player getPlayer(final String login) {
		this.playerMapLock.readLock().lock();
		try {
			return this.playerLoginMap.get(login);
		} finally {
			this.playerMapLock.readLock().unlock();
		}
	}

	/**
	 *
	 * @param login
	 * @param password
	 * @return
	 */
	public void addPlayer(final Player player) {
		this.playerMapLock.writeLock().lock();
		try {
			if (player.getLogin() == null) {
				throw new RuntimeException("Empty player login");
			}
			if (this.playerLoginMap.containsKey(player.getLogin())) {
				throw new RuntimeException("Login name already existed");
			}

			player.setId(this.idSequence.getAndIncrement());

			this.playerIdMap.put(player.getId(), player);
			this.playerLoginMap.put(player.getLogin(), player);

		} finally {
			this.playerMapLock.writeLock().unlock();
		}
	}
}
