package ua.pz33.generators;

import ua.pz33.StationController;
import ua.pz33.clients.Client;
import ua.pz33.clients.ClientStatus;
import ua.pz33.sprites.Entrance;
import ua.pz33.utils.clock.ClockObserver;
import ua.pz33.utils.configuration.ConfigurationListener;
import ua.pz33.utils.configuration.ConfigurationMediator;
import ua.pz33.utils.configuration.PropertyChangedEventArgs;
import ua.pz33.utils.configuration.PropertyRegistry;
import ua.pz33.utils.logs.LogMediator;

import java.util.List;
import java.util.Random;

import static ua.pz33.utils.configuration.PropertyRegistry.MAX_AMOUNT_OF_CLIENTS;
import static ua.pz33.utils.configuration.PropertyRegistry.TICKS_PER_CLIENT;


public class ClientGenerator implements ClockObserver, ConfigurationListener {
    private final Random random = new Random(System.currentTimeMillis());
    private boolean isPaused = false;
    private int maxAmountOfClients;
    public int clientCount = 0;
    private int ticksPerClient;
    private static int ClientId = 1;

    private final StationController stationController = StationController.getInstance();

    private static ClientGenerator instance;

    public static ClientGenerator getInstance() {
        if (instance == null) {
            instance = new ClientGenerator();
        }

        return instance;
    }

    private ClientGenerator() {
        maxAmountOfClients = config().getValueOrDefault(MAX_AMOUNT_OF_CLIENTS, 20);
        ticksPerClient = config().getValueOrDefault(TICKS_PER_CLIENT, 10);

        config().addListener(this);
    }

    private static ConfigurationMediator config() {
        return ConfigurationMediator.getInstance();
    }

    public void spawnClient() {
        if (!isPaused) {
            int a = random.nextInt(10);
            int ticketsCount = random.nextInt(9) + 1;
            List<Entrance> entrances = StationController.getInstance().getEntrances();
            Entrance randomEntrance = entrances.get(random.nextInt(entrances.size()));
            if (a < 5) {
                //50%
                //spawn normal client in pos (x-100,y-100)
                stationController.addClient(new Client(ClientId++, ticketsCount, ClientStatus.REGULAR), randomEntrance.getBounds().getLocation());
                LogMediator.getInstance().logMessage("Spawned a normal client");
            } else if (a < 7) {
                //20%
                //spawn invalid client in pos (x-100,y-100)
                stationController.addClient(new Client(ClientId++, ticketsCount, ClientStatus.INVALID), randomEntrance.getBounds().getLocation());
                LogMediator.getInstance().logMessage("Spawned a client with special needs");
            } else if (a < 9) {
                //20%
                //spawn client with children in pos (x-100,y-100)
                stationController.addClient(new Client(ClientId++, ticketsCount, ClientStatus.HAS_KIDS), randomEntrance.getBounds().getLocation());
                LogMediator.getInstance().logMessage("Spawned a client with children");
            } else {
                //10%
                //spawn VIP client in pos (x-100,y-100)
                stationController.addClient(new Client(ClientId++, ticketsCount, ClientStatus.VIP), randomEntrance.getBounds().getLocation());
                LogMediator.getInstance().logMessage("Spawned a VIP client");
            }
            clientCount++;
        }

        if (clientCount >= maxAmountOfClients) {
            isPaused = true;
        }

        if (isPaused && clientCount < maxAmountOfClients * 0.7) {
            isPaused = false;
        }
    }

    public void updateMaxAmountOfClients(int newValue) {
        maxAmountOfClients = newValue;
    }

    public void updateSpawnRate(int newValue) {
        ticksPerClient = newValue;
    }

    private int tickCount = 0;

    @Override
    public void onTick() {
        tickCount++;

        tickCount %= ticksPerClient;

        if (tickCount == 0) {
            spawnClient();
        }
    }

    @Override
    public void onPropertyChanged(PropertyChangedEventArgs args) {
        if (args.getPropertyName().equals(TICKS_PER_CLIENT)) {
            updateSpawnRate((int) args.getNewValue());
        } else if (args.getPropertyName().equals(MAX_AMOUNT_OF_CLIENTS)) {
            updateMaxAmountOfClients((int) args.getNewValue());
        }
    }
}
