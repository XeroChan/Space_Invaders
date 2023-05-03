package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Game extends JPanel {
    JPanel gameFrame, score, lives, game;
    JLabel s,l,g;
    Spaceship spaceship;
    static ImagePanel graphicsPanel;
    Game(){
        gameFrame = new JPanel();
        gameFrame.setLayout(new BorderLayout());

        score = new JPanel();
        score.setBackground(Color.decode("#021226"));
        gameFrame.add(score, BorderLayout.NORTH);
        s = new JLabel("score");
        s.setForeground(Color.decode("#ffffff"));
        score.add(s);

        lives = new JPanel();
        lives.setBackground(Color.decode("#021226"));
        gameFrame.add(lives,BorderLayout.SOUTH);
        l = new JLabel("lives");
        l.setForeground(Color.decode("#ffffff"));
        lives.add(l);


        spaceship = new Spaceship();
        Image spaceshipResized = spaceship.getResizedImage(69,69);
        graphicsPanel = new ImagePanel();
        graphicsPanel.setBackground(Color.decode("#4E458C"));
        graphicsPanel.addSpaceship(spaceshipResized);
        gameFrame.add(graphicsPanel, BorderLayout.CENTER);
        new Thread(this::run).start();


    }


    private void run() {
        while (true) {
            graphicsPanel.update();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
