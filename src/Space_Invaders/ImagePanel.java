package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ImagePanel extends JPanel {
    private ArrayList<Image> images;
    private ArrayList<Laser> lasers;

    private KeyboardHandling keyboard;
    private Spaceship spaceship;
    private boolean initialValuesSet = false;


    public ImagePanel() {
        images = new ArrayList<>();
        lasers = new ArrayList<>();
        keyboard = new KeyboardHandling();
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

    public void addSpaceship(Spaceship spaceship) {
        this.spaceship = spaceship;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (spaceship != null) {
            if (!initialValuesSet) {
                spaceship.setPosX(getWidth() / 2 - spaceship.getResizedImage(69, 69).getWidth()/2);
                spaceship.setPosY(getHeight() - spaceship.getResizedImage(69, 69).getHeight());
                initialValuesSet = true;
            } else g.drawImage(spaceship.getResizedImage(69, 69), spaceship.getPosX(), spaceship.getPosY(), null);
        } else {
            for (Image image : images) {
                g.drawImage(image, getWidth() / 2 - image.getWidth(this) / 2, getHeight() - image.getHeight(this), null);
            }
            for (Laser laser : lasers) {
                g.drawImage(laser.draw(), laser.getPosX(), laser.getPosY(), null);
            }
        }
    }

    public void update() {
        if (keyboard.isLeftPressed()) {
            spaceship.updatePosition(-2, 0);
        }
        if (keyboard.isRightPressed()) {
            spaceship.updatePosition(2, 0);
        }
        if (keyboard.isSpacePressed()) {
            Laser laser = spaceship.shootLaser();
            lasers.add(laser);
            System.out.println("Laser shot at x: " + laser.getPosX() + " y: " + laser.getPosY());
            System.out.println("Lasers in ArrayList: " + lasers.size());
        }

        for (Laser laser : lasers) { // loop through all lasers and update their positions
            laser.move(); // move the laser object
        }
        lasers.removeIf(laser -> laser.getPosY() < 0); // remove lasers that have gone off-screen

        repaint();
        System.out.println("New ship coords: x: " + spaceship.getPosX() + " y: " + spaceship.getPosY());
    }


}
