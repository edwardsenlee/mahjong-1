package com.ahliu.test.mahjong.model;

public class Composite {

	public enum Type {
		CHOW, PONG, KONG, FLOWER
	}

	private Type type;
	private Tile tile;

	public Type getType() {
		return this.type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Tile getTile() {
		return this.tile;
	}
	public void setTile(Tile tile) {
		this.tile = tile;
	}
}
