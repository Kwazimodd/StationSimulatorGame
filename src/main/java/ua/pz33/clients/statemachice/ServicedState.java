package ua.pz33.clients.statemachice;

import ua.pz33.clients.Client;

public class ServicedState extends State {
    public ServicedState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        //todo if client is servicing, do nothing -- there could be error
        if(client.wasServiced()){
            client.changeState(new MovingState(client));
        }
    }
}
