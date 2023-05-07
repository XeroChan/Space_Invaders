package Space_Invaders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Alien {
    private BufferedImage alienImage;
    private int posX;
    private int posY;

    public Alien() {
        try {
            alienImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("..\\assets\\spcship.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
