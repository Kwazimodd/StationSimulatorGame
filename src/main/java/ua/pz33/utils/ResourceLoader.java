package ua.pz33.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class ResourceLoader {
    private static ResourceLoader instance;

    public static ResourceLoader getInstance() {
        if (instance == null) {
            instance = new ResourceLoader();
        }

        return instance;
    }

    public Image loadImage(String image) {
        try {
            var imageStream = this.getClass().getResourceAsStream("/" + image);

            return ImageIO.read(imageStream);
        } catch (IOException e) {
            System.err.println("Could not load image " + image);
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }
}
