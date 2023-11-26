package gamecontroller;

import java.util.Random;

import spaceship.SpaceShip;

public class Player {


	private SpaceShip ship;
	private int score;
	private static final int SCORE_MODIFIER = 10;

	public Player(SpaceShip ship) {
		this.ship = ship;
		this.score = 0;
	}

	public void increaseScore() {
		int random = new Random().nextInt(SCORE_MODIFIER);
		score += random;
	}

	public SpaceShip getShip() {
		return ship;
	}

	public void setShip(SpaceShip ship) {
		this.ship = ship;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
