package ua.pz33.clients;

import ua.pz33.Station;
import ua.pz33.cashregisters.CashRegister;
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

        var firstCashRegister = sortedCashRegisters.get(0);
        var count = firstCashRegister.getClientsQueue().size();
        var allHaveSameCount = sortedCashRegisters.stream().allMatch(c -> c.getClientsQueue().size() == count);

        if (allHaveSameCount) {
            //todo choose the closest
            Station.getInstance().gets()
            //firstCashRegister = closest
        }

        if (firstCashRegister.tryAddToQueue(this)) {
            var cashRegisterSprite = Station.getInstance().getCashRegisterSprite(firstCashRegister.getId());
            goalPoint = new Point(cashRegisterSprite.getX(), cashRegisterSprite.getY());
            return true;
        } else {
            return false;
        }
    }

    public void changeState(State state) {
        currentState = state;
    }

    public State getCurrentState() {
        return currentState;
    }

    public static Comparator<CashRegister> countInQueueComparator = (c1, c2) -> (int) (c1.getClientsQueue().size() - c2.getClientsQueue().size());

    public Integer getId() {
        return id;
    }

    public Integer getCountOfTickets() {
        return countOfTickets;
    }

    public ClientStatus getStatus() {
        return status;
    }

    @Override
    public void onTick() {

    }

    public Point getGoalPoint() {
        return goalPoint;
    }
}

