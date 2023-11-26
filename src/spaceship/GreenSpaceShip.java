package spaceship;

import gamecontroller.Dimension2D;

public class GreenSpaceShip extends SpaceShip {
	private static final String GREEN_SHIP_IMAGE_FILE = "GreenSpaceShip.png";

	public GreenSpaceShip(Dimension2D gameBoardSize) {
		super(gameBoardSize);
		setIconLocation(GREEN_SHIP_IMAGE_FILE);
	}
}
