package Space_Invaders;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Spaceship {
    private BufferedImage cannonImage;
    private int posX;
    private int posY;
    private int lives = 3;


    public Spaceship() {
        try {
            cannonImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("..\\assets\\spaceship.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public BufferedImage getResizedImage(int targetWidth, int targetHeight) {
        return resizeImage(cannonImage, targetWidth, targetHeight);
    }

    public void updatePosition(int deltaX, int deltaY) {
        posX += deltaX;
        posY += deltaY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
    public int getLives() {
        return lives;
    }

    public Laser shootLaser(int objectWidth, int objectHeight) {
        int laserX = posX + objectHeight / 2;
        int laserY = posY  - objectWidth / 4;
        return new Laser(laserX, laserY, 3, Color.red, 4, 10);
    }

    public Rectangle getBounds(int objectWidth, int objectHeight) {
        return new Rectangle(posX, posY, objectWidth, objectHeight);
    }


    public int getHealth() {
        return lives;
    }

    public void reduceHealth() {
        lives--;
    }


}
