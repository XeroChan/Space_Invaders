package Space_Invaders;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Game extends JPanel {
    JPanel gameFrame, score, lives;
    JLabel s, l;
    static ImagePanel graphicsPanel;
    Timer timer;

    Game(GameFrame frame) {
        gameFrame = new JPanel();
        gameFrame.setLayout(new BorderLayout());

        score = new JPanel();
        score.setBackground(Color.decode("#4E458C"));
        gameFrame.add(score, BorderLayout.NORTH);

        lives = new JPanel();
        lives.setBackground(Color.decode("#4E458C"));
        gameFrame.add(lives, BorderLayout.SOUTH);



        timer = new Timer(10, e -> graphicsPanel.update());
        graphicsPanel = new ImagePanel(frame, timer, score, lives);

        gameFrame.add(graphicsPanel, BorderLayout.CENTER);

        run();

        timer.start();
    }

    private void run() {
        Spaceship spaceship = new Spaceship();
        int spaceshipWidth = spaceship.getResizedImage(40, 40).getWidth();
        int spaceshipHeight = spaceship.getResizedImage(40, 40).getHeight();
        graphicsPanel.setSpaceshipDimension(spaceshipWidth,spaceshipHeight);
        graphicsPanel.addSpaceship(spaceship);

        new Thread(() -> {
            int numAliens = 16;
            int aliensPerRow = numAliens/4;
            int alienGap = 10;

            Alien alienDim = new Alien();
            int alienWidth = alienDim.getResizedImage(40, 40).getWidth();
            int alienHeight = alienDim.getResizedImage(40, 40).getHeight();

            int totalAlienWidthRow = alienWidth * aliensPerRow;

            int startX = (graphicsPanel.getWidth() - (totalAlienWidthRow + alienGap * (aliensPerRow - 1))) / 2;
            int startY = 0;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < aliensPerRow; j++) {
                    Alien alien = new Alien();

                    alien.setPosX(startX + j * (alienWidth + alienGap));
                    alien.setPosY(startY);
                    graphicsPanel.addAlien(alien);
                }
                startY+=(alienWidth+alienGap);
            }
            graphicsPanel.setAlienNumber(numAliens);
            graphicsPanel.setAlienDimension(alienWidth,alienHeight);

        }).start();
    }
}
