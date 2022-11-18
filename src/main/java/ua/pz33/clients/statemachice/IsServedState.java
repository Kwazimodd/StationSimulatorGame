package ua.pz33.clients.statemachice;

import ua.pz33.clients.Client;
import ua.pz33.controllers.StationController;

import java.awt.*;

public class IsServedState extends State {
    public IsServedState(Client client) {
        super(client);
    }

    @Override
    public void onSwitchedTo() {
        customerController.onClientServiced(client);
    }

    @Override
    public void perform() {
    }
}
