package rocket;

import gamecontroller.Dimension2D;
import gamecontroller.Point2D;
import spaceship.GreenSpaceShip;
import spaceship.SpaceShip;

public class Rocket {

	private String ROCKET_IMAGE_FILE = "TUMRocket.png";
	private Point2D positionR;
	private boolean shot;
	protected static final int DEFAULT_ROCKET_WIDTH = 25;
	protected static final int DEFAULT_ROCKET_HEIGHT = 25;
	protected static final double DEFAULT_SPEED_MODIFIER = 0.2;
	private SpaceShip parent;

	private Dimension2D size = new Dimension2D(DEFAULT_ROCKET_WIDTH, DEFAULT_ROCKET_HEIGHT);
	private Dimension2D gameBoardSize;

	public Rocket(Point2D position, SpaceShip parent, Dimension2D gameBoardSize) {
		this.positionR = new Point2D(position.getX(), position.getY());
		this.gameBoardSize = gameBoardSize;
		this.parent = parent;
		setIsShot(true);
	}

	public String getRocketIconeLocation() {
		return this.ROCKET_IMAGE_FILE;
	}

	public void setRocketIconeLocation(String location) {
		this.ROCKET_IMAGE_FILE = location;
	}

	public SpaceShip getParent() {
		return this.parent;
	}

	public void setParent(SpaceShip parent) {
		this.parent = parent;
	}

	public boolean isIsShot() {
		return this.shot;
	}

	public boolean getIsShot() {
		return this.shot;
	}

	public void setIsShot(boolean isShot) {
		this.shot = isShot;
	}

	public Dimension2D getSize() {
		return this.size;
	}

	public void setSize(Dimension2D size) {
		this.size = size;
	}

	public void shoot() {
		shot = true;
	}

	public void moveRocket(int speed) {
		double oldX = positionR.getX();
		double oldY = positionR.getY();

		if (shot) {
			if (parent instanceof GreenSpaceShip) {
				double newY = oldY + speed;
				if (newY < 0) {
					destroy();
				} else {
					this.positionR = new Point2D(oldX, oldY + speed * DEFAULT_SPEED_MODIFIER);
				}
			} else {
				double newY = oldY + speed;
				if (newY > gameBoardSize.getHeight()) {
					destroy();
				} else {
					this.positionR = new Point2D(oldX, oldY - speed * DEFAULT_SPEED_MODIFIER);
				}
			}
		}
	}

	public Point2D getPosition() {
		return positionR;
	}

	public void setPosition(Point2D position) {
		this.positionR = position;
	}

	public boolean isShot() {
		return shot;
	}

	public void setShot(boolean isShot) {
		this.shot = isShot;
	}

	public void destroy() {
		shot = false;
	}

}
