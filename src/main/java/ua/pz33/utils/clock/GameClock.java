package ua.pz33.utils.clock;

import ua.pz33.utils.configuration.ConfigurationListener;
import ua.pz33.utils.configuration.PropertyChangedEventArgs;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static ua.pz33.utils.configuration.PropertyRegistry.IS_PAUSED;

public class GameClock implements ConfigurationListener {
    private static final int TICKS_PER_SECOND = 10;

    private final List<WeakReference<ClockObserver>> observers = new ArrayList<>();
    private final List<ClockObserver> delayedRunners = new ArrayList<>();
    private final Thread clockThread;

    private static GameClock instance;

    private boolean isPaused;

    public GameClock() {
        clockThread = new Thread(this::clockThreadRun, "Clock-Thread");
    }

    @SuppressWarnings("BusyWait")
    private void clockThreadRun() {
        long currMillis = System.currentTimeMillis() - 100;

        while (true) {
            if (!isPaused) {
                EventQueue.invokeLater(this::onTick);
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
        var postExecuteObserver = new PostExecuteObserver(ticks, function);
        this.delayedRunners.add(postExecuteObserver);
    }

    public static GameClock getInstance() {
        if (instance == null) {
            instance = new GameClock();
        }

        return instance;
    }

    public void startTimer() {
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
                observers.remove(observerWeakReference);

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
    }

    @Override
    public void onPropertyChanged(PropertyChangedEventArgs args) {
        if (args.getPropertyName().equals(IS_PAUSED)) {
            isPaused = args.getNewValue() instanceof Boolean ? ((Boolean) args.getNewValue()) : true;
        }
    }

    class PostExecuteObserver implements ClockObserver {
        private final int tickToRun;
        private final Runnable function;

        private int currentTick = 0;

        public PostExecuteObserver(int ticks, Runnable function) {
            this.tickToRun = ticks;
            this.function = function;
        }

        @Override
        public void onTick() {
            currentTick++;

            if (currentTick == tickToRun) {
                function.run();
                delayedRunners.remove(this);
            }
        }
    }
}
