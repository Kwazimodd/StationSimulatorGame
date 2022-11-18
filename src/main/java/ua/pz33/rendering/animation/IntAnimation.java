package ua.pz33.rendering.animation;

import ua.pz33.sprites.Sprite;

import java.util.function.ObjIntConsumer;

public class IntAnimation implements Animation {
    private int from, to;
    private double delta;
    private ObjIntConsumer<Sprite> propertySetter;

    private IntAnimation() {
    }

    @Override
    public void step(Sprite sprite, double absoluteValue) {
        var newValue = from + delta * absoluteValue;

        propertySetter.accept(sprite, (int) newValue);
    }

    public static class Builder {
        private final IntAnimation animation = new IntAnimation();

        public Builder withBounds(int from, int to) {
            animation.from = from;
            animation.to = to;

            animation.delta = to - from;

            return this;
        }

        public Builder withProperty(ObjIntConsumer<Sprite> setter) {
            animation.propertySetter = setter;

            return this;
        }

        public IntAnimation build() {
            return animation;
        }
    }
}
