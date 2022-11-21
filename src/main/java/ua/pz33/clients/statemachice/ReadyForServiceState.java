package ua.pz33.clients.statemachice;

import ua.pz33.clients.Client;

public class ReadyForServiceState extends State {
    public ReadyForServiceState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        // do nothing
    }
}
