package ua.pz33.rendering.animation;

import ua.pz33.Main;
import ua.pz33.sprites.Sprite;
import ua.pz33.utils.SpritePositionConsumer;

import java.awt.*;

public class VelocityPositionAnimation implements VelocityAnimation {
    private int velocityPtsPerS = 200;
    private Point from, to, curr;
    private SpritePositionConsumer propertySetter;
    private double sine;
    private double cosine;

    private VelocityPositionAnimation() {
    }

    @Override
    public boolean step(Sprite sprite, long deltaMs) {
        var distance = velocityPtsPerS * deltaMs / 1000d;

        int dy = (int) (distance * cosine);
        int dx = (int) (distance * sine);

        int newX = curr.x + dx;
        int newY = curr.y + dy;

        newX = clamp(newX, from.x, to.x);
        newY = clamp(newY, from.y, to.y);

        Point newValue = new Point(newX, newY);

        propertySetter.accept(sprite, newValue);

        curr = newValue;

        return newValue.equals(to);
    }

    private static int clamp(int a, int a1, int a2) {
        var min = Math.min(a1, a2);
        var max = Math.max(a1, a2);

        if (a < min) {
            return min;
        }

        if (a > max) {
            return max;
        }

        return a;
    }

    public static class Builder {
        private final VelocityPositionAnimation animation = new VelocityPositionAnimation();

        public Builder withBounds(Point from, Point to) {
            animation.curr = animation.from = from;
            animation.to = to;

            return this;
        }

        public Builder withVelocity(int velocityPointsPerSecond) {
            animation.velocityPtsPerS = velocityPointsPerSecond;

            return this;
        }

        public Builder withProperty(SpritePositionConsumer setter) {
            animation.propertySetter = setter;

            return this;
        }

        public VelocityPositionAnimation build() {
            animation.sine = ((double) (animation.to.x - animation.from.x)) / animation.to.distance(animation.from);
            animation.cosine = ((double) (animation.to.y - animation.from.y)) / animation.to.distance(animation.from);

            return animation;
        }
    }
}
