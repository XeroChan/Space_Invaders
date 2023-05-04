package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ImagePanel extends JPanel {
    private ArrayList<Image> images;
    private ArrayList<Laser> lasers;
    private KeyboardHandling keyboard;
    private Spaceship spaceship;
    private int spaceshipWidth;
    private int spaceshipHeight;
    private boolean initialValuesSet = false;
    private Image bufferImage;
    private Graphics bufferGraphics;
    private long lastShotTime;
    private static final long MIN_TIME_BETWEEN_SHOTS = 200;

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
        spaceshipWidth = spaceship.getResizedImage(69, 69).getWidth();
        spaceshipHeight = spaceship.getResizedImage(69, 69).getHeight();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bufferImage == null) {
            createBufferImage();
        }
        bufferGraphics.clearRect(0, 0, getWidth(), getHeight());
        bufferGraphics.setColor(Color.decode("#021226"));
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        if (spaceship != null) {
            if (!initialValuesSet) {
                spaceship.setPosX(getWidth() / 2 - spaceshipWidth/2);
                spaceship.setPosY(getHeight() - spaceshipHeight);
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
        if (keyboard.isLeftPressed() && spaceship.getPosX() > 0) {
            spaceship.updatePosition(-3, 0);
        }
        if (keyboard.isRightPressed() && spaceship.getPosX()+spaceshipWidth<getWidth()) {
            spaceship.updatePosition(3, 0);
        }
        long currentTime = System.currentTimeMillis();
        if (keyboard.isSpacePressed() && currentTime - lastShotTime >= MIN_TIME_BETWEEN_SHOTS) {
            Laser laser = spaceship.shootLaser();
            lasers.add(laser);
            lastShotTime = currentTime;
        }
        for (Laser laser : lasers) {
            laser.move();
        }
        lasers.removeIf(laser -> laser.getPosY() < 0);
        repaint();
    }
}
