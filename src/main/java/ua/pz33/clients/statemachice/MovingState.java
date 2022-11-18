package ua.pz33.clients.statemachice;

import ua.pz33.clients.Client;

public class MovingState extends State {
    public MovingState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        if (!client.getCashRegister().isOpen()) {
            client.changeState(new IdleState(client));
        }
    }
}
