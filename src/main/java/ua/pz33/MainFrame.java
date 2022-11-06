package ua.pz33;

import org.jetbrains.annotations.NotNull;
import ua.pz33.timers.RenderTimer;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private GameCanvas canvas;

    public MainFrame() {
        setTitle("Bruh");
        setBounds(100, 100, 1200, 600);
        setContentPane(initializeComponents());
        setResizable(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JPanel initializeComponents() {
        var content = new JPanel(true);
        content.setLayout(new GridBagLayout());

        var logsPanel = new LogsPanel();
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        content.add(logsPanel, c);

        canvas = createGameCanvas();
        content.add(canvas);

        var configPanel = new ConfigPanel();
        c.gridx = 2;
        content.add(configPanel, c);

        return content;
    }

    @NotNull
    private static GameCanvas createGameCanvas() {
        var canvas = new GameCanvas();
        canvas.setBounds(0, 0, 200, 200);
        return canvas;
    }

    public GameCanvas getCanvas() {
        return canvas;
    }
}
