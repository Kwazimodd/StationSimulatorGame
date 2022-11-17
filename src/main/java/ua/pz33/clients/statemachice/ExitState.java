package ua.pz33.clients.statemachice;

import ua.pz33.StationController;
import ua.pz33.clients.Client;

public class ExitState extends State{
    public ExitState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        var clientSprite = StationController.getInstance().getClientSprite(client.getId());
        var goalPoint = client.getGoalPoint();

        //we arrived exit
        if (clientSprite.getX() == goalPoint.x && clientSprite.getY() == goalPoint.y) {
            StationController.getInstance().removeClient(client);
        }
    }
}
