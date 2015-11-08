package com.ahliu.test.mahjong.model;

import java.util.List;

public class PlayerDeck {

	private Player player;
	private List<Tile> handTiles;
	private List<Composite> composites;

	public Player getPlayer() {
		return this.player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public List<Tile> getHandTiles() {
		return this.handTiles;
	}
	public void setHandTiles(List<Tile> handTiles) {
		this.handTiles = handTiles;
	}
	public List<Composite> getComposites() {
		return this.composites;
	}
	public void setComposites(List<Composite> composites) {
		this.composites = composites;
	}
}
