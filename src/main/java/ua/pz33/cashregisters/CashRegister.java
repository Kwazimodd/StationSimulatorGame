package ua.pz33.cashregisters;

import ua.pz33.StationController;
import ua.pz33.clients.Client;
import ua.pz33.clients.statemachice.MovingState;
import ua.pz33.clients.statemachice.ServicedState;
import ua.pz33.utils.clock.ClockObserver;
import ua.pz33.utils.clock.GameClock;
import ua.pz33.utils.configuration.ConfigurationMediator;
import ua.pz33.utils.configuration.PropertyChangedEventArgs;
import ua.pz33.utils.logs.LogMediator;

import java.awt.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

import static ua.pz33.utils.configuration.PropertyRegistry.TICKS_PER_SERVICE;

public class CashRegister implements ClockObserver {
    private static int CashRegisterId = 1;
    private final PriorityQueue<Client> clientsQueue = new PriorityQueue<>(statusComparator);
    private int ticksToServeClient;
    private boolean isBackup = false;
    private final int id;

    private boolean triedClose = false;

    private final Random random = new Random(System.currentTimeMillis());


    private CashRegisterState currentState;

    public CashRegister() {
        currentState = CashRegisterState.Open;
        id = CashRegisterId++;
        ticksToServeClient = ConfigurationMediator.getInstance().getValueOrDefault(TICKS_PER_SERVICE, 40);

        config().addListener(this::configUpdated);
    }

    public boolean tryAddToQueue(Client client) {
        if (currentState.equals(CashRegisterState.Closed)) {
            return false;
        }

        addToQueueInternal(client);

        return true;
    }

    public void service() {
        // todo add time for client service from configuration
        if (currentState.equals(CashRegisterState.Servicing) || currentState.equals(CashRegisterState.Closed)) {
            return;
        }

        boolean cashRegistersHasOpen = StationController.getInstance().getCashRegisters().stream().anyMatch(c->c.isOpen());

        if (clientsQueue.isEmpty()) {
            if(isBackup && cashRegistersHasOpen){
                close();
            }
            return;
        }

        currentState = CashRegisterState.Servicing;
        GameClock.getInstance().postExecute(ticksToServeClient, () -> {
            var currentClient = clientsQueue.poll();

            if (currentClient == null) {
                throw new IllegalArgumentException("The queue can't be empty");
            }

            currentClient.buyTickets(this);
            currentState = CashRegisterState.Open;
            notifyControllerAboutQueueUpdate();
        });

    }

    public PriorityQueue<Client> getClientsQueue() {
        return clientsQueue;
    }

    public static Comparator<Client> statusComparator = (c1, c2) -> {
        var statusCompareResultState = c1.getCurrentState() instanceof ServicedState;
        if (statusCompareResultState)
            return 1;

        var statusCompareResult = c1.getStatus().compareTo(c2.getStatus());
        if (statusCompareResult == 0) {
            return c1.getId().compareTo(c2.getId());
        }

        return statusCompareResult;
    };

    public void open() {
        currentState = CashRegisterState.Open;
    }

    public void close() {
        currentState = CashRegisterState.Closed;

        // TODO: please check
        if(!isBackup){
            StationController.getInstance().moveQueue(clientsQueue);
        }
    }

    public boolean isOpen() {
        return !currentState.equals(CashRegisterState.Closed);
    }

    public int getTicksToServeClient() {
        return ticksToServeClient;
    }

    public void setTicksToServeClient(int ticksToServeClient) {
        this.ticksToServeClient = ticksToServeClient;
    }

    public void makeBackup() {
        isBackup = true;
        close();
    }

    public int getId() {
        return id;
    }

    @Override
    public void onTick() {
        //random close
        if(!triedClose && isOpen() && !isBackup){
            var res = random.nextInt(10);

            if (res < 7) {
                LogMediator.getInstance().logMessage("Cashregiser " + id + " was broken.");
                close();
                GameClock.getInstance().postExecute(50, () -> open());
            }else {
                triedClose = true;
                GameClock.getInstance().postExecute(100, () -> triedClose = false);
            }
        }


        service();
    }

    private void addToQueueInternal(Client client) {
        clientsQueue.add(client);
        var cashRegisterSprite = StationController.getInstance().getCashRegisterSprite(this.getId());
        client.setGoalPoint(new Point(cashRegisterSprite.getX(), cashRegisterSprite.getY()));
        client.setCashRegister(this);
        client.changeState(new MovingState(client));

        notifyControllerAboutQueueUpdate();
    }

    private void notifyControllerAboutQueueUpdate() {
        StationController.getInstance().onQueueUpdated(this, clientsQueue);
    }

    private static ConfigurationMediator config() {
        return ConfigurationMediator.getInstance();
    }

    public void updateServiceTime(int newValue) {
        ticksToServeClient = newValue;
    }

    private void configUpdated(PropertyChangedEventArgs args) {
        if (args.getPropertyName().equals(TICKS_PER_SERVICE)) {
            updateServiceTime((int) args.getNewValue());
        }
    }
}