package Space_Invaders;

import javax.swing.*;
import java.awt.*;

public class Menu {
    JFrame frame;
    JPanel menu, title;
    JLabel titleLabel;
    JButton start;

    Menu() {
        frame = new JFrame("Space Invaders");

        menu = new JPanel();
        menu.setBackground(Color.decode("#021226"));

        title = new JPanel();
        title.setBackground(Color.decode("#021226"));
        titleLabel = new JLabel("Space Invaders");
        titleLabel.setForeground(Color.decode("#ffffff"));
        titleLabel.setFont(new Font("Impact", Font.ITALIC, 32));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        title.add(titleLabel);

        start = new JButton("Play");
        start.setPreferredSize(new Dimension(200, 50));
        start.setBackground(Color.decode("#ffffff"));

        start.addActionListener(e -> {
            Game gamePanel = new Game();
            gamePanel.gameFrame.setPreferredSize(frame.getSize());
            frame.getContentPane().removeAll();
            frame.getContentPane().add(gamePanel.gameFrame);
            frame.setMinimumSize(frame.getSize());
            frame.pack();
            frame.requestFocus();
            gamePanel.graphicsPanel.requestFocus();
        });

        menu.setLayout(new BorderLayout());
        menu.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.decode("#021226"));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(50, 0, 50, 0);
        centerPanel.add(start, c);
        JLabel spaceLightspeed = new JLabel(new ImageIcon("src\\assets\\lightspeed_sv.gif"));
        centerPanel.add(spaceLightspeed, c);


        menu.add(centerPanel, BorderLayout.CENTER);

        frame.add(menu);

        frame.setSize(500, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
    }
}
