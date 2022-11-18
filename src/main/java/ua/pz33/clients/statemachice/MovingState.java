package ua.pz33.clients.statemachice;

import ua.pz33.StationController;
import ua.pz33.clients.Client;
import ua.pz33.sprites.Exit;

import java.awt.*;

public class MovingState extends State {
    public MovingState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        var clientSprite = StationController.getInstance().getClientSprite(client.getId());
        var goalPoint = client.getGoalPoint();

        if (!client.getCashRegister().isOpen()) {
            client.changeState(new IdleState(client));
            return;
        }
    }
}
