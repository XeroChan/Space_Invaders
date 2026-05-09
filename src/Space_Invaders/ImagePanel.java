package Space_Invaders;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class ImagePanel extends JPanel {
    private final JPanel lives;
    private final JPanel score;
    private JLabel highscore;
    GameFrame frame;
    Timer timer;
    ArrayList<ImageIcon> heartIcons;
    private final ArrayList<Alien> aliens;
    private final ArrayList<Laser> spaceshipLasers;
    private final ArrayList<Laser> alienLasers;
    private final KeyboardHandling keyboard;
    private Spaceship spaceship;
    private final int objectWidth;
    private final int objectHeight;
    private boolean initialValuesSet = false;
    private Image bufferImage;
    private Graphics bufferGraphics;
    private long lastShotTime;
    private long alienShotTime;
    private long lastMoveTime;
    private static final long MIN_TIME_BETWEEN_SHOTS = 200;
    private static final long MIN_TIME_BETWEEN_ALIEN_SHOTS = 280;
    private static final long MIN_TIME_BETWEEN_ALIEN_MOVEMENT = 40;
    private int points;
    private boolean moveLeft = true;
    private boolean hasStarted = false;

    public void createBufferImage() {
        bufferImage = createImage(getWidth(), getHeight());
        bufferGraphics = bufferImage.getGraphics();
    }

    public ImagePanel(GameFrame frame, Timer timer, JPanel score, JPanel lives, int objectWidth, int objectHeight) {
        aliens = new ArrayList<>();
        spaceshipLasers = new ArrayList<>();
        alienLasers = new ArrayList<>();
        keyboard = new KeyboardHandling();
        this.frame = frame;
        this.timer = timer;
        this.score = score;
        this.lives = lives;
        this.objectWidth = objectWidth;
        this.objectHeight = objectHeight;

        heartIcons = new ArrayList<>();

        try {
            ImageIcon fullHeart = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/assets/heart.png"))));

            BufferedImage resizedFull = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);

            Graphics2D graphics2D = resizedFull.createGraphics();
            graphics2D.drawImage(fullHeart.getImage(), 0, 0, 20, 20, null);
            graphics2D.dispose();

            heartIcons.add(new ImageIcon(resizedFull));
        } catch (IOException e) {
            throw new IllegalStateException("Could not load heart image.", e);
        }



        addKeyListener(keyboard);
    }

    public void addSpaceship(Spaceship spaceship) {
        this.spaceship = spaceship;
        displayLives(spaceship);
    }

    private void displayLives(Spaceship spaceship) {
        lives.removeAll();

        JPanel heartsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        heartsPanel.setBackground(Color.decode("#4E458C"));
        for (int i = 0; i < spaceship.getLives(); i++) {
            JLabel heartLabel = new JLabel();
            heartLabel.setIcon(heartIcons.get(0));
            heartsPanel.add(heartLabel);
        }

        lives.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JLabel l = new JLabel("lives ");
        l.setForeground(Color.decode("#ffffff"));
        lives.add(l);
        lives.add(heartsPanel);

        lives.revalidate();
        lives.repaint();

        highscore = new JLabel("Score = "+points);
        highscore.setForeground(Color.decode("#ffffff"));

        score.add(highscore);

    }


    public void addAlien(Alien alien) {
        aliens.add(alien);
    }

    public void update() {
        if (!hasStarted && !aliens.isEmpty()) {
            hasStarted = true;
        }

        if (hasStarted && aliens.isEmpty()) {
            stop(true);
            return;
        }
        long currentTime = System.currentTimeMillis();
        for (Alien alien : aliens) {
            if (alien.getPosY() == getHeight()-objectHeight) {
                stop(false);
                return;
            }
        }
        if (!aliens.isEmpty() && currentTime - lastMoveTime >= MIN_TIME_BETWEEN_ALIEN_MOVEMENT) {
            hasStarted = true;
            if (moveLeft) {
                for (Alien alien : aliens) {
                    if (alien.getPosX() <= 0) {
                        moveLeft = false;
                        break;
                    }
                }

                for (Alien alien : aliens) {
                    alien.moveLeft();
                    if (alien.getPosY() <= getHeight()) {
                        alien.moveDown();
                    }
                }
                lastMoveTime = currentTime;
            }
            if (!moveLeft) {
                for (Alien alien : aliens) {
                    if (alien.getPosX() >= getWidth()-objectWidth) {
                        moveLeft = true;
                        break;
                    }
                }
                for (Alien alien : aliens) {
                    alien.moveRight();
                    if (alien.getPosY() <= getHeight()) {
                        alien.moveDown();
                    }
                }
                lastMoveTime = currentTime;
            }
        }


        for (Alien alien : aliens) {
            if (CollisionHandling.checkCollision(spaceship, alien, objectWidth, objectHeight)) {
                handleSpaceshipAlienCollision();
                break;
            }
        }

        for (int i = spaceshipLasers.size() - 1; i >= 0; i--) {
            Laser spaceshipLaser = spaceshipLasers.get(i);

            for (int j = aliens.size() - 1; j >= 0; j--) {
                Alien alien = aliens.get(j);

                if (CollisionHandling.checkCollision(spaceshipLaser, alien, objectWidth, objectHeight)) {
                    handleLaserAlienCollision(spaceshipLaser, alien);
                    break;
                }
            }
        }

        for (int i = alienLasers.size() - 1; i >= 0; i--) {
            Laser alienLaser = alienLasers.get(i);

            if (CollisionHandling.checkCollision(alienLaser, spaceship, objectWidth, objectHeight)) {
                handleLaserSpaceshipCollision(alienLaser);
                break;
            }
        }

        if (keyboard.isLeftPressed() && spaceship.getPosX() > 0) {
            spaceship.updatePosition(-3, 0);
        }
        if (keyboard.isRightPressed() && spaceship.getPosX()+objectWidth<getWidth()) {
            spaceship.updatePosition(3, 0);
        }
        if (keyboard.isSpacePressed() && currentTime - lastShotTime >= MIN_TIME_BETWEEN_SHOTS) {
            Laser laser = spaceship.shootLaser(objectWidth, objectHeight);
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

        for (int k = 0; k < aliens.size(); k++) {
            if (Math.random() < (0.1 / aliens.size())) {
                if (currentTime - alienShotTime >= MIN_TIME_BETWEEN_ALIEN_SHOTS) {
                    Laser laser = aliens.get(k).shootLaser(objectWidth, objectHeight);
                    alienLasers.add(laser);
                    alienShotTime = currentTime;
                }
            }


        }
        alienLasers.removeIf(laser -> laser.getPosY() > getHeight());

        repaint();
    }

    private void handleSpaceshipAlienCollision() {
        stop(false);
    }

    private void handleLaserAlienCollision(Laser laser, Alien alien) {
        spaceshipLasers.remove(laser);

        if (alien.getHealth() > 1) alien.reduceHealth();
        else{
            aliens.remove(alien);
            points += 10;
            highscore.setText("Score = "+points);
            score.revalidate();
            score.repaint();
        }
    }

    private void handleLaserSpaceshipCollision(Laser laser) {
        alienLasers.remove(laser);

        if (spaceship.getHealth() > 1){
            spaceship.reduceHealth();
            displayLives(spaceship);
        }
        else {
            stop(false);
        }
    }

    private void stop(boolean hasWon) {
        timer.stop();
        System.out.println("Timer stopped");
        Endgame endPanel;
        try {
            endPanel = new Endgame(points, hasWon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        endPanel.setPreferredSize(frame.getSize());
        frame.getContentPane().removeAll();
        frame.getContentPane().add(endPanel.endgamePanel);
        frame.setMinimumSize(frame.getSize());
        frame.pack();
        frame.requestFocus();
        endPanel.endgamePanel.requestFocus();
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
                spaceship.setPosX(getWidth() / 2 - objectWidth/2);
                spaceship.setPosY(getHeight() - objectHeight);
                initialValuesSet = true;
            } else bufferGraphics.drawImage(spaceship.getResizedImage(objectWidth, objectHeight), spaceship.getPosX(), spaceship.getPosY(), null);
        }
        for (Alien alien : aliens) {
            bufferGraphics.drawImage(alien.getResizedImage(objectWidth, objectHeight), alien.getPosX(), alien.getPosY(), null);
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
