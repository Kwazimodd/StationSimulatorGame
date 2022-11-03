package ua.pz33.timers;

import ua.pz33.GameCanvas;
import ua.pz33.utils.AmazingStopwatch;

import java.math.BigInteger;

public class RenderTimer implements Runnable {

    private static final int TARGET_FPS = 60;

    private final GameCanvas canvas;

    private boolean isPaused = false;

    public RenderTimer(GameCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void run() {

        return;
        /*
        var actualDelay = BigInteger.valueOf((long) (10e9 / TARGET_FPS) + 1);
        var frameStopwatch = new AmazingStopwatch();

        while (true) {
            frameStopwatch.start();
            canvas.repaint();
            frameStopwatch.stop();

            System.out.println("Rendered the frame in " + frameStopwatch.elapsedLong() + " nanos");

            var toSleep = actualDelay.min(frameStopwatch.elapsed());
            var toSleepMillis = toSleep.longValue() / 1_000_000;
            var toSleepNanos = (int) (toSleep.longValue() % 1_000_000);

            try {
                Thread.sleep(toSleepMillis, toSleepNanos);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }*/
    }
}
