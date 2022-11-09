package ua.pz33.rendering.animation;

import ua.pz33.sprites.Sprite;

public interface Animation {
    void step(Sprite sprite, double absoluteValue);
}
