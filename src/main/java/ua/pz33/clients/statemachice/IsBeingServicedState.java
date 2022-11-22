package ua.pz33.clients.statemachice;

import ua.pz33.controllers.StationController;
import ua.pz33.clients.Client;

import java.awt.*;

public class IsBeingServicedState extends State {
    public IsBeingServicedState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        // do nothing
    }
}
