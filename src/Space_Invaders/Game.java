package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JPanel {
    JPanel gameFrame, score, lives;
    JLabel s, l;
    static ImagePanel graphicsPanel;
    Timer timer;

    Game() {
        gameFrame = new JPanel();
        gameFrame.setLayout(new BorderLayout());

        score = new JPanel();
        score.setBackground(Color.decode("#4E458C"));
        gameFrame.add(score, BorderLayout.NORTH);
        s = new JLabel("score");
        s.setForeground(Color.decode("#ffffff"));
        score.add(s);

        lives = new JPanel();
        lives.setBackground(Color.decode("#4E458C"));
        gameFrame.add(lives, BorderLayout.SOUTH);
        l = new JLabel("lives");
        l.setForeground(Color.decode("#ffffff"));
        lives.add(l);

        graphicsPanel = new ImagePanel();
        gameFrame.add(graphicsPanel, BorderLayout.CENTER);

        run();
        timer = new Timer(10, e -> graphicsPanel.update());
        timer.start();
    }

    private void run() {
        Spaceship spaceship = new Spaceship();
        graphicsPanel.addSpaceship(spaceship);

        new Thread(() -> {
            int numAliens = 5;
            int alienGap = 10;
            Alien alienDim = new Alien();
            int alienWidth = alienDim.getResizedImage(69, 69).getWidth();
            int totalAlienWidth = alienWidth * numAliens;
            int startX = (graphicsPanel.getWidth() - (totalAlienWidth + alienGap * (numAliens - 1))) / 2;

            for (int i = 0; i < numAliens; i++) {
                Alien alien = new Alien();
                alien.setPosX(startX + i * (alienWidth + alienGap));
                alien.setPosY(0);
                graphicsPanel.addAlien(alien);
            }
        }).start();
    }
}
