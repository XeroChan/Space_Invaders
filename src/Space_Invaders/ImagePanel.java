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
    private Image bufferImage;
    private Graphics bufferGraphics;

    public void createBufferImage() {
        bufferImage = createImage(getWidth(), getHeight());
        bufferGraphics = bufferImage.getGraphics();

    }



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
        if (bufferImage == null) {
            createBufferImage();
        }
        bufferGraphics.clearRect(0, 0, getWidth(), getHeight());
        bufferGraphics.setColor(Color.decode("#4E458C"));
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        if (spaceship != null) {
            if (!initialValuesSet) {
                spaceship.setPosX(getWidth() / 2 - spaceship.getResizedImage(69, 69).getWidth()/2);
                spaceship.setPosY(getHeight() - spaceship.getResizedImage(69, 69).getHeight());
                initialValuesSet = true;
            } else bufferGraphics.drawImage(spaceship.getResizedImage(69, 69), spaceship.getPosX(), spaceship.getPosY(), null);
        }
        for (Image image : images) {
            bufferGraphics.drawImage(image, getWidth() / 2 - image.getWidth(this) / 2, getHeight() - image.getHeight(this), null);
        }
        for (Laser laser : lasers) {
            bufferGraphics.drawImage(laser.draw(), laser.getPosX(), laser.getPosY(), null);
        }
        g.drawImage(bufferImage, 0, 0, null);
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
        }

        for (Laser laser : lasers) {
            laser.move();
        }
        lasers.removeIf(laser -> laser.getPosY() < 0);

        repaint();
    }



}
