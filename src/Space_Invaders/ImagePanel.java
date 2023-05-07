package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class ImagePanel extends JPanel {
    private ArrayList<Alien> aliens;
    private ArrayList<Laser> spaceshipLasers;
    private ArrayList<Laser> alienLasers;
    private KeyboardHandling keyboard;
    private Spaceship spaceship;
    private Alien alien;
    private int spaceshipWidth;
    private int spaceshipHeight;
    private int alienWidth;
    private int alienHeight;
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
        aliens = new ArrayList<>();
        spaceshipLasers = new ArrayList<>();
        alienLasers = new ArrayList<>();
        keyboard = new KeyboardHandling();
        addKeyListener(keyboard);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                createBufferImage();
                revalidate();
                repaint();

                if (spaceship != null) {
                    int panelWidth = getWidth();
                    int panelHeight = getHeight();

                    int startX = (panelWidth - spaceshipWidth) / 2;
                    int startY = panelHeight - spaceshipHeight;

                    spaceship.setPosX(startX);
                    spaceship.setPosY(startY);

                    int totalAlienWidth = alienWidth * aliens.size();
                    int gap = 100; // Specify the desired gap between aliens
                    int startAlienX = (panelWidth - (totalAlienWidth + gap * (aliens.size() - 1))) / 2;

                    for (int i = 0; i < aliens.size(); i++) {
                        Alien alien = aliens.get(i);
                        alien.setPosX(startAlienX + i * (alienWidth + gap));
                    }
                }
            }
        });

    }

    public void addSpaceship(Spaceship spaceship) {
        this.spaceship = spaceship;
        spaceshipWidth = spaceship.getResizedImage(69, 69).getWidth();
        spaceshipHeight = spaceship.getResizedImage(69, 69).getHeight();
    }

    public void addAlien(Alien alien) {
        alienWidth = alien.getResizedImage(69, 69).getWidth();;
        alienHeight = alien.getResizedImage(69, 69).getHeight();;
        aliens.add(alien);
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
        for (Alien alien : aliens) {
            bufferGraphics.drawImage(alien.getResizedImage(69, 69), alien.getPosX(), alien.getPosY(), null);
        }
        for (Laser laser : spaceshipLasers) {
            bufferGraphics.drawImage(laser.draw(), laser.getPosX(), laser.getPosY(), null);
        }
        for (Laser laser : alienLasers) {
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
            spaceshipLasers.add(laser);
            lastShotTime = currentTime;
        }
        for (Laser laser : spaceshipLasers) {
            laser.move();
        }
        spaceshipLasers.removeIf(laser -> laser.getPosY() < 0);
        for (Laser alienLaser : alienLasers) {
            alienLaser.moveDown();
        }

        for (Alien alien : aliens) {
            if (Math.random() < 0.011) {
                Laser laser = alien.shootLaser();
                alienLasers.add(laser);
            }
        }
        alienLasers.removeIf(laser -> laser.getPosY() > getHeight());
        repaint();
    }


}
