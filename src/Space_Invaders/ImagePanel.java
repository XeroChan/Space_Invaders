package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;


public class ImagePanel extends JPanel {
    GameFrame frame;
    private ArrayList<Alien> aliens;
    private ArrayList<Laser> spaceshipLasers;
    private ArrayList<Laser> alienLasers;
    private KeyboardHandling keyboard;
    private Spaceship spaceship;
    private int spaceshipWidth;
    private int spaceshipHeight;
    private int alienWidth;
    private int alienHeight;
    private boolean initialValuesSet = false;
    private Image bufferImage;
    private Graphics bufferGraphics;
    private long lastShotTime;
    private long alienShotTime;
    private static final long MIN_TIME_BETWEEN_SHOTS = 200;
    private static final long MIN_TIME_BETWEEN_ALIEN_SHOTS = 280;

    public void createBufferImage() {
        bufferImage = createImage(getWidth(), getHeight());
        bufferGraphics = bufferImage.getGraphics();
    }

    public ImagePanel(GameFrame frame) {
        aliens = new ArrayList<>();
        spaceshipLasers = new ArrayList<>();
        alienLasers = new ArrayList<>();
        keyboard = new KeyboardHandling();
        this.frame = frame;
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
                    int gap = 100;
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
        alienWidth = alien.getResizedImage(69, 69).getWidth();
        alienHeight = alien.getResizedImage(69, 69).getHeight();
        aliens.add(alien);
    }

    public void update() {

        for (int i=0; i<aliens.size();i++) {
            if (CollisionHandling.checkCollision(spaceship, aliens.get(i))) {
                handleSpaceshipAlienCollision();
                break;
            }
        }

        for (int i=0; i<spaceshipLasers.size();i++) {
            for (int j=0; j<aliens.size();j++) {
                if (CollisionHandling.checkCollision(spaceshipLasers.get(i), aliens.get(j))) {
                    handleLaserAlienCollision(spaceshipLasers.get(i), aliens.get(j));
                    break;
                }
            }
        }

        for (int i=0; i<alienLasers.size();i++) {
            if (CollisionHandling.checkCollision(alienLasers.get(i), spaceship)) {
                handleLaserSpaceshipCollision(alienLasers.get(i));
                break;
            }
        }

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

        for (int k=0; k<aliens.size();k++) {
            if (Math.random() < (0.1 / aliens.size())) {
                if(currentTime - alienShotTime >= MIN_TIME_BETWEEN_ALIEN_SHOTS) {
                    Laser laser = aliens.get(k).shootLaser();
                    alienLasers.add(laser);
                    alienShotTime = currentTime;
                }
            }
        }
        alienLasers.removeIf(laser -> laser.getPosY() > getHeight());
        repaint();
    }

    private void handleSpaceshipAlienCollision() {
    }

    private void handleLaserAlienCollision(Laser laser, Alien alien) {
        spaceshipLasers.remove(laser);

        if (alien.getHealth() >=1) alien.reduceHealth();
        else aliens.remove(alien);
    }

    private void handleLaserSpaceshipCollision(Laser laser) {
        alienLasers.remove(laser);


        if (spaceship.getHealth() >= 1) spaceship.reduceHealth();
        else {
            /*Endgame endPanel = new Endgame();
            endPanel.endgamePanel.setPreferredSize(frame.getSize());
            frame.getContentPane().removeAll();
            frame.getContentPane().add(endPanel.endgamePanel);
            frame.setMinimumSize(frame.getSize());
            frame.pack();
            frame.requestFocus();
            endPanel.endgamePanel.requestFocus();*/
        }
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
}
