package com.ahliu.test.mahjong.model;

import java.util.List;

public class Game {

	public enum Wind {
		EAST, SOUTH, WEST, NORTH
	}

	public enum Status {
		RAW, WAITING_FOR_PLAYERS,
		PENDING_GAME_START,
		PENDING_POS_INIT, POS_INITIALIZED,
		PENDING_ROUND_START, ROUND_STARTED
	}

	private String uuid;
	private Status status;
	private int roundNum;
	private Wind wind;
	private int index; // 1,2,3,4
	private Player captain;
	private List<Player> players;

	private List<Tile> currentPool;
	private List<Tile> discardPool;

	public String getUuid() {
		return this.uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getRoundNum() {
		return this.roundNum;
	}
	public void setRoundNum(int roundNum) {
		this.roundNum = roundNum;
	}
	public Wind getWind() {
		return this.wind;
	}
	public void setWind(Wind wind) {
		this.wind = wind;
	}
	public int getIndex() {
		return this.index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public List<Player> getPlayers() {
		return this.players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	public List<Tile> getCurrentPool() {
		return this.currentPool;
	}
	public void setCurrentPool(List<Tile> currentPool) {
		this.currentPool = currentPool;
	}
	public List<Tile> getDiscardPool() {
		return this.discardPool;
	}
	public void setDiscardPool(List<Tile> discardPool) {
		this.discardPool = discardPool;
	}
	public Status getStatus() {
		return this.status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Player getCaptain() {
		return this.captain;
	}
	public void setCaptain(Player captain) {
		this.captain = captain;
	}
}
