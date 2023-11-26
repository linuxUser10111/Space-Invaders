package keyboardsteering;


import gamecontroller.Point2D;
import gameview.GameBoardUI;
import rocket.Rocket;
import spaceship.SpaceShip;
import javafx.scene.input.KeyEvent;

public class KeyboardSteering {
	private SpaceShip userShip;
	private GameBoardUI gameBoardGUI;

	public KeyboardSteering(GameBoardUI gameBoardGUI, SpaceShip spaceship) {
		this.userShip = spaceship;
		this.gameBoardGUI = gameBoardGUI;
		// gameBoardGUI.addEventHandler(KeyEvent.KEY_TYPED, this:: pressButton);
	}

	public void pressButton(KeyEvent event) {
		Point2D playerPosiotion = userShip.getPosition();
		if (gameBoardGUI.getGameBoard().isRunning() && !gameBoardGUI.getGameBoard().isPaused()) {
			switch (event.getCode()) {
			
				case D:
					userShip.setPosition(
							new Point2D(playerPosiotion.getX() + userShip.getSpeed(), playerPosiotion.getY()), gameBoardGUI.getGameBoard().getSize());
					break;
				case A:
					userShip.setPosition(
							new Point2D(playerPosiotion.getX() - userShip.getSpeed(), playerPosiotion.getY()), gameBoardGUI.getGameBoard().getSize());
					break;
				case V:
					gameBoardGUI.getGameBoard().shootRocket(
							new Rocket(userShip.getPosition(), userShip, gameBoardGUI.getGameBoard().getSize()));
					gameBoardGUI.getGameBoard().getAudioplayer().playShootingSound();
					break;
				default:
					System.out.println(event.getCode().toString());
					System.out.println("undefined key");
					break;

			}
		}

	}
}

