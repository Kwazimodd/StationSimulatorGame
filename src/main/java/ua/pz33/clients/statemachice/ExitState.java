package ua.pz33.clients.statemachice;

import ua.pz33.controllers.StationController;
import ua.pz33.clients.Client;

public class ExitState extends State{
    private boolean executedAnim = false;
    public ExitState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        var clientSprite = StationController.getInstance().getClientSprite(client.getId());
        var goalPoint = client.getGoalPoint();

        if(!executedAnim){
            clientSprite.moveTo(goalPoint.x, goalPoint.y);
            executedAnim = true;
        }

        //we arrived exit
        if (clientSprite.getX() == goalPoint.x && clientSprite.getY() == goalPoint.y) {
            StationController.getInstance().removeClient(client);
        }
    }
}
