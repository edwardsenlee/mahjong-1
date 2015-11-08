package com.ahliu.test.mahjong.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ahliu.test.mahjong.model.Player;

@Service
public class PlayerManager {

	@Value("${mahjong.max.player.num}")
	private int maxPlayerNum;

	private final Map<String,Player> playerNameMap = new HashMap<String,Player>();
	private final ReadWriteLock playerNameMapLock = new ReentrantReadWriteLock();

	/**
	 *
	 * @param name
	 * @return
	 */
	public Player getPlayer(String name) {
		this.playerNameMapLock.readLock().lock();
		try {
			// if it does exist, then return the cached one
			if (this.playerNameMap.containsKey(name)) {
				return this.playerNameMap.get(name);
			}

			// not exist, create one but check if reach max no. of players
			if (this.playerNameMap.size() >= this.maxPlayerNum) {
				return null;
			}

			this.playerNameMapLock.readLock().unlock();
			this.playerNameMapLock.writeLock().lock();
			try {
				if (!this.playerNameMap.containsKey(name) && this.playerNameMap.size() < this.maxPlayerNum) {
					final Player player = new Player();
					player.setName(name);
					this.playerNameMap.put(name, player);
					return player;
				} else {
					return this.playerNameMap.get(name);
				}
			} finally {
				this.playerNameMapLock.writeLock().unlock();
				this.playerNameMapLock.readLock().lock();
			}

		} finally {
			this.playerNameMapLock.readLock().unlock();
		}
	}
}
