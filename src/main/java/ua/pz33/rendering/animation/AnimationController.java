package ua.pz33.rendering.animation;

import ua.pz33.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

public class AnimationController {
    private static AnimationController instance;

    public static AnimationController getInstance() {
        if (instance == null) {
            instance = new AnimationController();
        }

        return instance;
    }

    private final List<AnimationRecord> activeAnimations = new ArrayList<>();

    public void beginAnimation(Sprite onSprite, Animation animation) {
        activeAnimations.add(new AnimationRecord(onSprite, animation));
    }

    public void animationStep() {
        if (activeAnimations.isEmpty()) {
            return;
        }

        int deletedCount = 0;

        for (int i = 0; i < activeAnimations.size() - deletedCount; i++) {
            var animationRecord = activeAnimations.get(i);

            var animationFinished = animationRecord.animation.step(animationRecord.sprite, animationRecord.deltaMs());

            if (animationFinished) {
                activeAnimations.remove(animationRecord);

                deletedCount++;

                i--;
            }
        }
    }

    private static class AnimationRecord {
        Sprite sprite;
        Animation animation;
        long startTimeMs;

        public AnimationRecord(Sprite sprite, Animation animation) {
            this.sprite = sprite;
            this.animation = animation;

            startTimeMs = System.currentTimeMillis();
        }

        long deltaMs() {
            return System.currentTimeMillis() - startTimeMs;
        }
    }
}
