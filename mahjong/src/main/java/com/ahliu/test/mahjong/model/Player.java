package com.ahliu.test.mahjong.model;

public class Player {

	public enum Status {
		NOT_LOGGED_IN, LOGGED_IN, IN_GAME
	}

	private long id;
	private String login;
	private String password;
	private String sessionId;
	private Status status;

	public String getLogin() {
		return this.login;
	}
	public void setLogin(final String login) {
		this.login = login;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(final String password) {
		this.password = password;
	}
	public long getId() {
		return this.id;
	}
	public void setId(final long id) {
		this.id = id;
	}
	public Status getStatus() {
		return this.status;
	}
	public void setStatus(final Status status) {
		this.status = status;
	}
	public String getSessionId() {
		return this.sessionId;
	}
	public void setSessionId(final String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof Player)) {
			return false;
		}
		return this.getLogin().equals(((Player)obj).getLogin());
	}
}
