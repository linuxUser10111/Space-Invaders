// this class was not implemented by Baraa Alnassan

import gamemodel.GameModel;
import gameview.GameBoardUI;
import gameview.GameToolBar;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SpaceInvadersApplication extends Application {

	private static final int GRID_LAYOUT_PADDING = 5;
	private static final int GRID_LAYOUT_PREF_HEIGHT = 350;
	private static final int GRID_LAYOUT_PREF_WIDTH = 505;

	/**
	 * Starts the Bumpers Window by setting up a new tool bar, a new user interface
	 * and adding them to the stage.
	 *
	 * @param primaryStage the primary stage for this application, onto which the
	 *                     application scene can be set.
	 */
	@Override
	public void start(Stage primaryStage) {
		//for observer pattern
		GameModel model = new GameModel();

		// the tool bar object with start and stop buttons
		GameToolBar toolBar = new GameToolBar();
		GameBoardUI gameBoardGUI = new GameBoardUI(toolBar, model);
		toolBar.initializeActions(gameBoardGUI);

		Pane gridLayout = createLayout(gameBoardGUI, toolBar);

		// scene and stages
		Scene scene = new Scene(gridLayout);
		scene.setOnKeyPressed(gameBoardGUI);
		primaryStage.setTitle("Space Invaders");
		primaryStage.setScene(scene);
		// primaryStage.getIcons().add(new
		// Image(getClass().getResourceAsStream("icon.png")));
		primaryStage.setOnCloseRequest(closeEvent -> gameBoardGUI.stopGame());
		primaryStage.show();
	}

	/**
	 * Creates a new {@link Pane} that arranges the game's UI elements.
	 */
	private static Pane createLayout(GameBoardUI gameBoardUI, GameToolBar toolBar) {
		// GridPanes are divided into columns and rows, like a table
		GridPane gridLayout = new GridPane();
		gridLayout.setPrefSize(GRID_LAYOUT_PREF_WIDTH, GRID_LAYOUT_PREF_HEIGHT);
		gridLayout.setVgap(GRID_LAYOUT_PADDING);
		gridLayout.setPadding(new Insets(GRID_LAYOUT_PADDING));

		// add all components to the gridLayout
		// second parameter is column index, second parameter is row index of grid
		gridLayout.add(gameBoardUI, 0, 1);
		gridLayout.add(toolBar, 0, 0);
		return gridLayout;
	}

	/**
	 * The whole game will be executed through the launch() method.
	 * <p>
	 * Use to run the Java application.
	 */
	public static void startApp(String[] args) {
		launch(args);
	}

}