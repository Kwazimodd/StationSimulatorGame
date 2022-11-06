package ua.pz33.registries;

import ua.pz33.sprites.Sprite;

import java.util.*;

public class SpriteRegistry {
    private static SpriteRegistry instance;

    private final List<Sprite> sprites = new ArrayList<>();

    private SpriteRegistry() {

    }

    public static SpriteRegistry getInstance() {
        if (instance == null) {
            instance = new SpriteRegistry();
        }

        return instance;
    }

    public void registerSprite(Sprite sprite) {
        var z = sprite.getZIndex();

        if (sprites.size() == 0) {
            sprites.add(sprite);

            return;
        }

        if (sprites.get(0).getZIndex() > z) {
            sprites.add(0, sprite);

            return;
        }

        if (sprites.get(sprites.size() - 1).getZIndex() < z) {
            sprites.add(sprite);

            return;
        }

        int l = 0, r = sprites.size() - 1;

        while (r - l > 1) {
            int m = (r + l) / 2;

            if (sprites.get(m).getZIndex() < z) {
                l = m;
            } else {
                r = m;
            }
        }

        sprites.add(r, sprite);
    }

    public Iterable<Sprite> getRegisteredSprites() {
        return sprites;
    }
}
