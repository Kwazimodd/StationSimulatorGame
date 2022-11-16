package ua.pz33.clients.statemachice;

import ua.pz33.clients.Client;

public class IdleState extends State {
    public IdleState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        //client.chooseCashRegister();
    }
}
