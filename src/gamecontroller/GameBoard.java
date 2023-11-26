package gamecontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import audioplayer.AudioPlayerInterface;
import collision.Collision;
import gamemodel.GameModel;
import gamemodel.Observer;
import rocket.Rocket;
import spaceship.BlueSpaceShip;
import spaceship.GreenSpaceShip;
import spaceship.SpaceShip;

public class GameBoard implements Observer<Boolean> {

	private static final int NUMBER_OF_INVADERS = 20;
	private static final int PLAYER_SPEED = 20;
	protected static final double DEFAULT_SPEED_MODIFIER = 10.0;
	private static final int EASY_SPEED = 2;
	private static final int MEDIUM_SPEED = 5;
	private static final int HARD_SPEED = 7;
	private static final int LIMIT = 50;
	
	private int invaderShipsLeft;
	private static int speed;
	private DataCollectionInterface dataCollection;

	private List<SpaceShip> invaders = new ArrayList<>();
	private List<Rocket> rockets = new ArrayList<>();
	private Player player;
	private Dimension2D size;
	private AudioPlayerInterface audioplayer;

	private DifficultyLevel level;
	private boolean running;
	private boolean paused;
	private GameOutcome gameOutcome = GameOutcome.OPEN;

	// for observer pattern
	private final GameModel model;
	private Timer gameTimer;
	private static final int UPDATE_PERIOD = 2000;

	// added one parameter for the observer pattern
	public GameBoard(Dimension2D size, DifficultyLevel level, GameModel model) {
		this.model = model;
		model.addObserver(this);

		player = new Player(new BlueSpaceShip(size));
		player.getShip().setSpeed(PLAYER_SPEED);
		createInvaders();
		this.level = level;
		chooseDifficultyLevel(level);
		setInvadersSpeed(speed);
		this.size = size;
		gameOutcome = GameOutcome.OPEN;

	}


	public void chooseSpeed(DifficultyLevel newLevel) {
		switch (newLevel) {
			case EASY:
				speed = EASY_SPEED;
				break;
			case MEDIUM:
				speed = MEDIUM_SPEED;
				break;
			default:
				speed = HARD_SPEED;
				break;
		}
	}

	public void startGame() {
		// createInvaders();
		model.setRunning(true);
		playMusic();
		startTimer();
	}

	public void update() {
		moveShips();
	}

	public void stopGame() {
		stopMusic();
		model.setRunning(false);
		if (invaders.isEmpty()) {
			createInvaders();
		}
		invaders.get(0).resetPositioning();
		this.running = false;
		gameTimer.cancel();
	}

