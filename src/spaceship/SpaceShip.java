package spaceship;



import gamecontroller.Dimension2D;
import gamecontroller.Point2D;
import rocket.Rocket;


public abstract class SpaceShip {

	private int speed;
	private boolean hitByRocket;
	private Point2D position;
	protected static final int DEFAULT_SHIP_WIDTH = 55;
	protected static final int DEFAULT_SHIP_HEIGHT = 25;
	private Dimension2D gameBoardSize;
	protected static final int LEFT_DIRECTION = 180;
	protected static final int RIGHT_DIRECTION = 0;
	protected static final int DEFAULT_SPACES = 50;
	protected static final int NUMBER_OF_COLUMN = 5;
	protected static final int DEFAULT_HSPACES = 40;

	private static int direction;
	private static int NUMBER_OF_SET_SHIPS = 0;
	private static int HORIZONTAL = 0;
	private static int VERTICAL = 0;
	private String iconLocation;
	private Dimension2D size = new Dimension2D(DEFAULT_SHIP_WIDTH, DEFAULT_SHIP_HEIGHT);
	private static boolean shouldTurn;

	// Scanner myObj = new Scanner(System.in);

	public SpaceShip(Dimension2D gameBoardSizer) {
		this.gameBoardSize = gameBoardSizer;
		direction = RIGHT_DIRECTION;
		NUMBER_OF_SET_SHIPS++;
		setRandomPosition();
		HORIZONTAL += DEFAULT_SPACES;
	}

	private void setRandomPosition() {
		if (NUMBER_OF_SET_SHIPS > NUMBER_OF_COLUMN) {
			HORIZONTAL = 0;
			VERTICAL += DEFAULT_HSPACES;
			NUMBER_OF_SET_SHIPS = 1;
		}
		this.position = new Point2D(HORIZONTAL, VERTICAL);
		// HORIZONTAL += 50;
	}

	public void decrementHorezental() {
		HORIZONTAL -= DEFAULT_SPACES;
		NUMBER_OF_SET_SHIPS--;
	}

	public void resetPositioning() {
		HORIZONTAL = 0;
		VERTICAL = 0;
		NUMBER_OF_SET_SHIPS = 0;
	}
	
	public boolean getShouldTurn() {
		return shouldTurn;
	}
	
	public void setShouldTurn(boolean value) {
		 shouldTurn = value;
	}

	public boolean isIsHitByRocket() {
		return this.hitByRocket;
	}

	public boolean getIsHitByRocket() {
		return this.hitByRocket;
	}

	public void setIsHitByRocket(boolean isHitByRocket) {
		this.hitByRocket = isHitByRocket;
	}

	public Dimension2D getSize() {
		return this.size;
	}

	public void setSize(Dimension2D size) {
		this.size = size;
	}

	public void moveShip(Dimension2D gameBoardSizeM, int speedM) {

		if (this.hitByRocket) {
			return;
		}
		double maxX = gameBoardSizeM.getWidth();
		// calculate delta between old coordinates and new ones based on speed and

		double deltaX = this.speed * Math.cos(Math.toRadians(direction));
		double newX = this.position.getX() + deltaX;
		double newY = this.position.getY();

		// calculate position in case the boarder of the game board has been reached
		if (newX < 0) {
			shouldTurn = true;
			if (direction == LEFT_DIRECTION) {
				direction = RIGHT_DIRECTION;
			} else {
				direction = LEFT_DIRECTION;
			}

		} else if (newX + this.size.getWidth() > maxX) {
			shouldTurn = true;
			if (direction == LEFT_DIRECTION) {
				direction = RIGHT_DIRECTION;
			} else {
				direction = LEFT_DIRECTION;
			}
		}
		// set coordinates
		this.position = new Point2D(newX, newY);

	}


	protected void setIconLocation(String iconLocation) {
		if (iconLocation == null) {
			throw new NullPointerException("The chassis image of a car cannot be null.");
		}
		this.iconLocation = iconLocation;
	}

	public String getIconLocation() {
		return iconLocation;
	}

	public Rocket shootRocket() {
		Rocket rocket = new Rocket(position, this, gameBoardSize);
		rocket.setRocketIconeLocation("LMURocket.png");
		return rocket;
	}

	public void explode() {
		hitByRocket = true;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isHitByRocket() {
		return hitByRocket;
	}

	public void setHitByRocket(boolean isHitByRocket) {
		this.hitByRocket = isHitByRocket;
	}

	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D positionV, Dimension2D sizeS) {
		if (positionV.getX() < 0) {
			positionV.setX(0);
		} else if (sizeS != null && positionV.getX() > sizeS.getWidth() - DEFAULT_SPACES) {
			positionV.setX(gameBoardSize.getWidth() - DEFAULT_SPACES);
		}
		this.position = positionV;
	}


	public Dimension2D getGameBoardSize() {
		return this.gameBoardSize;
	}

	public void setGameBoardSize(Dimension2D newSize) {
		this.gameBoardSize = newSize;
	}
}