package com.ahliu.test.mahjong.model;

import java.util.List;
import java.util.Set;

public class Game {

	public enum Status {
		UNOCCUPIED,
		WAITING_FOR_PLAYERS,
		PENDING_GAME_START,
		STARTED
	}

	private String uuid;
	private List<Player> players;
	private Player dealer;
	private List<Match> matches;
	private Set<Rule> ruleSet;
	private Status status;

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
	public boolean isStarted() {
		return this.status == Status.STARTED;
	}

	/**
	 *
	 * @return
	 */
	public boolean canQuit() {
		return !this.isStarted();
	}

	public String getUuid() {
		return this.uuid;
	}
	public void setUuid(final String uuid) {
		this.uuid = uuid;
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
	public List<Match> getMatches() {
		return this.matches;
	}
	public void setMatches(final List<Match> matches) {
		this.matches = matches;
	}
	public Set<Rule> getRuleSet() {
		return this.ruleSet;
	}
	public void setRuleSet(final Set<Rule> ruleSet) {
		this.ruleSet = ruleSet;
	}
	public Status getStatus() {
		return this.status;
	}
	public void setStatus(final Status status) {
		this.status = status;
	}
}
