package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class ImagePanel extends JPanel {
    private ArrayList<Image> images;
    private Image spaceship;
    private KeyboardHandling keyboard;
    private int posX=100;
    private int posY=100;
    private int panelWidth;
    private int panelHeight;


    public ImagePanel() {
        images = new ArrayList<>();
        keyboard = new KeyboardHandling();
        addKeyListener(keyboard);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelWidth = getWidth();
                panelHeight = getHeight();
            }
        });
    }

    public void addImage(Image image) {
        images.add(image);
        int width = 0;
        int height = 0;
        for (Image img : images) {
            width = Math.max(width, img.getWidth(this));
            height = Math.max(height, img.getHeight(this));
        }
        setPreferredSize(new Dimension(width, height));
        repaint();
    }

    public void addSpaceship(Image spaceship) {

        int spaceshipWidth = spaceship.getWidth(this);
        int spaceshipHeight = spaceship.getHeight(this);
        System.out.println("Panel height: " + panelHeight + " Panel width: " + panelWidth);
        System.out.println("Image height: " + spaceshipHeight + " Image width: " + spaceshipWidth);



    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (spaceship != null) {
            g.drawImage(spaceship, posX, posY, null);
        } else {
            for (Image image : images) {
                g.drawImage(image, getWidth() / 2 - image.getWidth(this) / 2, getHeight() - image.getHeight(this), null);
            }
        }
    }

    int getPanelHeight(){
        return getHeight();
    };
    int getPanelWidth(){
        return getWidth();
    };

    public void update() {
        System.out.println("has focus: " + hasFocus());
        System.out.println("Height: " + getHeight() + " Width: " + getWidth());
        if (keyboard.isLeftPressed()) {
            posX -= 5;
        }
        if (keyboard.isRightPressed()) {
            posX += 5;
        }
        if (keyboard.isSpacePressed()) {
            posY -= 5;
        }
        repaint();
        System.out.println("New ship coords: x: " + posX + " y: " + posY);
    }
}
