package ua.pz33.clients;

import ua.pz33.StationController;
import ua.pz33.cashregisters.CashRegister;
import ua.pz33.sprites.CashRegisterSprite;
import ua.pz33.utils.DistanceCounter;
import ua.pz33.utils.clock.ClockObserver;
import ua.pz33.clients.statemachice.IdleState;
import ua.pz33.clients.statemachice.State;
import ua.pz33.utils.logs.LogMediator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
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

    public void buyTickets(CashRegister cashRegister) {
        var message = String.format("Client %d bought %d tickets in %d cashregister.", id, countOfTickets, cashRegister.getId());
        countOfTickets = 0;
        LogMediator.getInstance().logMessage(message);
    }

    public boolean tryChooseCashRegister(Collection<CashRegister> cashRegisters) {
        //check if all cash registers have same number of people in queue
        var sortedCashRegisters = cashRegisters.stream().filter(c -> c.isOpen()).sorted(countInQueueComparator).toList();

        if(sortedCashRegisters.isEmpty()){
            return false;
        }

        var count = sortedCashRegisters.get(0).getClientsQueue().size();
        var filteredCashRegisters = sortedCashRegisters.stream().filter(c -> c.getClientsQueue().size() == count).toList();

        var isBackup = filteredCashRegisters.get(0).isBackup();

        List<CashRegisterSprite> openedCashRegisterSprites = new ArrayList<>();

        filteredCashRegisters.forEach(c -> openedCashRegisterSprites.add(isBackup
                ? StationController.getInstance().getBackupCashRegisterSprite(c.getId())
                : StationController.getInstance().getCashRegisterSprite(c.getId())));

        var closestCashRegisterSprite = openedCashRegisterSprites.stream().min(closestCashRegisterComparator).get();
        var bestCashRegister = isBackup
                ? StationController.getInstance().getBackupCashRegister(closestCashRegisterSprite.getId())
                : StationController.getInstance().getCashRegister(closestCashRegisterSprite.getId());

        return bestCashRegister.tryAddToQueue(this);
    }

    public void changeState(State state) {
        currentState = state;
    }

    public State getCurrentState() {
        return currentState;
    }

    private final Comparator<CashRegister> countInQueueComparator = Comparator.comparingInt(c -> c.getClientsQueue().size());
    private final Comparator<CashRegisterSprite> closestCashRegisterComparator = (c1, c2) ->  {
        var clientSprite = StationController.getInstance().getClientSprite(id);
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

