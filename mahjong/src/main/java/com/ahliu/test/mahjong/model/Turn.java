package com.ahliu.test.mahjong.model;

import java.util.List;
import java.util.Map;

public class Turn {
	private Map<Position, Player> players;
	private Map<Position, Deck> decks;
	private List<Integer> diceValues;
	private List<Tile> tilePool;
	private List<Tile> discardedPool;
	private Wind prevailingWind;
	private int turnNum; // 1,2,3,4
	private int repeat; // due to winning or draw game
	private Player winner;
	private List<FanItem> winningFanItems;
	private Map<Position, Integer> initScores;
	private Map<Position, Integer> scoreMovements;
	private Map<Position, Integer> finalScores;
}
