package ua.pz33.cashregisters;

import ua.pz33.StationController;
import ua.pz33.clients.Client;
import ua.pz33.clients.statemachice.MovingState;
import ua.pz33.clients.statemachice.IsServicedState;
import ua.pz33.utils.ResourceLoader;
import ua.pz33.utils.clock.ClockObserver;
import ua.pz33.utils.clock.GameClock;
import ua.pz33.utils.configuration.ConfigurationListener;
import ua.pz33.utils.configuration.ConfigurationMediator;
import ua.pz33.utils.configuration.PropertyChangedEventArgs;
import ua.pz33.utils.logs.LogMediator;

import java.awt.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

import static ua.pz33.utils.configuration.PropertyRegistry.TICKS_PER_SERVICE;

public class CashRegister implements ClockObserver, ConfigurationListener {
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

        config().addListener(this);
    }

    public boolean tryAddToQueue(Client client) {
        if (currentState.equals(CashRegisterState.Closed)) {
            return false;
        }

        addToQueueInternal(client);

        return true;
    }

    public void service() {
        if (currentState.equals(CashRegisterState.Servicing) || currentState.equals(CashRegisterState.Closed)) {
            return;
        }

        boolean existOpenedCashRegisters = StationController.getInstance().getCashRegisters().stream().anyMatch(CashRegister::isOpen);

        if (clientsQueue.isEmpty()) {
            if(isBackup && existOpenedCashRegisters){
                close();
            }
            return;
        }

        currentState = CashRegisterState.Servicing;
        var currentClient = clientsQueue.peek();
        currentClient.changeState(new IsServicedState(currentClient));

        var message = String.format("Cash register %d started servicing client %d.", id, currentClient.getId());
        LogMediator.getInstance().logMessage(message);
        GameClock.getInstance().postExecute(currentClient.getCountOfTickets() * ticksToServeClient, () -> {
            clientsQueue.remove(currentClient);

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
        var c1StatusCompareResultState = c1.getCurrentState() instanceof IsServicedState;
        if (c1StatusCompareResultState)
            return -1;

        var c2StatusCompareResultState = c2.getCurrentState() instanceof IsServicedState;
        if (c2StatusCompareResultState)
            return 1;

        var statusCompareResult = c1.getStatus().compareTo(c2.getStatus());
        if (statusCompareResult == 0) {
            return c1.getId().compareTo(c2.getId());
        }

        return statusCompareResult;
    };

    public void open() {
        currentState = CashRegisterState.Open;
        if (!isBackup) {
            StationController.getInstance().getCashRegisterSprite(id).setSprite(ResourceLoader.getInstance().loadImage("CashRegister200X200.png"));
        }
    }

    public void close() {
        currentState = CashRegisterState.Closed;
        if (!isBackup) {
            StationController.getInstance().getCashRegisterSprite(id).setSprite(ResourceLoader.getInstance().loadImage("CashRegisterBroken200X200.png"));
        }

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

    public boolean isBackup(){
        return isBackup;
    }
    @Override
    public void onTick() {
        //random close
        if(!triedClose && isOpen() && !isBackup){
            var res = random.nextInt(10);

            if (res < 1) {
                LogMediator.getInstance().logMessage("Cashregiser " + id + " was broken.");
                close();
                GameClock.getInstance().postExecute(50, this::open);
            }else {
                triedClose = true;
                GameClock.getInstance().postExecute(100, () -> triedClose = false);
            }
        }


        service();
    }

    private void addToQueueInternal(Client client) {
        clientsQueue.add(client);
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

    @Override
    public void onPropertyChanged(PropertyChangedEventArgs args) {
        if (args.getPropertyName().equals(TICKS_PER_SERVICE)) {
            updateServiceTime((int) args.getNewValue());
        }
    }
}