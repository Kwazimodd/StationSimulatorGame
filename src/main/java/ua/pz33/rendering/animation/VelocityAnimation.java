package ua.pz33.rendering.animation;

import ua.pz33.sprites.Sprite;

public interface VelocityAnimation {
    boolean step(Sprite sprite, long deltaMs);
}
