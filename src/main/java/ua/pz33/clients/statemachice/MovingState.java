package ua.pz33.clients.statemachice;

import ua.pz33.Station;
import ua.pz33.clients.Client;
import ua.pz33.clients.ClientController;

import java.awt.*;

public class MovingState extends State {
    public MovingState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        var clientSprite = ClientController.getInstance().getClientSprite(client.getId()).get();
        var goalPoint = client.getGoalPoint();

        if(!client.getCashRegister().isOpen()){
            client.changeState(new IdleState(client));
        }

        //todo to test if position of client sprite is position of service, cashregister.service() can be executed
        if(clientSprite.getX() == goalPoint.x && clientSprite.getY() == goalPoint.y){
            client.getCashRegister().service();
            client.changeState(new ServicedState(client));
        }

        if(client.wasServiced()){
            var exit = Station.getInstance().getExit();
            var exitPoint = new Point(exit.getX(), exit.getY());
            client.setGoalPoint(exitPoint);
        }

        //if on exit, execute delete itself in client

        clientSprite.moveTo(goalPoint.x, goalPoint.y);
    }
}
