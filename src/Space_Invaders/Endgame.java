package Space_Invaders;

import javax.swing.*;
import java.awt.*;

public class Endgame extends JPanel {
    JPanel endgamePanel,endScreen, score;
    JLabel s,e;

    Endgame() {
        endgamePanel = new JPanel();
        endgamePanel.setLayout(new BorderLayout());

        score = new JPanel();
        score.setBackground(Color.decode("#4E458C"));
        endgamePanel.add(score, BorderLayout.NORTH);
        s = new JLabel("Highscore");
        s.setForeground(Color.decode("#ffffff"));
        score.add(s);

        endScreen = new JPanel();
        endScreen.setBackground(Color.decode("#4E458C"));
        endgamePanel.add(endScreen, BorderLayout.CENTER);
        e = new JLabel("You lose!");
        e.setForeground(Color.decode("#ffffff"));
        endScreen.add(e);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.decode("#021226"));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(50, 0, 50, 0);
        centerPanel.add(e, c);
        centerPanel.setBackground(Color.decode("#4E458C"));

        endgamePanel.add(centerPanel, BorderLayout.CENTER);

    }
}
