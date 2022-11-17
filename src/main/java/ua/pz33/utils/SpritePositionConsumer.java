package ua.pz33.utils;

import ua.pz33.sprites.Sprite;

import java.awt.*;

@FunctionalInterface
public interface SpritePositionConsumer {
    void accept(Sprite sprite, Point point);
}
