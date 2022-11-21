package ua.pz33.cashregisters;

import ua.pz33.controllers.CashRegisterController;
import ua.pz33.controllers.StationController;
import ua.pz33.clients.Client;
import ua.pz33.clients.statemachice.MovingState;
import ua.pz33.clients.statemachice.IsBeingServicedState;
import ua.pz33.utils.clock.ClockObserver;
import ua.pz33.utils.clock.GameClock;
import ua.pz33.utils.configuration.ConfigurationListener;
import ua.pz33.utils.configuration.ConfigurationMediator;
import ua.pz33.utils.configuration.PropertyChangedEventArgs;
import ua.pz33.utils.logs.LogMediator;

import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;

import static ua.pz33.cashregisters.CashRegisterState.*;
import static ua.pz33.utils.configuration.PropertyRegistry.TICKS_PER_SERVICE;

public class CashRegister implements ClockObserver, ConfigurationListener {
    private static int CashRegisterId = 1;
    private final CashRegisterController controller;
    private final LogMediator logger = LogMediator.getInstance();
    private final PriorityQueue<Client> clientsQueue = new PriorityQueue<>(statusComparator);
    private int ticksToServeClient;
    private boolean isBackup = false;
    private final int id;

    private boolean triedClose = false;

    private final Random random = new Random(System.currentTimeMillis());


    private CashRegisterState currentState;

    public CashRegister() {
        currentState = Open;
        id = CashRegisterId++;
        ticksToServeClient = config().getValueOrDefault(TICKS_PER_SERVICE, 5);

        config().addListener(this);
        controller = StationController.getInstance();
    }

    public boolean tryAddToQueue(Client client) {
        if (currentState.equals(Closed)) {
            return false;
        }

        addToQueueInternal(client);

        controller.notifyQueueUpdated(this);

        return true;
    }

    public boolean tryAddAllToQueue(Iterable<Client> clients) {
        if (currentState.equals(Closed)) {
            return false;
        }

        for (Client client : clients) {
            addToQueueInternal(client);
        }

        controller.notifyQueueUpdated(this);

        return true;
    }

    public void service() {
        if (currentState == Servicing || currentState == Closed) {
            return;
        }

        boolean existOpenedCashRegisters = controller.hasAnyOpenRegisters();

        if (isBackup && existOpenedCashRegisters) {
            close();

            return;
        }

        if (clientsQueue.isEmpty()) {
            return;
        }

        currentState = Servicing;

        var currentClient = clientsQueue.peek();
        controller.notifyClientBeganService(currentClient);

        var message = String.format("Cash register %d started servicing client %d.", id, currentClient.getId());
        LogMediator.getInstance().logMessage(message);
        GameClock.getInstance().postExecute(getServiceDelay(currentClient), () -> {
            if(currentState == Closed) {
                return;
            }

            clientsQueue.poll();

            logClientBoughtTickets(currentClient);
            currentState = Open;

            controller.notifyClientServiced(currentClient);
            controller.notifyQueueUpdated(this);
        });
    }

    private int getServiceDelay(Client currentClient) {
        return (1 + currentClient.getCountOfTickets() / 2) * ticksToServeClient;
    }

    public PriorityQueue<Client> getClientsQueue() {
        return clientsQueue;
    }

    public static Comparator<Client> statusComparator = (c1, c2) -> {
        var c1StatusCompareResultState = c1.getCurrentState() instanceof IsBeingServicedState;
        if (c1StatusCompareResultState)
            return -1;

        var c2StatusCompareResultState = c2.getCurrentState() instanceof IsBeingServicedState;
        if (c2StatusCompareResultState)
            return 1;

        var statusCompareResult = c1.getStatus().compareTo(c2.getStatus());
        if (statusCompareResult == 0) {
            return c1.getId().compareTo(c2.getId());
        }

        return statusCompareResult;
    };

    public void open() {
        currentState = Open;

        controller.notifyCashRegisterOpened(this);
    }

    public void close() {
        currentState = Closed;

        controller.notifyCashRegisterClosed(this);

        clientsQueue.clear();
    }

    public boolean isOpen() {
        return !currentState.equals(Closed);
    }

    public void makeBackup() {
        isBackup = true;
        GameClock.getInstance().postExecute(1, this::close);
    }

    public int getId() {
        return id;
    }

    public boolean isBackup() {
        return isBackup;
    }

    @Override
    public void onTick() {
        //random close
        if (!triedClose && isOpen() && !isBackup) {
            var res = random.nextInt(10);

            if (res < 1) {
                LogMediator.getInstance().logMessage("Cashregiser " + id + " was broken.");
                close();
                GameClock.getInstance().postExecute(50, this::open);

                return;
            } else {
                triedClose = true;
                GameClock.getInstance().postExecute(100, () -> triedClose = false);
            }
        }

        service();
    }

    private void logClientBoughtTickets(Client client) {
        var message = String.format("Client %d bought %d tickets in %d cashregister.", id, client.getCountOfTickets(), this.getId());
        logger.logMessage(message);
    }

    private void addToQueueInternal(Client client) {
        clientsQueue.add(client);
        client.setCashRegister(this);
        client.changeState(new MovingState(client));
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

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}