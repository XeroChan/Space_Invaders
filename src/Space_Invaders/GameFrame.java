package Space_Invaders;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        super("Space Invaders");
        setAlwaysOnTop(true);
        setResizable(false);
        setSize(500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
