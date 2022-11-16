package ua.pz33.clients;

import ua.pz33.rendering.SpriteRegistry;
import ua.pz33.sprites.ClientSprite;
import ua.pz33.sprites.Entrance;
import ua.pz33.utils.clock.GameClock;

import java.util.List;
import java.util.Optional;

public class ClientController {
    private static ClientController instance;

    public static ClientController getInstance() {
        if (instance == null) {
            instance = new ClientController();
        }

        return instance;
    }
}
