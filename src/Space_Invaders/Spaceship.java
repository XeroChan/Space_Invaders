package Space_Invaders;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Spaceship {
    private BufferedImage cannonImage;

    public Spaceship() {
        try {
            cannonImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("..\\assets\\spcship.png")));
            System.out.println("Image width: " + cannonImage.getWidth() + ", height: " + cannonImage.getHeight());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public BufferedImage getResizedImage(int targetWidth, int targetHeight) {
        // Resize the cannon image to targetWidth x targetHeight pixels
        BufferedImage resizedCannonImage = resizeImage(cannonImage, targetWidth, targetHeight);
        System.out.println("Resized image width: " + resizedCannonImage.getWidth() + ", height: " + resizedCannonImage.getHeight());
        return resizedCannonImage;
    }

}
