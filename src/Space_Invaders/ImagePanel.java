package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ImagePanel extends JPanel {
    private ArrayList<Image> images;
    private KeyboardHandling keyboard;
    private Spaceship spaceship;
    private boolean initialValuesSet = false;


    public ImagePanel() {
        images = new ArrayList<>();
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
            spaceship.updatePosition(0, -2);
        }
        repaint();
        System.out.println("New ship coords: x: " + spaceship.getPosX() + " y: " + spaceship.getPosY());
    }
}
