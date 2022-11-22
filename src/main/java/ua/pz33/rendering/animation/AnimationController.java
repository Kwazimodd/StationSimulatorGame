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

    public void beginAnimation(Sprite onSprite, Storyboard storyboard) {
        activeAnimations.add(new AnimationRecord(onSprite, storyboard));
    }

    public void animationStep() {
        if (activeAnimations.isEmpty()) {
            return;
        }

        int deletedCount = 0;

        int animCount = activeAnimations.size();
        for (int i = 0; i < animCount - deletedCount; i++) {
            var animationRecord = activeAnimations.get(i);

            var animationFinished = animationRecord.storyboard.step(animationRecord.sprite, animationRecord.deltaMs());

            if (animationFinished) {
                activeAnimations.remove(i);

                deletedCount++;

                i--;
            }
        }
    }

    private static class AnimationRecord {
        Sprite sprite;
        Storyboard storyboard;
        long startTimeMs;

        public AnimationRecord(Sprite sprite, Storyboard storyboard) {
            this.sprite = sprite;
            this.storyboard = storyboard;

            startTimeMs = System.currentTimeMillis();
        }

        long deltaMs() {
            return System.currentTimeMillis() - startTimeMs;
        }
    }
}
