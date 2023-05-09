package Space_Invaders;

import java.awt.Rectangle;

public class CollisionHandling {
    public static boolean checkCollision(Spaceship spaceship, Alien alien) {
        Rectangle spaceshipBounds = spaceship.getBounds();
        Rectangle alienBounds = alien.getBounds();
        return spaceshipBounds.intersects(alienBounds);
    }

    public static boolean checkCollision(Laser laser, Alien alien) {
        Rectangle laserBounds = laser.getBounds();
        Rectangle alienBounds = alien.getBounds();
        return laserBounds.intersects(alienBounds);
    }

    public static boolean checkCollision(Laser laser, Spaceship spaceship) {
        Rectangle laserBounds = laser.getBounds();
        Rectangle spaceshipBounds = spaceship.getBounds();
        return laserBounds.intersects(spaceshipBounds);
    }
}
