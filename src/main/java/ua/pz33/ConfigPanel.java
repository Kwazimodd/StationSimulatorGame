package ua.pz33;

import ua.pz33.utils.configuration.ConfigurationMediator;

import javax.swing.*;
import java.awt.*;

import static ua.pz33.utils.configuration.PropertyRegistry.DUMB_ASS_SHIT_PROPERTY;

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

        JLabel dasLabel = new JLabel("das property");
        dasLabel.setAlignmentX(LEFT_ALIGNMENT);


        JTextField bruuh = new JTextField("bruuh");
        bruuh.setAlignmentX(LEFT_ALIGNMENT);
        bruuh.setMaximumSize(new Dimension(Integer.MAX_VALUE, bruuh.getPreferredSize().height));

        var save = new JButton("Save");
        save.setAlignmentX(LEFT_ALIGNMENT);
        save.addActionListener(e -> configs().setValue(DUMB_ASS_SHIT_PROPERTY, bruuh.getText()));

        add(configLabel);
        add(dasLabel);
        add(bruuh);
        add(save);
    }

    private ConfigurationMediator configs() {
        return ConfigurationMediator.getInstance();
    }
}
