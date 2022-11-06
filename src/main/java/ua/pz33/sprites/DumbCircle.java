package ua.pz33.sprites;

import java.awt.*;
import java.util.Map;
import java.util.Random;

public class DumbCircle extends Sprite {

    private static final Random random = new Random(System.currentTimeMillis());

    private final Color circleColor;

    private int zIndex = 1;

    public DumbCircle(Color circleColor) {
        this.circleColor = circleColor;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(circleColor);

        g.fillOval(getX(), getY(), getWidth(), getHeight());

        setX(getX() + Math.max(random.nextInt(1005) - 1000, 0));
        setY(getY() + Math.max(random.nextInt(1005) - 1000, 0));
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
