package gameview;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import audioplayer.AudioPlayer;
import gamecontroller.DifficultyLevel;
import gamecontroller.Dimension2D;
import gamecontroller.GameBoard;
import gamecontroller.GameOutcome;
import gamecontroller.Point2D;
import gamemodel.GameModel;
import gamemodel.Observer;
import keyboardsteering.KeyboardSteering;
import rocket.Rocket;
import spaceship.SpaceShip;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;

public class GameBoardUI extends Canvas implements EventHandler<KeyEvent>, Observer<Boolean> {

	private static final int UPDATE_PERIOD = 100 / 25;
	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 500;
	private static final Dimension2D DEFAULT_SIZE = new Dimension2D(DEFAULT_WIDTH, DEFAULT_HEIGHT);

	public static Dimension2D getPreferredSize() {
		return DEFAULT_SIZE;
	}

	/**
	 * Timer responsible for updating the game every frame that runs in a separate
	 * thread.
	 */
	private Timer gameTimer;

	private GameBoard gameBoard;

	private final GameToolBar gameToolBar;

	private KeyboardSteering keyBoardSteering;

	private HashMap<String, Image> imageCache;

	private boolean running;
	private boolean paused;

	//for observer pattern
	private final GameModel model;

	public GameBoardUI(GameToolBar gameToolBar, GameModel model) {
		this.model = model;
		model.addObserver(this);
		this.running = model.isRunning();
		this.gameToolBar = gameToolBar;
		setup();

	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public KeyboardSteering getKeyBoardSteering() {
		return keyBoardSteering;
	}

	/**
	 * Removes all existing cars from the game board and re-adds them. Player car is
	 * reset to default starting position. Renders graphics.
	 */
	public void setup() {
		setupGameBoard();
		setupImageCache();
		this.gameToolBar.updateToolBarStatus(false);
		paint();
	}

	private void setupGameBoard() {
		Dimension2D size = getPreferredSize();
		this.gameBoard = new GameBoard(size, chooseDifficultyLevel(), model);
		this.gameBoard.setAudioplayer(new AudioPlayer());
		widthProperty().set(size.getWidth());
		heightProperty().set(size.getHeight());
		this.keyBoardSteering = new KeyboardSteering(this, this.gameBoard.getPlayer().getShip());
	}

	private DifficultyLevel chooseDifficultyLevel() {
		return DifficultyLevel.EASY;
	}

	private void updateScore() {
		this.gameToolBar.updateScore(gameBoard.getPlayer().getScore());
	}

	private synchronized void setupImageCacheRockets(List<Rocket> rocketsList) {
		for (Rocket rocket : rocketsList) {
			String rocketLocation = rocket.getRocketIconeLocation();
			this.imageCache.put(rocketLocation, getImage(rocketLocation));
		}
	}
	
	private void setupImageCache() {
		this.imageCache = new HashMap<>();
		for (SpaceShip spaceShip : this.gameBoard.getInvaders()) {
			String imageLocation = spaceShip.getIconLocation();
			this.imageCache.computeIfAbsent(imageLocation, this::getImage);
		}
		String playerImageLocation = this.gameBoard.getPlayer().getShip().getIconLocation();
		this.imageCache.put(playerImageLocation, getImage(playerImageLocation));
	}

	/**
	 * Sets the car's image.
	 *
	 * @param shipImageFilePath an image file path that needs to be available in the
	 *                          resources folder of the project
	 */
	private Image getImage(String shipImageFilePath) {
		URL shipImageUrl = getClass().getClassLoader().getResource(shipImageFilePath);
		if (shipImageUrl == null) {
			throw new IllegalArgumentException(
					"Please ensure that your resources folder contains the appropriate files for this exercise.");
		}
		return new Image(shipImageUrl.toExternalForm());
	}

	/**
	 * Starts the GameBoardUI Thread, if it wasn't running. Starts the game board,
	 * which causes the ships to change their positions (i.e. move). Renders graphics
	 * and updates tool bar status.
	 */
	public void startGame() {
		//changed for observer pattern
		if (!this.running) {
			this.gameBoard.startGame();
			this.gameToolBar.updateToolBarStatus(true);
			startTimer();
			paint();
		}
	}

	private void startTimer() {
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				if(running && !paused) {
					updateGame();
				}
			}
		};
		if (this.gameTimer != null) {
			this.gameTimer.cancel();
		}
		this.gameTimer = new Timer();
		this.gameTimer.scheduleAtFixedRate(timerTask, UPDATE_PERIOD, UPDATE_PERIOD);
	}

	private void updateGame() {
		//changed for observer pattern
		if (this.running) {
			// updates car positions and re-renders graphics
			this.gameBoard.update();
			updateScore();
			// when this.gameBoard.getOutcome() is OPEN, do nothing
			if (this.gameBoard.getGameOutcome() == GameOutcome.LOST) {
				showAsyncAlert("Oh.. you lost.");
				this.stopGame();
			} else if (this.gameBoard.getGameOutcome() == GameOutcome.WON) {
				showAsyncAlert("Congratulations! You won!! SCORE: " + gameBoard.getPlayer().getScore());
				this.stopGame();
			}
			paint();

		}
	}

	/**
	 * Stops the game board and set the tool bar to default values.
	 */
	public void stopGame() {
		if (model.isRunning()) {
			this.gameBoard.stopGame();
			this.gameToolBar.updateToolBarStatus(false);
			this.gameTimer.cancel();
			gameBoard.getPlayer().setScore(0);
		}
	}

	/**
	 * Render the graphics of the whole game by iterating through the cars of the
	 * game board at render each of them individually.
	 */
	private void paint() {
		//this.imageCache.values().remove(getImage("rocket.png"));
		//this.imageCache.values().remove(getImage("GreenShipRocket.png"));
		List<Rocket> temp = new ArrayList<>();
		temp.addAll(gameBoard.getRockets());
		setupImageCacheRockets(temp);

		ImagePattern pattern = new ImagePattern(new Image("LMUCampus.jpeg"));
		getGraphicsContext2D().setFill(pattern);
		getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
		for (SpaceShip Ship : this.gameBoard.getInvaders()) {
			paintShip(Ship);
		}
		// render player car
		paintShip(this.gameBoard.getPlayer().getShip());
		paintRockets(gameBoard.getRockets());

	}
	
	
	private void paintRockets(List<Rocket> rockets) {

		for (Rocket rocket : rockets) {
			Point2D RocketPosition = rocket.getPosition();
			getGraphicsContext2D().drawImage(this.imageCache.get(rocket.getRocketIconeLocation()),
					RocketPosition.getX(), RocketPosition.getY(), rocket.getSize().getWidth(),
					rocket.getSize().getHeight());
		}

	}

	/**
	 * Show image of a car at the current position of the car.
	 *
	 */
	private void paintShip(SpaceShip ship) {
		Point2D ShipPosition = ship.getPosition();

		getGraphicsContext2D().drawImage(this.imageCache.get(ship.getIconLocation()), ShipPosition.getX(),
				ShipPosition.getY(), ship.getSize().getWidth(), ship.getSize().getHeight());
	}

	/**
	 * Method used to display alerts in moveCars().
	 *
	 * @param message you want to display as a String
	 */
	private void showAsyncAlert(String message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(message);
			alert.showAndWait();
			this.setup();
		});
	}
	
	public void pauseGame() {
			this.gameBoard.pauseGame();	
	}

	public void resumeGame() {

		this.gameBoard.resumeGame();
	}

	@Override
	public void handle(KeyEvent event) {
		keyBoardSteering.pressButton(event);
	}

	@Override
	public void onUpdate(Boolean newStatRunning, Boolean newStatePaused) {
		this.running = newStatRunning;
		this.paused = newStatePaused;
	}

}
