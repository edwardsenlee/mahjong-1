package com.ahliu.test.mahjong.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ahliu.test.mahjong.model.Table;

@Repository
public class TableDao {

	private static List<Table> tables = new ArrayList<Table>();
	private static Map<String,Table> idTableMap = new HashMap<String,Table>();

	static {
		// initialize 60 unoccupied tables for players to choose
		for (int i = 0; i < 4; i++) {
			Table table = new Table();
			table.setStatus(Table.Status.UNOCCUPIED);
			table.setUuid("table-" + i);
			tables.add(table);
			idTableMap.put(table.getUuid(), table);
		}
	}

	/**
	 *
	 * @return
	 */
	public List<Table> getAllTables() {
		return tables;
	}

	/**
	 *
	 * @param gameId
	 * @return
	 */
	public Table getTable(final String tableId) {
		return idTableMap.get(tableId);
	}
}
