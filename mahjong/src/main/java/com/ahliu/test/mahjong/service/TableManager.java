package com.ahliu.test.mahjong.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahliu.test.mahjong.dao.TableDao;
import com.ahliu.test.mahjong.model.Player;
import com.ahliu.test.mahjong.model.Table;

@Service
public class TableManager {

	@Autowired
	private TableDao tableDao;

	private Map<String,Table> sessionTableMap = new HashMap<String,Table>();
	private ReadWriteLock sessionTableMapLock = new ReentrantReadWriteLock();

	/**
	 *
	 * @param tableId
	 * @return
	 */
	public Table getTable(final String tableId) {
		return this.tableDao.getTable(tableId);
	}

	/**
	 *
	 * @return
	 */
	public List<Table> getAllTables() {
		return this.tableDao.getAllTables();
	}

	/**
	 *
	 * @param sessionId
	 * @return
	 */
	public Table getTableBySessionId(final String sessionId) {
		this.sessionTableMapLock.readLock().lock();
		try {
			return this.sessionTableMap.get(sessionId);
		} finally {
			this.sessionTableMapLock.readLock().unlock();
		}
	}

	/**
	 *
	 * @param player
	 * @param table
	 */
	public void occupyTable(final Player dealer, final Table table) {
		synchronized(table) {
			// set table to occupied
			table.setStatus(Table.Status.WAITING_FOR_PLAYERS);
		}
	}

	public void unoccupyTable(final Table table) {
		synchronized(table) {

		}
	}
}
