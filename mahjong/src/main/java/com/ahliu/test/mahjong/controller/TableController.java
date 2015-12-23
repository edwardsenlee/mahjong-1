package com.ahliu.test.mahjong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ahliu.test.mahjong.model.Player;
import com.ahliu.test.mahjong.model.Table;
import com.ahliu.test.mahjong.service.PlayerManager;
import com.ahliu.test.mahjong.service.TableManager;
import com.ahliu.test.mahjong.util.ResponseCode;

@Controller
public class TableController {

	@Autowired
	private TableManager tableManager;

	@Autowired
	private PlayerManager playerManager;

	public Response listAllTables() {
		return Response.success().set("tables", this.tableManager.getAllTables());
	}

	public Response selectTable(final String sessionId, final String tableId) {
		Player player = this.playerManager.getPlayerBySessionId(sessionId);
		if (player == null) {
			return Response.fail(ResponseCode.ERR_NOT_LOGGED_IN);
		}

		Table table = this.tableManager.getTable(tableId);
		if (table == null) {
			return Response.fail(ResponseCode.ERR_GAME_NOT_EXISTED);
		}

		synchronized(table) {
			if (!table.isOccupied()) {
				this.tableManager.occupyTable(player, table);
				return Response.success();
			} else {
				return Response.fail(ResponseCode.ERR_GAME_OCCUPIED);
			}
		}
	}

	public Response unselectTable(final String sessionId) {
		Player player = this.playerManager.getPlayerBySessionId(sessionId);
		if (player == null) {
			return Response.fail(ResponseCode.ERR_NOT_LOGGED_IN);
		}

		Table table = this.tableManager.getTableBySessionId(sessionId);
		if (table == null) {
			return Response.fail(ResponseCode.ERR_GAME_NOT_SELECTED);
		}

		synchronized(table) {
			if (table.canQuit()) {
				if (table.getDealer().equals(player)) {
					// dealer leaves table, dismiss
					this.tableManager.unoccupyTable(table);
				} else {
					// just a player leave
				}
			} else {
				return Response.fail(ResponseCode.ERR_GAME_ALREADY_STARTED);
			}
		}
	}
}
