
import static org.junit.jupiter.api.Assertions.assertTrue;

//import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

//import org.junit.jupiter.api.extension.ExtendWith;
//import org.easymock.EasyMockExtension;
//import org.easymock.Mock;
//import org.easymock.TestSubject;

import collision.Collision;
import gamecontroller.Dimension2D;
import gamecontroller.Player;
import gamecontroller.Point2D;
import rocket.Rocket;
import spaceship.BlueSpaceShip;
import spaceship.GreenSpaceShip;
//import static org.easymock.EasyMock.expect;
//import static org.easymock.EasyMock.replay;

//@ExtendWith(EasyMockExtension.class)
public class TestCollision {

    @Test
    public void testCollisionDetectionOnInvader() {
        Dimension2D size = new Dimension2D(800, 500);
        Rocket rocket = new Rocket(new Point2D(550, 150), new BlueSpaceShip(size), size);
        GreenSpaceShip invader = new GreenSpaceShip(size);
        invader.setPosition(new Point2D(550, 150), size);
        Collision collision = new Collision(invader, rocket);

        assertTrue(collision.detectCollision());
    }

    @Test
    public void testCollisionDetectionOnPlayer() {
        Dimension2D size = new Dimension2D(800, 500);
        Rocket rocket = new Rocket(new Point2D(550, 150), new GreenSpaceShip(size), size);
        Player player = new Player(new BlueSpaceShip(size));
        player.getShip().setPosition(new Point2D(550, 150), size);
        Collision collision = new Collision(player.getShip(), rocket);

        assertTrue(collision.detectCollision());
    }
}
