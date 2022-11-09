package ua.pz33.rendering.animation;

import org.intellij.lang.annotations.MagicConstant;
import ua.pz33.rendering.animation.interpolation.Interpolator;
import ua.pz33.rendering.animation.interpolation.Interpolators;
import ua.pz33.sprites.Sprite;

import java.util.function.ObjDoubleConsumer;

public class Animation {
    protected int durationMs = 100;
    protected Interpolator interpolator = Interpolators.LINEAR;
    protected ObjDoubleConsumer<Sprite> propertySetter;
    protected double valueFrom;
    protected double valueTo;

    private Animation() {

    }

    public boolean step(Sprite sprite, long timeElapsed) {
        var absoluteTime = ((double) timeElapsed) / durationMs;
        var shouldFinish = false;

        if (absoluteTime > 1.0) {
            shouldFinish = true;

            absoluteTime = 1;
        }

        var valueDelta = valueTo - valueFrom;

        var actualValue = valueFrom + valueDelta * interpolator.getAbsoluteAnimationState(absoluteTime);

        propertySetter.accept(sprite, actualValue);

        return shouldFinish;
    }

    public static class Builder {
        private final Animation animation = new Animation();

        public Builder withDuration(int durationMs) {
            animation.durationMs = durationMs;

            return this;
        }

        public Builder withProperty(ObjDoubleConsumer<Sprite> setter) {
            animation.propertySetter = setter;

            return this;
        }

        public Builder withBounds(double from, double to) {
            animation.valueFrom = from;
            animation.valueTo = to;

            return this;
        }

        public Builder withInterpolator(@MagicConstant(valuesFromClass = Interpolators.class) Interpolator interpolator) {
            animation.interpolator = interpolator;

            return this;
        }

        public Animation build() {
            if (animation.propertySetter == null) {
                throw new NullPointerException("Property to animate was not set!");
            }

            if (animation.interpolator == null) {
                throw new NullPointerException("Interpolator can't be null");
            }

            return animation;
        }
    }
}
