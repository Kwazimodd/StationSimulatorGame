package ua.pz33;

import ua.pz33.rendering.GameCanvas;
import ua.pz33.utils.configuration.ConfigurationListener;
import ua.pz33.utils.configuration.ConfigurationMediator;
import ua.pz33.utils.configuration.PropertyChangedEventArgs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static ua.pz33.utils.configuration.PropertyRegistry.*;

public class ConfigPanel extends JPanel implements ConfigurationListener {

    private final GameCanvas canvas;
    private JButton startButton;
    private JButton pauseButton;

    public ConfigPanel(GameCanvas canvas) {
        super(true);
        this.canvas = canvas;
        configs().addListener(this);
        initializeContent();
    }

    private void initializeContent() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel configLabel = new JLabel("Configs");
        configLabel.setAlignmentX(LEFT_ALIGNMENT);
        configLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 12));

        JLabel dasLabel = new JLabel("Tick per client spawn");
        dasLabel.setAlignmentX(LEFT_ALIGNMENT);

        JTextField ticksPerClientField = new JTextField("20");
        ticksPerClientField.setAlignmentX(LEFT_ALIGNMENT);
        ticksPerClientField.setMaximumSize(new Dimension(Integer.MAX_VALUE, ticksPerClientField.getPreferredSize().height));

        var save = new JButton("Save");
        save.setAlignmentX(LEFT_ALIGNMENT);
        save.addActionListener(e -> configs().setValue(TICKS_PER_CLIENT, Integer.parseInt(ticksPerClientField.getText())));

        //Another configuration fields
        //Time of service
        JTextField serviceTimeField = new JTextField("10");
        serviceTimeField.setAlignmentX(LEFT_ALIGNMENT);
        serviceTimeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, ticksPerClientField.getPreferredSize().height));
        save.addActionListener(e -> configs().setValue(TICKS_PER_SERVICE, Integer.parseInt(serviceTimeField.getText())));

        //Expected amount of people
        JTextField expectedPeopleField = new JTextField("6");
        expectedPeopleField.setAlignmentX(LEFT_ALIGNMENT);
        expectedPeopleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, ticksPerClientField.getPreferredSize().height));
        //% of people in special group
        JTextField percentSpecialField = new JTextField("5");
        percentSpecialField.setAlignmentX(LEFT_ALIGNMENT);
        percentSpecialField.setMaximumSize(new Dimension(Integer.MAX_VALUE, ticksPerClientField.getPreferredSize().height));


        JTextField xCoordinateField = new JTextField("");
        xCoordinateField.setAlignmentX(LEFT_ALIGNMENT);
        xCoordinateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, ticksPerClientField.getPreferredSize().height));

        JTextField yCoordinateField = new JTextField("");
        yCoordinateField.setAlignmentX(LEFT_ALIGNMENT);
        yCoordinateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, ticksPerClientField.getPreferredSize().height));

        var addCashRegisterButton = new JButton("Cash Register");
        addCashRegisterButton.setAlignmentX(LEFT_ALIGNMENT);
        addCashRegisterButton.addActionListener(e -> canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                configs().setValue(LAST_MOUSE_CLICK_POSITION, new Point(e.getX(), e.getY()));

                //console log
                System.out.println("Put Cash Register at position: " + configs().getValueOrDefault(LAST_MOUSE_CLICK_POSITION, null).toString());
                canvas.removeMouseListener(this);
            }
        }));

        var addReserveCashRegisterButton = new JButton("Reserve CR");
        addReserveCashRegisterButton.setAlignmentX(LEFT_ALIGNMENT);
        addReserveCashRegisterButton.addActionListener(e -> canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                configs().setValue(LAST_MOUSE_CLICK_POSITION, new Point(e.getX(), e.getY()));
                //console log
                System.out.println("Put Reserved Cash Register at position: " + configs().getValueOrDefault(LAST_MOUSE_CLICK_POSITION, null).toString());
                canvas.removeMouseListener(this);
            }
        }));

        var addEntranceButton = new JButton("Entrance");
        addEntranceButton.setAlignmentX(LEFT_ALIGNMENT);
        addEntranceButton.addActionListener(e -> canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                configs().setValue(LAST_MOUSE_CLICK_POSITION, new Point(e.getX(), e.getY()));
                //console log
                System.out.println("Put Entrance at position: " + configs().getValueOrDefault(LAST_MOUSE_CLICK_POSITION, null).toString());
                canvas.removeMouseListener(this);
            }
        }));

        var addItemsPanel = new JPanel();
        addItemsPanel.setAlignmentX(LEFT_ALIGNMENT);
        addItemsPanel.setLayout(new BoxLayout(addItemsPanel, BoxLayout.X_AXIS));

        addItemsPanel.add(addEntranceButton);
        addItemsPanel.add(addCashRegisterButton);
        addItemsPanel.add(addReserveCashRegisterButton);

        startButton = new JButton("Start");
        startButton.addActionListener(e -> configs().setValue(IS_PAUSED, false));

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> configs().setValue(IS_PAUSED, true));

        var startStopPanel = new JPanel();
        startStopPanel.setAlignmentX(LEFT_ALIGNMENT);
        startStopPanel.setLayout(new BoxLayout(startStopPanel, BoxLayout.X_AXIS));

        startStopPanel.add(startButton);
        startStopPanel.add(pauseButton);

        //configuration models
        add(configLabel);
        add(dasLabel);
        add(ticksPerClientField);
        //another configuration components

        add(new JLabel("Time of service for single CR"));
        add(serviceTimeField);
        add(new JLabel("Expected amount of people for single CR"));
        add(expectedPeopleField);
        add(new JLabel("Percent of special group peoples"));
        add(percentSpecialField);
        add(addItemsPanel);
        add(save);

        add(new JLabel("Select creating model:"));
        add(startStopPanel);
    }

    private ConfigurationMediator configs() {
        return ConfigurationMediator.getInstance();
    }

    @Override
    public void onPropertyChanged(PropertyChangedEventArgs args) {
        if (args.getPropertyName().equals(IS_PAUSED)) {
            var isPaused = args.getNewValue() instanceof Boolean ? ((Boolean) args.getNewValue()) : true;

            startButton.setEnabled(isPaused);
            pauseButton.setEnabled(!isPaused);
        }
    }
}
