package com.ahliu.test.mahjong.model;

public class Player {

	private long id;
	private String name;

	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Player)) {
			return false;
		}
		return this.name.equals(((Player)obj).getName());
	}
}
