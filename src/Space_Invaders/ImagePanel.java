package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ImagePanel extends JPanel {
    private ArrayList<Image> images;
    private Image spaceship;
    private KeyboardHandling keyboard;
    private int centerX;
    private int centerY;


    public ImagePanel() {
        images = new ArrayList<>();
        keyboard = new KeyboardHandling();
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(keyboard);
    }

    public void addImage(Image image) {
        images.add(image);
        int width = 0;
        int height = 0;
        for (Image img : images) {
            width = Math.max(width, img.getWidth(this));
            height = Math.max(height, img.getHeight(this));
        }
        setPreferredSize(new Dimension(width, height));
        repaint();
    }

    public void addSpaceship(Image image) {
        spaceship = image;
        images.add(spaceship);
        centerX = 40;
        centerY = 200;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (spaceship != null) {
            g.drawImage(spaceship, centerX, centerY, null);
        } else {
            for (Image image : images) {
                g.drawImage(image, getWidth() / 2 - image.getWidth(this) / 2, getHeight() - image.getHeight(this), null);
            }
        }
    }


    public int spaceshipX() {
        if (spaceship != null) {
            return centerX;
        } else {
            return 0;
        }
    }

    public int spaceshipY() {
        if (spaceship != null) {
            return centerY;
        } else {
            return 0;
        }
    }


    public void update() {
        if (keyboard.isLeftPressed()) {
            centerX -= 5;
        }
        if (keyboard.isRightPressed()) {
            centerX += 5;
        }
        if (keyboard.isSpacePressed()) {
            centerY -= 5;
        }
        repaint();
        System.out.println("New ship coords: x: " + centerX + " y: " + centerY);
    }
}
