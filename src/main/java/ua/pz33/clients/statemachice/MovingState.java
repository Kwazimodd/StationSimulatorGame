package ua.pz33.clients.statemachice;

import ua.pz33.StationController;
import ua.pz33.clients.Client;

import java.awt.*;

public class MovingState extends State {
    public MovingState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        var clientSprite = StationController.getInstance().getClientSprite(client.getId());
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
            var exit = StationController.getInstance().getExit();
            var exitPoint = new Point(exit.getX(), exit.getY());
            client.setGoalPoint(exitPoint);
        }

        //if on exit, execute delete itself in client

        clientSprite.moveTo(goalPoint.x, goalPoint.y);
    }
}
