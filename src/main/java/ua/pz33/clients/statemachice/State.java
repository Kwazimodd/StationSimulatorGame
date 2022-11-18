package ua.pz33.clients.statemachice;

import ua.pz33.clients.Client;
import ua.pz33.controllers.CustomerController;
import ua.pz33.controllers.StationController;

public abstract class State {
    protected final Client client;
    protected final CustomerController customerController;

    public State(Client client){
        this.client = client;
        customerController = StationController.getInstance();
    }

    public abstract void perform();
}
