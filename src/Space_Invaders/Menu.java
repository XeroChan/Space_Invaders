package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Menu {

    GameFrame frame;
    JPanel menu, title;
    JLabel titleLabel;
    JButton start, fullscreen;

    Menu(boolean startFullScreen) {
        frame = new GameFrame();

        menu = new JPanel();
        menu.setBackground(Color.decode("#021226"));
        menu.setLayout(new BorderLayout());

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
        start.setFocusPainted(false);

        fullscreen = new JButton("Fullscreen");
        fullscreen.setPreferredSize(new Dimension(200, 50));
        fullscreen.setBackground(Color.decode("#ffffff"));
        fullscreen.setFocusPainted(false);
        fullscreen.addActionListener(e -> frame.toggleFullScreen());

        start.addActionListener(e -> {
            Game gamePanel = new Game(frame);
            frame.setContentPane(gamePanel.gameFrame);
            frame.revalidate();
            frame.repaint();
            SwingUtilities.invokeLater(gamePanel::requestGameFocus);
        });

        menu.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.decode("#021226"));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(50, 0, 50, 0);
        centerPanel.add(start, c);

        c.gridy = 1;
        c.insets = new Insets(0, 0, 50, 0);
        centerPanel.add(fullscreen, c);

        JLabel spaceLightspeed = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/lightspeed_sv.gif"))));
        c.gridy = 2;
        c.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(spaceLightspeed, c);

        menu.add(centerPanel, BorderLayout.CENTER);

        frame.add(menu);

        frame.setVisible(true);
        frame.setFullScreen(startFullScreen);
    }

    public static void main(String[] args) {
        boolean startFullScreen = args.length > 0 && "--fullscreen".equalsIgnoreCase(args[0]);
        new Menu(startFullScreen);
    }
}
