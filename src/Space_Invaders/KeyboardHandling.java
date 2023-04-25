package Space_Invaders;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandling implements KeyListener {
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean spacePressed;

    public KeyboardHandling() {
        leftPressed = false;
        rightPressed = false;
        spacePressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> leftPressed = true;
            case KeyEvent.VK_RIGHT -> rightPressed = true;
            case KeyEvent.VK_SPACE -> spacePressed = true;
            default -> {
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> leftPressed = false;
            case KeyEvent.VK_RIGHT -> rightPressed = false;
            case KeyEvent.VK_SPACE -> spacePressed = false;
        }
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isSpacePressed() {
        return spacePressed;
    }
}


