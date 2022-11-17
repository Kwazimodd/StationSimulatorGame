package ua.pz33.clients.statemachice;

import ua.pz33.StationController;
import ua.pz33.clients.Client;
import ua.pz33.sprites.Exit;

import java.awt.*;

public class MovingState extends State {
    public MovingState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        var clientSprite = StationController.getInstance().getClientSprite(client.getId()).get();
        var goalPoint = client.getGoalPoint();

        if (client.wasServiced()) {
            var exit = StationController.getInstance().getExit();
            var exitPoint = new Point(exit.getX(), exit.getY());
            client.setGoalPoint(exitPoint);
            clientSprite.moveTo(exitPoint.x, exitPoint.y);
            client.changeState(new ExitState(client));
            return;
        }

        if (!client.getCashRegister().isOpen()) {
            client.changeState(new IdleState(client));
            return;
        }

        //todo to test if position of client sprite is position of service, cashregister.service() can be executed
        if (clientSprite.getX() == goalPoint.x && clientSprite.getY() == goalPoint.y) {
            //client.changeState(new ServicedState(client));
            return;
        }

        clientSprite.moveTo(goalPoint.x, goalPoint.y);
    }
}
