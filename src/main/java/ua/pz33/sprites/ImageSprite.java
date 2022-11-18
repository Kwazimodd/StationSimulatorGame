package ua.pz33.sprites;

import ua.pz33.rendering.animation.AnimationController;
import ua.pz33.rendering.animation.IntAnimation;
import ua.pz33.rendering.animation.Storyboard;
import ua.pz33.rendering.animation.interpolation.Interpolators;
import ua.pz33.utils.DistanceCounter;
import ua.pz33.utils.ResourceLoader;

import java.awt.*;

public class ImageSprite extends Sprite {
    private final int zIndex;
    protected Image spriteImage;

    public ImageSprite(String image) {
        this(image, 0);
    }

    public ImageSprite(String image, int zIndex) {
        this.zIndex = zIndex;
        spriteImage = ResourceLoader.getInstance().loadImage(image);
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();

        g.drawImage(spriteImage, getX(), getY(), getWidth(), getHeight(), null);

        g.dispose();
    }

    public void setSprite(Image image)
    {
        spriteImage = image;
    }
}
