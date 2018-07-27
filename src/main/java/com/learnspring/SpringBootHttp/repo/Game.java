package com.learnspring.SpringBootHttp.repo;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String winner;
	private String loser;
	private Integer winnerScore;
	private Integer loserScore;
	public Date timestamp;
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String getLoser() {
		return loser;
	}
	public void setLoser(String loser) {
		this.loser = loser;
	}
	public Integer getWinnerScore() {
		return winnerScore;
	}
	public void setWinnerScore(Integer winnerScore) {
		this.winnerScore = winnerScore;
	}
	public Integer getLoserScore() {
		return loserScore;
	}
	public void setLoserScore(Integer loserScore) {
		this.loserScore = loserScore;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
}
