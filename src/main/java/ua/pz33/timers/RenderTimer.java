package ua.pz33.timers;

import org.jetbrains.annotations.NotNull;
import ua.pz33.GameCanvas;
import ua.pz33.utils.AmazingStopwatch;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RenderTimer implements Runnable {

    private static final int TARGET_FPS = 60;

    private final GameCanvas canvas;
    private final Timer timer;

    private boolean isPaused = false;
    private final AmazingStopwatch stopwatch = new AmazingStopwatch();

    public RenderTimer(GameCanvas canvas) {
        this.canvas = canvas;

        timer = new Timer(1000 / TARGET_FPS, this::onTimerTick);
    }

    private void onTimerTick(ActionEvent event) {
        stopwatch.start();
        canvas.repaint();
        stopwatch.stop();

        System.out.println("Rendered frame in " + stopwatch.elapsedLong() + " ns");
    }

    @Override
    public void run() {
        timer.start();
    }
}
