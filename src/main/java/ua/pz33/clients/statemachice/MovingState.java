package ua.pz33.clients.statemachice;

import ua.pz33.clients.Client;

public class MovingState extends State {
    public MovingState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        //todo getting position to move from client and executing move in clientSprite

        //change position of move if cashregister was closed while client was moving
        //if position of client sprite is position of service, cashregister.service() can be executed
        client.changeState(new ServicedState(client));

        //if is serviced, moving to the exit
        //if on exit, execute delete itself in client
        }
}
