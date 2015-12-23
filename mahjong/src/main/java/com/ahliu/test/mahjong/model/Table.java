package com.ahliu.test.mahjong.model;

import java.util.List;
import java.util.Set;

public class Table {

	public enum Status {
		UNOCCUPIED,
		WAITING_FOR_PLAYERS,
		PENDING_GAME_START,
		IN_PROGRESS
	}

	private String uuid;
	private Status status;
	private List<Player> players;
	private Player dealer;
	private Set<Rule> ruleSet;

	/**
	 *
	 * @return
	 */
	public boolean isOccupied() {
		return this.status != Status.UNOCCUPIED;
	}

	/**
	 *
	 * @return
	 */
	public boolean isInProgress() {
		return this.status == Status.IN_PROGRESS;
	}

	/**
	 *
	 * @return
	 */
	public boolean canQuit() {
		return !this.isInProgress();
	}

	public String getUuid() {
		return this.uuid;
	}
	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}
	public Status getStatus() {
		return this.status;
	}
	public void setStatus(final Status status) {
		this.status = status;
	}
	public List<Player> getPlayers() {
		return this.players;
	}
	public void setPlayers(final List<Player> players) {
		this.players = players;
	}
	public Player getDealer() {
		return this.dealer;
	}
	public void setDealer(final Player dealer) {
		this.dealer = dealer;
	}
	public Set<Rule> getRuleSet() {
		return this.ruleSet;
	}
	public void setRuleSet(final Set<Rule> ruleSet) {
		this.ruleSet = ruleSet;
	}
}
