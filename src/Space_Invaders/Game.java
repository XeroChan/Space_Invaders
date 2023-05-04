package Space_Invaders;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {
    JPanel gameFrame, score, lives;
    JLabel s,l;
    static ImagePanel graphicsPanel;
    Game(){
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
        gameFrame.add(lives,BorderLayout.SOUTH);
        l = new JLabel("lives");
        l.setForeground(Color.decode("#ffffff"));
        lives.add(l);

        graphicsPanel = new ImagePanel();
        gameFrame.add(graphicsPanel, BorderLayout.CENTER);
        new Thread(this::run).start();
    }

    private void run() {
        Spaceship spaceship = new Spaceship();
        graphicsPanel.addSpaceship(spaceship);
        while (true) {
            graphicsPanel.update();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
