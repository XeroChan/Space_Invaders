package Space_Invaders;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Laser {
    private int posX;
    private int posY;
    private int speed;
    private Color color;
    private int width;
    private int height;

    public Laser(int x, int y, int speed, Color color, int width, int height) {
        this.posX = x;
        this.posY = y;
        this.speed = speed;
        this.color = color;
        this.width = width;
        this.height = height;
    }

    public void move() {
        posY -= speed;
    }

    public void moveDown() {
        posY += speed;
    }

    public BufferedImage draw() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return image;
    }

    public Rectangle getBounds() {
        return new Rectangle(posX, posY, width, height);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

}
