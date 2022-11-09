package ua.pz33.timers;

import ua.pz33.rendering.GameCanvas;
import ua.pz33.rendering.animation.AnimationController;
import ua.pz33.utils.AmazingStopwatch;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RenderTimer {
    private static final boolean PRINT_FRAME_TIMES = false;

    private static final int TARGET_FPS = 60;

    private GameCanvas canvas;
    private final Timer timer;

    private boolean isPaused = false;
    private final AmazingStopwatch stopwatch = new AmazingStopwatch();

    private static RenderTimer instance;

    public static RenderTimer getInstance() {
        if (instance == null) {
            instance = new RenderTimer();
        }

        return instance;
    }

    private RenderTimer() {
        timer = new Timer(1000 / TARGET_FPS, this::onTimerTick);
    }

    private void onTimerTick(ActionEvent event) {
        stopwatch.start();
        AnimationController.getInstance().animationStep();
        canvas.repaint();
        stopwatch.stop();

        if (PRINT_FRAME_TIMES) {
            System.out.println("Rendered frame in " + stopwatch.elapsedLong() + " ns");
        }
    }

    public void setCanvasAndStart(GameCanvas canvas) {
        this.canvas = canvas;

        timer.start();
    }
}
