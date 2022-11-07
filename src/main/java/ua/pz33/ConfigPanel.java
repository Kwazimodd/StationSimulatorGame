package ua.pz33;

import ua.pz33.utils.configuration.ConfigurationMediator;
import ua.pz33.utils.configuration.PropertyRegistry;

import javax.swing.*;
import java.awt.*;

public class ConfigPanel extends JPanel {
    public ConfigPanel() {
        super(true);

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

        add(configLabel);
        add(dasLabel);
        add(ticksPerClientField);
        add(save);
    }

    private ConfigurationMediator configs() {
        return ConfigurationMediator.getInstance();
    }
}
