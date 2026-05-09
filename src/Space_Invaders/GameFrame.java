package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameFrame extends JFrame {
    private static final Dimension DEFAULT_SIZE = new Dimension(500, 800);
    private static final Dimension MINIMUM_SIZE = new Dimension(400, 600);

    private final GraphicsDevice graphicsDevice;
    private boolean fullScreen;
    private Rectangle windowedBounds;

    public GameFrame() {
        super("Space Invaders");
        graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        setResizable(true);
        setSize(DEFAULT_SIZE);
        setMinimumSize(MINIMUM_SIZE);
        setLocationRelativeTo(null);
        windowedBounds = getBounds();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFullScreenKeyBindings();
    }

    public void toggleFullScreen() {
        setFullScreen(!fullScreen);
    }

    public void setFullScreen(boolean enabled) {
        if (fullScreen == enabled) {
            return;
        }

        fullScreen = enabled;

        if (enabled) {
            windowedBounds = getBounds();
        }

        graphicsDevice.setFullScreenWindow(null);
        dispose();
        setUndecorated(enabled);
        setResizable(!enabled);

        if (enabled) {
            setVisible(true);
            graphicsDevice.setFullScreenWindow(this);
        } else {
            setBounds(windowedBounds);
            setVisible(true);
        }
    }

    private void addFullScreenKeyBindings() {
        JRootPane rootPane = getRootPane();

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleFullScreen");
        rootPane.getActionMap().put("toggleFullScreen", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                toggleFullScreen();
            }
        });

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitFullScreen");
        rootPane.getActionMap().put("exitFullScreen", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (fullScreen) {
                    setFullScreen(false);
                }
            }
        });
    }
}
