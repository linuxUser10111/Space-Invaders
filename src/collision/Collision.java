package collision;


import gamecontroller.Dimension2D;
import gamecontroller.Point2D;
import rocket.Rocket;
import spaceship.SpaceShip;

public class Collision {

	private SpaceShip spaceShip;
	private Rocket rocket;
	private boolean crash;

	public Collision(SpaceShip spaceShip, Rocket rocket) {
		this.spaceShip = spaceShip;
		this.rocket = rocket;
		this.crash = detectCollision();
	}

	public boolean detectCollision() {
		Point2D p1 = spaceShip.getPosition();
		Dimension2D d1 = spaceShip.getSize();

		Point2D p2 = rocket.getPosition();
		Dimension2D d2 = rocket.getSize();

		boolean above = p1.getY() + d1.getHeight() < p2.getY();
		boolean below = p1.getY() > p2.getY() + d2.getHeight();
		boolean right = p1.getX() + d1.getWidth() < p2.getX();
		boolean left = p1.getX() > p2.getX() + d2.getWidth();

		return !above && !below && !right && !left;
	}

	public SpaceShip evaluate() {
		return null;
	}

	public SpaceShip getSpaceShip() {
		return spaceShip;
	}

	public void setSpaceShip(SpaceShip spaceShip) {
		this.spaceShip = spaceShip;
	}

	public Rocket getRocket() {
		return rocket;
	}

	public void setRocket(Rocket rocket) {
		this.rocket = rocket;
	}

	public boolean isCrash() {
		return this.crash;
	}

	public boolean getCrash() {
		return this.crash;
	}

	public void setCrash(boolean crash) {
		this.crash = crash;
	}
}
