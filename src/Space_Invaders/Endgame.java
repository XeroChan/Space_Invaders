package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Endgame extends JPanel {
    JPanel endgamePanel,endScreen, score;
    JLabel s,e;

    Endgame(int points, boolean hasWon) throws IOException {
        endgamePanel = new JPanel();
        endgamePanel.setLayout(new BorderLayout());

        score = new JPanel();
        score.setBackground(Color.decode("#4E458C"));
        endgamePanel.add(score, BorderLayout.NORTH);
        if (Integer.parseInt(getHighscore())<points) {
            s = new JLabel("Highscore = "+points);
            setHighscore(points);
        } else {
            s = new JLabel("Highscore = "+getHighscore());
        }
        s.setForeground(Color.decode("#ffffff"));
        score.add(s);


        endScreen = new JPanel();
        endScreen.setBackground(Color.decode("#4E458C"));
        endgamePanel.add(endScreen, BorderLayout.CENTER);
        if(hasWon){
            e = new JLabel("You win!");
        }else{
            e = new JLabel("You lose!");
        }
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
    public void setHighscore(int points) throws IOException {

        String str = String.valueOf(points);
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/highscore.txt"));
        writer.write(str);
        writer.close();
    }

    public String getHighscore() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/data/highscore.txt"));
        return reader.readLine();
    }
}
