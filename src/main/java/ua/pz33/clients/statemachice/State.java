package ua.pz33.clients.statemachice;

import ua.pz33.clients.Client;

public abstract class State {
    protected Client client;
    public State(Client client){
        this.client = client;
    }

    public abstract void perform();
}
