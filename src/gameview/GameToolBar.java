package gameview;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;

public class GameToolBar extends ToolBar {
	 private final Button start;
	    private final Button stop;
	    private final Button pause;
		private Text scoreText;
		private static final int FONT_SIZE = 18;
	    //private final Label gameModeLabel;
	    //private final CheckBox gameMode;

	    public GameToolBar() {
	        this.start = new Button("Start");
	        this.stop = new Button("Stop");
	        this.pause = new Button("Pause");
			this.scoreText = new Text("SCORE: 0");
			this.scoreText.setFont(Font.font("Brush Script MT", FontWeight.BOLD, FONT_SIZE));
			this.scoreText.setFill(Color.GREEN);
	        //this.gameModeLabel = new Label("Game mode");
	        //this.gameMode = new CheckBox("Hardcore-Mode");
	        // the game is stopped initially
	        updateToolBarStatus(false);
	        getItems().addAll(this.start, new Separator(), this.stop, new Separator(),this.pause, new Separator(), scoreText);
	    }

	    /**
	     * Initializes the actions of the toolbar buttons.
	     */
	    public void initializeActions(GameBoardUI gameBoardGUI) {
	        this.start.setOnAction(event -> gameBoardGUI.startGame());

	       /* this.gameMode.setOnAction(event -> {
	            //this.gameMode.setSelected(!this.gameMode.isSelected());
	            gameBoardGUI.getGameBoard().setGameMode(this.gameMode.isSelected());
	        });*/

	        this.pause.setOnAction(event -> {
	            if(gameBoardGUI.getGameBoard().isPaused()) {
	                gameBoardGUI.resumeGame();
				} else { 
	                gameBoardGUI.pauseGame();
				}
	        });
	        this.stop.setOnAction(event -> {
	            // stop the game while the alert is shown
	            gameBoardGUI.stopGame();

	            Alert alert = new Alert(AlertType.CONFIRMATION, "Do you really want to stop the game?", ButtonType.YES,
	                    ButtonType.NO);
	            alert.setTitle("Stop Game Confirmation");
	            // By default the header additionally shows the Alert Type (Confirmation)
	            // but we want to disable this to only show the question
	            alert.setHeaderText("");

	            Optional<ButtonType> result = alert.showAndWait();
	            // reference equality check is OK here because the result will return the same
	            // instance of the ButtonType
	            if (result.isPresent() && result.get() == ButtonType.YES) {
	                // reset the game board to prepare the new game
	                gameBoardGUI.setup();
	            } else {
	                // continue running
	                gameBoardGUI.startGame();
	            }
	        });
	    }

		public void updateScore(int newScore) {
			scoreText.setText("SCORE: " + newScore);
		}

	    /**
	     * Updates the status of the toolbar. This will for example enable or disable
	     * buttons.
	     *
	     * @param running true if game is running, false otherwise
	     */
	    public void updateToolBarStatus(boolean running) {
	        this.start.setDisable(running);
	        this.stop.setDisable(!running);
			this.pause.setDisable(!running);
	    }
}
