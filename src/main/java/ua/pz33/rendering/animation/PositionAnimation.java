package ua.pz33.rendering.animation;

import ua.pz33.sprites.Sprite;
import ua.pz33.utils.SpritePositionConsumer;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

public class PositionAnimation implements Animation {
    private Point from, to;
    private Point delta;
    private SpritePositionConsumer propertySetter;

    private PositionAnimation() {
    }

    @Override
    public void step(Sprite sprite, double absoluteValue) {
        Point newValue = new Point(from.x + (int) (delta.x * absoluteValue), from.y + (int) (delta.y * absoluteValue));

        propertySetter.accept(sprite, newValue);
    }

    public static class Builder {
        private final PositionAnimation animation = new PositionAnimation();

        public Builder withBounds(Point from, Point to) {
            animation.from = from;
            animation.to = to;

            animation.delta = new Point(to.x - from.x, to.y - from.y);

            return this;
        }

        public Builder withProperty(SpritePositionConsumer setter) {
            animation.propertySetter = setter;

            return this;
        }

        public PositionAnimation build() {
            return animation;
        }
    }
}
