package ua.pz33.clients.statemachice;

import ua.pz33.StationController;
import ua.pz33.clients.Client;

import java.awt.*;

public class IsServicedState extends State {
    public IsServicedState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        if(client.wasServiced()){
            var exit = StationController.getInstance().getExit();
            var exitPoint = new Point(exit.getX(), exit.getY());
            client.setGoalPoint(exitPoint);
            client.changeState(new ExitState(client));
        }
    }
}
