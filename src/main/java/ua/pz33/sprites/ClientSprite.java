package ua.pz33.sprites;

import ua.pz33.clients.Client;
import ua.pz33.clients.ClientStatus;

import java.util.Map;

public class ClientSprite extends ImageSprite {
    private int id;
    private static final Map<ClientStatus, String> clientStatusImageMap = Map.of(
            ClientStatus.REGULAR, "Body200X200.png",
            ClientStatus.INVALID, "BodyExempt200X200.png",
            ClientStatus.VIP, "BodyVIP200X200.png",
            ClientStatus.HAS_KIDS, "BodyWithChild200X200.png");

    public ClientSprite(String image) {
        super(image);
    }

    public ClientSprite(String image, int zIndex) {
        super(image, zIndex);
    }

    public ClientSprite(Client client) {
        super(clientStatusImageMap.get(client.getStatus()), 10);
        id = client.getId();
    }

    public int getId() {
        return id;
    }
}
