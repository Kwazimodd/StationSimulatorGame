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
    private List<Client> clients;
    private List<ClientSprite> clientSprites;

    public void AddClient(Client client, Entrance entrance){
        GameClock.getInstance().addObserver(client);
        clients.add(client);

        ClientSprite clientSprite = new ClientSprite(client);
        clientSprite.setX(entrance.getX());
        clientSprite.setY(entrance.getY());
        SpriteRegistry.getInstance().registerSprite(clientSprite);
        clientSprites.add(clientSprite);
    }

    public List<Client> getClients(){
        return clients;
    }

    public List<ClientSprite> getClientSprites(){
        return clientSprites;
    }

    public Optional<ClientSprite> getClientSprite(int id){
        return clientSprites.stream().filter(clientSprite -> clientSprite.getId() == id).findFirst();
    }
}
