package ua.pz33.rendering;

import ua.pz33.rendering.animation.AnimationController;
import ua.pz33.utils.AmazingStopwatch;

import java.awt.*;

public class RenderTimer {
    private static final boolean PRINT_FRAME_TIMES = false;

    private static final int TARGET_FPS = 60;

    private GameCanvas canvas;

    private boolean isPaused = false;
    private final AmazingStopwatch stopwatch = new AmazingStopwatch();
    private final Thread renderThread;

    private static RenderTimer instance;

    public static RenderTimer getInstance() {
        if (instance == null) {
            instance = new RenderTimer();
        }

        return instance;
    }

    private RenderTimer() {
        renderThread = new Thread(this::renderThreadRun, "Render-Thread");
    }

    private void renderThreadRun() {
        final long frameTime = 1000 / TARGET_FPS;

        long currMillis = System.currentTimeMillis() - frameTime;

        while (true) {
            onNextFrame();

            var delta = System.currentTimeMillis() - currMillis;
            var toSleep = Math.max(2 * frameTime - delta, 0);

            currMillis = System.currentTimeMillis();

            try {
                Thread.sleep(toSleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void onNextFrame() {
        stopwatch.start();
        AnimationController.getInstance().animationStep();

        EventQueue.invokeLater(() -> canvas.repaint());
        stopwatch.stop();

        if (PRINT_FRAME_TIMES) {
            System.out.println("Rendered frame in " + stopwatch.elapsedLong() + " ns");
        }
    }

    public void setCanvasAndStart(GameCanvas canvas) {
        this.canvas = canvas;

        renderThread.start();
    }
}
