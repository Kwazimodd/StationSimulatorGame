package ua.pz33.utils.clock;

import ua.pz33.utils.configuration.ConfigurationListener;
import ua.pz33.utils.configuration.ConfigurationMediator;
import ua.pz33.utils.configuration.PropertyChangedEventArgs;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static ua.pz33.utils.configuration.PropertyRegistry.IS_PAUSED;

public class GameClock implements ConfigurationListener {
    private static final int TICKS_PER_SECOND = 10;

    private final List<WeakReference<ClockObserver>> observers = new ArrayList<>();
    private final List<PostExecuteObserver> delayedRunners = new ArrayList<>();
    private final Thread clockThread;

    private static GameClock instance;

    private boolean isPaused;

    public GameClock() {
        config().addListener(this);

        clockThread = new Thread(this::clockThreadRun, "Clock-Thread");
    }

    @SuppressWarnings("BusyWait")
    private void clockThreadRun() {
        long currMillis = System.currentTimeMillis() - 100;

        while (true) {
            if (!isPaused) {
                this.onTick();
            }

            var delta = System.currentTimeMillis() - currMillis;
            var toSleep = Math.max(2 * 1000 / TICKS_PER_SECOND - delta, 0);

            currMillis = System.currentTimeMillis();

            try {
                Thread.sleep(toSleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void postExecute(int ticks, Runnable function) {
        var postExecuteObserver = new PostExecuteObserverImpl(ticks, function);
        this.delayedRunners.add(postExecuteObserver);
    }

    public static GameClock getInstance() {
        if (instance == null) {
            instance = new GameClock();
        }

        return instance;
    }

    public void startTimer() {
        isPaused = config().getValueOrDefault(IS_PAUSED, true);

        clockThread.start();
    }

    public void addObserver(ClockObserver destination) {
        this.observers.add(new WeakReference<>(destination));
    }

    private void onTick() {
        if (observers.isEmpty()) {
            return;
        }

        for (var observerWeakReference : observers) {
            var observer = observerWeakReference.get();

            if (observer == null) {
                continue;
            }

            try {
                observer.onTick();
            } catch (Exception ignored) {
            }
        }

        for (var runner : delayedRunners) {
            try {
                runner.onTick();
            } catch (Exception ignored) {
            }
        }

        observers.stream()
                .filter(wr -> wr.get() == null)
                .collect(Collectors.toList())
                .forEach(observers::remove);

        delayedRunners.stream()
                .filter(PostExecuteObserver::canBeDeleted)
                .collect(Collectors.toList())
                .forEach(delayedRunners::remove);
    }

    private static ConfigurationMediator config() {
        return ConfigurationMediator.getInstance();
    }

    @Override
    public void onPropertyChanged(PropertyChangedEventArgs args) {
        if (args.getPropertyName().equals(IS_PAUSED)) {
            isPaused = args.getNewValue() instanceof Boolean ? ((Boolean) args.getNewValue()) : true;
        }
    }

    private static class PostExecuteObserverImpl implements PostExecuteObserver {
        private final int tickToRun;
        private final Runnable function;

        private int currentTick = 0;
        private boolean canBeDeleted = false;

        public PostExecuteObserverImpl(int ticks, Runnable function) {
            this.tickToRun = ticks;
            this.function = function;
        }

        @Override
        public void onTick() {
            currentTick++;

            if (currentTick == tickToRun) {
                function.run();

                canBeDeleted = true;
            }
        }

        @Override
        public boolean canBeDeleted() {
            return canBeDeleted;
        }
    }
}
