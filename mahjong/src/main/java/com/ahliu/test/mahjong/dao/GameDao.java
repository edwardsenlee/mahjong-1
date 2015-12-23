package com.ahliu.test.mahjong.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ahliu.test.mahjong.model.Game;

@Repository
public class GameDao {

	private static List<Game> games = new ArrayList<Game>();
	private static Map<String,Game> idGameMap = new HashMap<String,Game>();

	static {
		// initialize 60 unoccupied tables for players to choose
		for (int i = 0; i < 4; i++) {
			Game game = new Game();
			game.setStatus(Game.Status.UNOCCUPIED);
			game.setUuid("game-" + i);
			games.add(game);
			idGameMap.put(game.getUuid(), game);
		}
	}

	/**
	 *
	 * @return
	 */
	public List<Game> getAllGames() {
		return games;
	}

	/**
	 *
	 * @param gameId
	 * @return
	 */
	public Game getGame(final String gameId) {
		return this.idGameMap.get(gameId);
	}
}