	private void startTimer() {
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				if (!paused && running) {
					randomInvaderShoot();
				}

			}
		};
		if (this.gameTimer != null) {
			this.gameTimer.cancel();
		}
		this.gameTimer = new Timer();
		this.gameTimer.scheduleAtFixedRate(timerTask, UPDATE_PERIOD, UPDATE_PERIOD);
	}

	public void createInvaders() {
		for (int i = 0; i < NUMBER_OF_INVADERS; i++) {
			SpaceShip invader = new GreenSpaceShip(size);
			invader.setSpeed(speed);
			invaders.add(invader);
		}
	}

	public void playMusic() {
		this.audioplayer.playBackgroundMusic();

	}

	public void stopMusic() {
		this.audioplayer.stopBackgroundMusic();
	}


	public void chooseDifficultyLevel(DifficultyLevel newLevel) {
		this.level = newLevel;
		chooseSpeed(level);
	}

	public void pauseGame() {
		if (!paused) {
			model.setPaused(!paused);
			audioplayer.stopBackgroundMusic();
		}
	}

	public void resumeGame() {
		if (paused) {
			model.setPaused(!paused);
			audioplayer.playBackgroundMusic();
		}

	}

	public void shootRocket(Rocket rocket) {
		this.rockets.add(rocket);

	}

	public void moveInvadersDown() {
		for (SpaceShip spaceShip : invaders) {
			Point2D position = spaceShip.getPosition();
			spaceShip.setPosition(new Point2D(position.getX(), position.getY() + spaceShip.getSpeed() * DEFAULT_SPEED_MODIFIER), size);
		}
	}

	public void moveShips() {
		if (running && !paused && gameOutcome == GameOutcome.OPEN) {

			List<SpaceShip> newInvaders = new ArrayList<>();
			newInvaders.addAll(invaders);
			// List<Rocket> newRockets = new ArrayList<>();
			// newRockets.addAll(rockets);
			for (SpaceShip spaceShip : newInvaders) {
				spaceShip.moveShip(size, speed);
				if (spaceShip.getShouldTurn()) {
					moveInvadersDown();
					spaceShip.setShouldTurn(false);
				}

			}

			for (SpaceShip spaceShip : invaders) {
				for (Rocket rocket : rockets) {
					rocket.moveRocket(speed);
					if (rocket.getParent() instanceof BlueSpaceShip) {
						Collision collision = new Collision(spaceShip, rocket);
						if (collision.detectCollision()) {
							spaceShip.explode();
							audioplayer.playExplosionSound();
							player.increaseScore();
							newInvaders.remove(spaceShip);
							rocket.destroy();
						}
					} else {
						Collision collision = new Collision(player.getShip(), rocket);
						if (collision.detectCollision()) {
							audioplayer.playExplosionSound();
							player.getShip().explode();
							gameOutcome = GameOutcome.LOST;
						}
					}
				}

			}
			List<Rocket> tRockets = new ArrayList<>();
			tRockets.addAll(rockets);
			for (Rocket rocket : tRockets) {
				if (!rocket.isIsShot() || rocket.getPosition().getY() + rocket.getSize().getHeight() > size.getHeight()
						|| rocket.getPosition().getY() + rocket.getSize().getHeight() < 0) {
					rockets.remove(rocket);
				}
			}

			invaders = newInvaders;

			if (reachedGameBoardLimits(invaders)) {
				gameOutcome = GameOutcome.LOST;
			}
			if (gameHasEnded()) {
				gameOutcome = GameOutcome.WON;
			}

		}

	}

	public boolean reachedGameBoardLimits(List<SpaceShip> invadersList) {
		for (SpaceShip invader : invadersList) {
			if (invader.getPosition().getY() + invader.getSize().getHeight() >= size.getHeight() - LIMIT) {
				return true;
			}
		}
		return false;
	}

	private void randomInvaderShoot() {
		for (int i = 0; i < 1; i++) {
			int random = new Random().nextInt(invaders.size());
			rockets.add(invaders.get(random).shootRocket());
			audioplayer.playShootingSound();

		}
	}

	public boolean gameHasEnded() {
		if (invaders.isEmpty()) {
			return true;
		}
		for (SpaceShip spaceship : invaders) {
			if (!spaceship.isHitByRocket()) {
				return false;
			}
		}
		return true;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed2) {
		speed = speed2;
	}

	public boolean isRunning() {
		return this.running;
	}

	public void setIsRunning(boolean isRunning) {
		this.running = isRunning;
	}

	public boolean isPaused() {
		return this.paused;
	}

	public void setIsPaused(boolean isPaused) {
		this.paused = isPaused;
	}

	public int getInvaderShipsLeft() {
		return invaderShipsLeft;
	}

	public void setInvaderShipsLeft(int invaderShipsLeft) {
		this.invaderShipsLeft = invaderShipsLeft;
	}

	public List<SpaceShip> getInvaders() {
		return invaders;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Dimension2D getSize() {
		return this.size;
	}

	public void setSize(Dimension2D size) {
		this.size = size;
	}

	public AudioPlayerInterface getAudioplayer() {
		return this.audioplayer;
	}

	public void setAudioplayer(AudioPlayerInterface audioplayer) {
		this.audioplayer = audioplayer;
	}

	public DifficultyLevel getLevel() {
		return this.level;
	}

	public void setLevel(DifficultyLevel level) {
		this.level = level;
	}

	public void setInvaders(List<SpaceShip> invaders) {
		this.invaders = invaders;
	}

	public List<Rocket> getRockets() {
		return this.rockets;
	}

	public void setRockets(List<Rocket> rockets) {
		this.rockets = rockets;
	}

	public GameOutcome getGameOutcome() {
		return this.gameOutcome;
	}

	public void setGameOutcome(GameOutcome gameOutcome) {
		this.gameOutcome = gameOutcome;
	}

	private void setInvadersSpeed(int speed2) {
		invaders.forEach(invader -> invader.setSpeed(speed2));
	}

	@Override
	public void onUpdate(Boolean newStateRunning, Boolean newStatePaused) {
		running = newStateRunning;
		paused = newStatePaused;
	}

	public boolean sendData() {
		return dataCollection.saveScoreData(player.getScore());
	}

	public void setDataCollectionInterface(DataCollectionInterface dataCollectionInterface) {
		this.dataCollection = dataCollectionInterface;
	}

}