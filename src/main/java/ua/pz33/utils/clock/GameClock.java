package ua.pz33.utils.clock;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GameClock {
    private static final int TICKS_PER_SECOND = 10;

    private final List<WeakReference<ClockObserver>> observers = new ArrayList<>();
    private final Thread clockThread;

    private static GameClock instance;

    public GameClock() {
        clockThread = new Thread(this::clockThreadRun, "Clock-Thread");
    }

    private void clockThreadRun() {
        long currMillis = System.currentTimeMillis() - 100;

        while (true) {
            EventQueue.invokeLater(this::onTick);

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
    public void postExecute(int ticks, Runnable function){
        var postExecuteObserver = new PostExecuteObserver(ticks, function);
        addObserver(postExecuteObserver);
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

            observer.onTick();
        }
    }

    class PostExecuteObserver implements  ClockObserver{
        private int tickToRun;
        private int currentTick = 0;
        private Runnable function;
        public PostExecuteObserver(int ticks, Runnable function){
            this.tickToRun = ticks;
            this.function = function;
        }
        @Override
        public void onTick() {
            currentTick++;

            if(currentTick == tickToRun){
                function.run();
                observers.remove(this);
            }
        }
    }
}
