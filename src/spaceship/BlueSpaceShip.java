package spaceship;

import gamecontroller.Dimension2D;
import gamecontroller.Point2D;

public class BlueSpaceShip extends SpaceShip {
	private static final String BLUE_SHIP_IMAGE_FILE = "BlueSpaceShip.png";
	protected static final int INITIAL_X = 10;
	public BlueSpaceShip(Dimension2D gameBoardSize) {
		super(gameBoardSize);
		setPosition(new Point2D(INITIAL_X, gameBoardSize.getHeight() - DEFAULT_SPACES), gameBoardSize);
		decrementHorezental();
		setIconLocation(BLUE_SHIP_IMAGE_FILE);
	}
}
