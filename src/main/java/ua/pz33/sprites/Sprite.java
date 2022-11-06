package ua.pz33.sprites;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Comparator;

public abstract class Sprite implements Comparable<Sprite> {
    public static final int Z_INDEX_UI = 1999;
    public static final int Z_INDEX_BACKGROUND = -1;

    private Rectangle bounds = new Rectangle(0, 0);

    public abstract int getZIndex();

    @Override
    public int compareTo(@NotNull Sprite o) {
        return Comparator.comparingInt(Sprite::getZIndex).compare(this, o);
    }

    public abstract void paint(Graphics g);

    public void setX(int x) {
        bounds = new Rectangle(bounds);
        bounds.x = x;
    }

    public void setY(int y) {
        bounds = new Rectangle(bounds);
        bounds.y = y;
    }

    public void setWidth(int width) {
        bounds = new Rectangle(bounds);
        bounds.width = width;
    }

    public void setHeight(int height) {
        bounds = new Rectangle(bounds);
        bounds.height = height;
    }

    public int getX() {
        return bounds.x;
    }

    public int getY() {
        return bounds.y;
    }

    public int getWidth() {
        return bounds.width;
    }

    public int getHeight() {
        return bounds.height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
