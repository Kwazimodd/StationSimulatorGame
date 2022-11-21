package ua.pz33.rendering.animation;

import org.intellij.lang.annotations.MagicConstant;
import ua.pz33.rendering.animation.interpolation.Interpolator;
import ua.pz33.rendering.animation.interpolation.Interpolators;
import ua.pz33.sprites.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TimedStoryboard implements Storyboard {
    private int durationMs = 100;
    private Interpolator interpolator = Interpolators.LINEAR;
    private final List<Animation> animations = new ArrayList<>();
    private Runnable onExecuted;

    private TimedStoryboard() {

    }

    public boolean step(Sprite sprite, long timeElapsed) {
        var absoluteTime = ((double) timeElapsed) / durationMs;
        var shouldFinish = false;

        if (timeElapsed > durationMs) {
            shouldFinish = true;

            absoluteTime = 1;
        }

        var absoluteValue = interpolator.getAbsoluteAnimationState(absoluteTime);

        for (var animation : animations) {
            animation.step(sprite, absoluteValue);
        }

        if (shouldFinish && onExecuted != null) {
            EventQueue.invokeLater(onExecuted);
        }

        return shouldFinish;
    }

    public static class Builder {
        private final TimedStoryboard storyboard = new TimedStoryboard();

        public Builder withDuration(int durationMs) {
            storyboard.durationMs = durationMs;

            return this;
        }

        public Builder withAnimation(Animation animation) {
            storyboard.animations.add(animation);

            return this;
        }

        public Builder withAnimations(Animation... animations) {
            storyboard.animations.addAll(List.of(animations));

            return this;
        }

        public Builder withInterpolator(@MagicConstant(valuesFromClass = Interpolators.class) Interpolator interpolator) {
            storyboard.interpolator = interpolator;

            return this;
        }

        public Builder addOnExecutedListener(Runnable onExecuted) {
            storyboard.onExecuted = onExecuted;

            return this;
        }

        public TimedStoryboard build() {
            if (storyboard.interpolator == null) {
                throw new NullPointerException("Interpolator can't be null");
            }

            return storyboard;
        }
    }
}
