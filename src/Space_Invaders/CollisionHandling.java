package Space_Invaders;

import java.awt.Rectangle;

public class CollisionHandling {
    public static boolean checkCollision(Spaceship spaceship, Alien alien, int objectWidth, int objectHeight) {
        Rectangle spaceshipBounds = spaceship.getBounds(objectWidth, objectHeight);
        Rectangle alienBounds = alien.getBounds(objectWidth, objectHeight);
        return spaceshipBounds.intersects(alienBounds);
    }

    public static boolean checkCollision(Laser laser, Alien alien, int objectWidth, int objectHeight) {
        Rectangle laserBounds = laser.getBounds();
        Rectangle alienBounds = alien.getBounds(objectWidth, objectHeight);
        return laserBounds.intersects(alienBounds);
    }

    public static boolean checkCollision(Laser laser, Spaceship spaceship, int objectWidth, int objectHeight) {
        Rectangle laserBounds = laser.getBounds();
        Rectangle spaceshipBounds = spaceship.getBounds(objectWidth, objectHeight);
        return laserBounds.intersects(spaceshipBounds);
    }
}
