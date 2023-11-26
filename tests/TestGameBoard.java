import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import gamecontroller.DifficultyLevel;
import gamecontroller.GameBoard;
import gamecontroller.Player;
import gamemodel.GameModel;
import spaceship.BlueSpaceShip;
import spaceship.SpaceShip;
import gamecontroller.Dimension2D;


public class TestGameBoard {

    @Test
    public void testInvadersCreationInGameBoard() {
        GameBoard gameBoard = new GameBoard(new Dimension2D(800, 500), DifficultyLevel.EASY, new GameModel());
        assertTrue(gameBoard.getInvaders().size() > 0);
    }

    @Test
    public void testGameIsnNotRunning() {
        GameBoard gameBoard =  new GameBoard(new Dimension2D(800, 500), DifficultyLevel.EASY, new GameModel());
        assertFalse(gameBoard.isRunning());
    }

    @Test
    public void testObserverPatternRunningOnGameBoard() {
        GameModel model = new GameModel();
        GameBoard gameBoard = new GameBoard(new Dimension2D(800, 500), DifficultyLevel.EASY, model);
        model.setRunning(true);

        assertTrue(gameBoard.isRunning());

    }

    @Test
    public void testObserverPatternPausedOnGameBoard() {
        GameModel model = new GameModel();
        GameBoard gameBoard = new GameBoard(new Dimension2D(800, 500), DifficultyLevel.EASY, model);
        model.setPaused(true);

        assertTrue(gameBoard.isPaused());

    }

    @Test
    public void testScoreIncreased() {
        Dimension2D size = new Dimension2D(800, 500);
        Player player = new Player(new BlueSpaceShip(size));
        int oldScore = player.getScore();
        player.increaseScore();
        assertTrue(player.getScore() >= oldScore);
    }

    @Test
    public void testGameHasEnded() {
        Dimension2D size = new Dimension2D(800, 500);
        GameBoard gameBoard = new GameBoard(size, DifficultyLevel.EASY, new GameModel());

        for (SpaceShip invader : gameBoard.getInvaders()) {
            invader.explode();
        }
        
        boolean observedValue = gameBoard.gameHasEnded();
        assertTrue(observedValue);
    }

    @Test
    public void testGameHasNotEnded() {
        Dimension2D size = new Dimension2D(800, 500);
        GameBoard gameBoard = new GameBoard(size, DifficultyLevel.EASY, new GameModel());
        boolean observedValue = gameBoard.gameHasEnded();
        assertFalse(observedValue);
    }









    /**
     * package de.tum.in.ase.eist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.tum.in.ase.eist.gamecontroller.DataCollectionInterface;
import de.tum.in.ase.eist.gamecontroller.DifficultyLevel;
import de.tum.in.ase.eist.gamecontroller.Dimension2D;
import de.tum.in.ase.eist.gamecontroller.GameBoard;
import de.tum.in.ase.eist.gamemodel.GameModel;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(EasyMockExtension.class)
public class DataCollectionTest {

    @Mock
    private DataCollectionInterface dataCollectionMock;

    @TestSubject
    private GameBoard gameBoard = new GameBoard(new Dimension2D(800, 500), DifficultyLevel.EASY, new GameModel());

    @Test
    public void testScoreSent() {
        gameBoard.setDataCollectionInterface(dataCollectionMock);
        expect(dataCollectionMock.saveScoreData(gameBoard.getPlayer().getScore())).andReturn(true);
        replay(dataCollectionMock);
        
        assertTrue(gameBoard.sendData());
    }

    @Test
    public void testScoreNotSent() {
        gameBoard.setDataCollectionInterface(dataCollectionMock);
        expect(dataCollectionMock.saveScoreData(gameBoard.getPlayer().getScore())).andReturn(false);
        replay(dataCollectionMock);
        
        assertFalse(gameBoard.sendData());
    }
}

     */
}
