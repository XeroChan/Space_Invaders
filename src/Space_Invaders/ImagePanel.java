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
    GameFrame frame;
    Timer timer;
    ArrayList<ImageIcon> heartIcons;
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
    private long lastMoveTime;
    private static final long MIN_TIME_BETWEEN_SHOTS = 200;
    private static final long MIN_TIME_BETWEEN_ALIEN_SHOTS = 280;
    private static final long MIN_TIME_BETWEEN_ALIEN_MOVEMENT = 40;
    private final JPanel score;
    private int points;
    private int numAliens;
    private boolean moveLeft = true;
    private JLabel highscore;

    public void createBufferImage() {
        bufferImage = createImage(getWidth(), getHeight());
        bufferGraphics = bufferImage.getGraphics();
    }

    public ImagePanel(GameFrame frame, Timer timer, JPanel score, JPanel lives) {
        aliens = new ArrayList<>();
        spaceshipLasers = new ArrayList<>();
        alienLasers = new ArrayList<>();
        keyboard = new KeyboardHandling();
        this.frame = frame;
        this.timer = timer;
        this.score = score;
        this.lives = lives;

        heartIcons = new ArrayList<>();

        try {
            ImageIcon fullHeart = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("..\\assets\\heart.png"))));

            BufferedImage resizedFull = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);

            Graphics2D graphics2D = resizedFull.createGraphics();
            graphics2D.drawImage(fullHeart.getImage(), 0, 0, 20, 20, null);
            graphics2D.dispose();

            heartIcons.add(new ImageIcon(resizedFull));
        } catch (IOException e) {
            e.printStackTrace();
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

        long currentTime = System.currentTimeMillis();
        for (Alien alien : aliens) {
            if (alien.getPosY() == getHeight()-alienHeight) {
                stop();
            }
        }
        if (!aliens.isEmpty() && currentTime - lastMoveTime >= MIN_TIME_BETWEEN_ALIEN_MOVEMENT) {
            if (moveLeft) {
                for (Alien alien : aliens) {
                    if (alien.getPosX() <= 0) {
                        moveLeft = false;
                        break;
                    }
                }

                for (int m = 0; m < aliens.size(); m++) {
                    aliens.get(m).moveLeft();
                    if (aliens.get(m).getPosY() <= getHeight()) {
                        aliens.get(m).moveDown();
                    }
                }
                lastMoveTime = currentTime;
            }
            if (!moveLeft) {
                for (Alien alien : aliens) {
                    if (alien.getPosX() >= getWidth()-alienWidth) {
                        moveLeft = true;
                        break;
                    }
                }
                for (int m = 0; m < aliens.size(); m++) {
                    aliens.get(m).moveRight();
                    if (aliens.get(m).getPosY() <= getHeight()) {
                        aliens.get(m).moveDown();
                    }
                }
                lastMoveTime = currentTime;
            }
        }


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

        for (int k = 0; k < aliens.size(); k++) {
            if (Math.random() < (0.1 / aliens.size())) {
                if (currentTime - alienShotTime >= MIN_TIME_BETWEEN_ALIEN_SHOTS) {
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
        stop();
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
            stop();
        }
    }

    private void stop() {
        timer.stop();
        System.out.println("Timer stopped");
        Endgame endPanel;
        try {
            endPanel = new Endgame(points);
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
                spaceship.setPosX(getWidth() / 2 - spaceshipWidth/2);
                spaceship.setPosY(getHeight() - spaceshipHeight);
                initialValuesSet = true;
            } else bufferGraphics.drawImage(spaceship.getResizedImage(spaceshipWidth, spaceshipHeight), spaceship.getPosX(), spaceship.getPosY(), null);
        }
        for (Alien alien : aliens) {
            bufferGraphics.drawImage(alien.getResizedImage(alienWidth, alienHeight), alien.getPosX(), alien.getPosY(), null);
        }
        for (Laser laser : spaceshipLasers) {
            bufferGraphics.drawImage(laser.draw(), laser.getPosX(), laser.getPosY(), null);
        }
        for (Laser laser : alienLasers) {
            bufferGraphics.drawImage(laser.draw(), laser.getPosX(), laser.getPosY(), null);
        }
        g.drawImage(bufferImage, 0, 0, null);
    }

    public void setAlienNumber(int numAliens) {
        this.numAliens = numAliens;
    }

    public void setAlienDimension(int alienWidth, int alienHeight) {
        this.alienWidth = alienWidth;
        this.alienHeight = alienHeight;
    }

    public void setSpaceshipDimension(int spaceshipWidth, int spaceshipHeight) {
        this.spaceshipWidth = spaceshipWidth;
        this.spaceshipHeight = spaceshipHeight;
    }
}
