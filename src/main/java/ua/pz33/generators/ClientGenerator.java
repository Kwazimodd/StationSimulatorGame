package ua.pz33.generators;

import ua.pz33.utils.clock.ClockObserver;
import ua.pz33.utils.configuration.ConfigurationMediator;
import ua.pz33.utils.configuration.PropertyChangedEventArgs;
import ua.pz33.utils.configuration.PropertyRegistry;
import ua.pz33.utils.logs.LogMediator;

import java.awt.*;
import java.util.Random;


public class ClientGenerator implements ClockObserver {
    private final Point position;
    private final Random random = new Random(System.currentTimeMillis());
    private boolean isPaused = false;
    private final int maxAmountOfClients;
    public int clientCount = 0;
    private int ticksPerClient;

    private static ClientGenerator instance;

    public static ClientGenerator getInstance() {
        if (instance == null) {
            instance = new ClientGenerator();
        }

        return instance;
    }

    private ClientGenerator() {
        position = config().getValueOrDefault(PropertyRegistry.ENTRANCE_POSITION, new Point(0, 0));
        maxAmountOfClients = config().getValueOrDefault(PropertyRegistry.MAX_AMOUNT_OF_CLIENTS, 20);
        ticksPerClient = config().getValueOrDefault(PropertyRegistry.TICKS_PER_CLIENT, 20);

        config().addListener(this::configUpdated);
    }

    private static ConfigurationMediator config() {
        return ConfigurationMediator.getInstance();
    }

    public void spawnClient() {
        if (!isPaused) {
            int a = random.nextInt(10);
            if (a < 5) {
                //50%
                //spawn normal client in pos (x-100,y-100)
                LogMediator.getInstance().logMessage("Spawned a normal client");
            } else if (a < 7) {
                //20%
                //spawn exempt client in pos (x-100,y-100)
                LogMediator.getInstance().logMessage("Spawned a client with special needs");
            } else if (a < 9) {
                //20%
                //spawn client with children in pos (x-100,y-100)

                LogMediator.getInstance().logMessage("Spawned a client with children");
            } else {
                //10%
                //spawn VIP client in pos (x-100,y-100)
                LogMediator.getInstance().logMessage("Spawned a VIP client");
            }

            clientCount++;
        }

        if (clientCount >= maxAmountOfClients) {
            isPaused = true;
        }

        if (isPaused && clientCount < maxAmountOfClients / 70) {
            isPaused = false;
        }
    }

    public void updateSpawnRate(int newValue) {
        ticksPerClient = newValue;
    }

    private void configUpdated(PropertyChangedEventArgs args) {
        if (args.getPropertyName().equals(PropertyRegistry.TICKS_PER_CLIENT)) {
            updateSpawnRate((int) args.getNewValue());
        }
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
}
