package ua.pz33;

import ua.pz33.utils.configuration.ConfigurationMediator;
import ua.pz33.utils.configuration.PropertyRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConfigPanel extends JPanel {

    private GameCanvas canvas;
    public ConfigPanel() {
        super(true);

        initializeContent();
    }

    public ConfigPanel(GameCanvas canvas)
    {
        super(true);
        this.canvas = canvas;
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
        save.addActionListener(e -> configs().setValue(PropertyRegistry.TICKS_PER_CLIENT, Integer.parseInt(ticksPerClientField.getText())));

        //Another configuration fields
        //Time of service
        JTextField serviceTimeField = new JTextField("10");
        serviceTimeField.setAlignmentX(LEFT_ALIGNMENT);
        serviceTimeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, ticksPerClientField.getPreferredSize().height));
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
        addCashRegisterButton.addActionListener(e->canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                ConfigurationMediator.getInstance().setValue("lastMouseClickPosition", MouseInfo.getPointerInfo().getLocation());

                //console log
                System.out.println("Put Cash Register at position: " + ConfigurationMediator.getInstance().getValueOrDefault("lastMouseClickPosition",null).toString());
                canvas.removeMouseListener(this);
            }
        }));
        var addReserveCashRegisterButton = new JButton("Reserve CR");
        addReserveCashRegisterButton.setAlignmentX(LEFT_ALIGNMENT);
        addReserveCashRegisterButton.addActionListener(e->canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                ConfigurationMediator.getInstance().setValue("lastMouseClickPosition", MouseInfo.getPointerInfo().getLocation());

                //console log
                System.out.println("Put Reserved Cash Register at position: " + ConfigurationMediator.getInstance().getValueOrDefault("lastMouseClickPosition",null).toString());
                canvas.removeMouseListener(this);
            }
        }));
        var addEntranceButton = new JButton("Entrance");
        addEntranceButton.setAlignmentX(LEFT_ALIGNMENT);
        addEntranceButton.addActionListener(e->canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                ConfigurationMediator.getInstance().setValue("lastMouseClickPosition", MouseInfo.getPointerInfo().getLocation());

                //console log
                System.out.println("Put Entrance at position: " + ConfigurationMediator.getInstance().getValueOrDefault("lastMouseClickPosition",null).toString());
                canvas.removeMouseListener(this);
            }
        }));


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
        add(save);
        //spawning models components
//        add(new JLabel("Enter spawning cords:"));
//        add(new JLabel("x:"));
//        add(xCoordinateField);
//        add(new JLabel("y:"));
//        add(yCoordinateField);
        add(new JLabel("Select creating model:"));
        add(addEntranceButton);
        add(addCashRegisterButton);
        add(addReserveCashRegisterButton);

    }

    private ConfigurationMediator configs() {
        return ConfigurationMediator.getInstance();
    }
}
