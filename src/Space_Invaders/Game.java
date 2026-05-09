package Space_Invaders;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {
    JPanel gameFrame, score, lives;
    static ImagePanel graphicsPanel;
    int objectWidth = 40;
    int objectHeight = 40;
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
        graphicsPanel = new ImagePanel(frame, timer, score, lives, objectWidth, objectHeight);

        gameFrame.add(graphicsPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::run);

        timer.start();
    }

    private void run() {
        Spaceship spaceship = new Spaceship();



        graphicsPanel.addSpaceship(spaceship);

        int numAliens = 16;
        int aliensPerRow = numAliens/4;
        int alienGap = 10;

        int totalAlienWidthRow = objectWidth * aliensPerRow;

        int startX = (graphicsPanel.getWidth() - (totalAlienWidthRow + alienGap * (aliensPerRow - 1))) / 2;
        int startY = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < aliensPerRow; j++) {
                Alien alien = new Alien();

                alien.setPosX(startX + j * (objectWidth + alienGap));
                alien.setPosY(startY);
                graphicsPanel.addAlien(alien);
            }
            startY+=(objectWidth+alienGap);
        }
        graphicsPanel.setAlienNumber(numAliens);
    }
}
