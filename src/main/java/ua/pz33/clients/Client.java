package ua.pz33.clients;

import ua.pz33.Station;
import ua.pz33.cashregisters.CashRegister;
import ua.pz33.sprites.CashRegisterSprite;
import ua.pz33.utils.DistanceCounter;
import ua.pz33.utils.clock.ClockObserver;
import ua.pz33.clients.statemachice.IdleState;
import ua.pz33.clients.statemachice.State;
import ua.pz33.utils.logs.LogMediator;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class Client implements ClockObserver {
    private Integer id;
    private Integer countOfTickets;
    private ClientStatus status;
    private State currentState;
    private Point goalPoint;
    private CashRegister cashRegister;

    public Client(int id, int countOfTickets, ClientStatus status) {
        this.id = id;
        this.countOfTickets = countOfTickets;
        this.status = status;
        this.currentState = new IdleState(this);
    }

    public void buyTickets() {
        var message = String.format("Client %d bought %d tickets", id, countOfTickets);
        countOfTickets = 0;
        LogMediator.getInstance().logMessage(message);
    }

    public boolean tryChooseCashRegister(List<CashRegister> cashRegisters) {
        //check if all cash registers have same number of people in queue
        var sortedCashRegisters = cashRegisters.stream().filter(c -> c.isOpen()).sorted(countInQueueComparator).toList();

        var bestCashRegister = sortedCashRegisters.get(0);
        var count = bestCashRegister.getClientsQueue().size();
        var allHaveSameCount = sortedCashRegisters.stream().allMatch(c -> c.getClientsQueue().size() == count);

        if(allHaveSameCount){
            //todo choose the closest
            var cashRegistersSpriteList =  Station.getInstance().getCashRegisterSprites();
            var closestCashRegisterSprite = cashRegistersSpriteList.stream().min(closestCashRegisterComparator).get();
            bestCashRegister = Station.getInstance().getCashRegister(closestCashRegisterSprite.getId());
        }

        if (bestCashRegister.tryAddToQueue(this)) {
            var cashRegisterSprite = Station.getInstance().getCashRegisterSprite(bestCashRegister.getId());
            goalPoint = new Point(cashRegisterSprite.getX(), cashRegisterSprite.getY());
            cashRegister = bestCashRegister;
            return true;
        }
        else {
            return false;
        }
    }

    public void changeState(State state) {
        currentState = state;
    }

    public State getCurrentState() {
        return currentState;
    }

    private Comparator<CashRegister> countInQueueComparator = Comparator.comparingInt(c -> c.getClientsQueue().size());
    private Comparator<CashRegisterSprite> closestCashRegisterComparator = (c1, c2) ->  {
        var clientSprite = ClientController.getInstance().getClientSprite(id).get();
        int x = clientSprite.getX(), y = clientSprite.getY();
        return Integer.compare(DistanceCounter.getDistance(x, y, c1.getX(), c1.getY()), DistanceCounter.getDistance(x, y, c2.getX(), c2.getY()));
    };

    public Integer getId() {
        return id;
    }

    public Integer getCountOfTickets() {
        return countOfTickets;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public boolean wasServiced(){
        return countOfTickets == 0;
    }

    @Override
    public void onTick() {
        currentState.perform();
    }

    public void setGoalPoint(Point goalPoint) {
        this.goalPoint = goalPoint;
    }

    public Point getGoalPoint() {
        return goalPoint;
    }

    public CashRegister getCashRegister() {
        return cashRegister;
    }

    public void setCashRegister(CashRegister cashRegister) {
        this.cashRegister = cashRegister;
    }
}

