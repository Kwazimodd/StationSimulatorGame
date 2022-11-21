package ua.pz33.rendering.animation;

import org.intellij.lang.annotations.MagicConstant;
import ua.pz33.rendering.animation.interpolation.Interpolator;
import ua.pz33.rendering.animation.interpolation.Interpolators;
import ua.pz33.sprites.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VelocityStoryboard implements Storyboard {
    private static final long TIMEOUT = 5_000;
    private final List<VelocityAnimation> animations = new ArrayList<>();
    private Runnable onExecuted;

    private long lastFrame = 0;

    private VelocityStoryboard() {
    }

    public boolean step(Sprite sprite, long timeElapsed) {
        if (timeElapsed > TIMEOUT) {
            for (var animation : animations) {
                animation.step(sprite, timeElapsed - lastFrame);
            }

            if (onExecuted != null) {
                EventQueue.invokeLater(onExecuted);
            }


            return true;
        }

        var shouldFinish = true;

        for (var animation : animations) {
            shouldFinish = shouldFinish && animation.step(sprite, timeElapsed - lastFrame);
        }

        lastFrame = timeElapsed;

        if (shouldFinish && onExecuted != null) {
            EventQueue.invokeLater(onExecuted);
        }

        return shouldFinish;
    }

    public static class Builder {
        private final VelocityStoryboard storyboard = new VelocityStoryboard();

        public Builder withAnimation(VelocityAnimation animation) {
            storyboard.animations.add(animation);

            return this;
        }

        public Builder withAnimations(VelocityAnimation... animations) {
            storyboard.animations.addAll(List.of(animations));

            return this;
        }

        public Builder addOnExecutedListener(Runnable onExecuted) {
            storyboard.onExecuted = onExecuted;

            return this;
        }

        public VelocityStoryboard build() {
            return storyboard;
        }
    }
}
