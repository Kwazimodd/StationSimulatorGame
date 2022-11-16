package ua.pz33;

import ua.pz33.utils.logs.LogDestination;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class LogsPanel extends JPanel implements LogDestination {
    private JTextArea textArea;

    public LogsPanel() {
        super(true);

        initializeContent();
    }

    private void initializeContent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel logsLabel = new JLabel("Logs");
        logsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logsLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(logsLabel);

        textArea = new JTextArea();
        textArea.setFont(textArea.getFont().deriveFont(12f));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane logsScroller = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        logsScroller.setBorder(BorderFactory.createEmptyBorder(0, 12, 32, 12));
        add(logsScroller);
    }

    @Override
    public void logMessage(String message) {
        textArea.append(String.format("%1$TH:%1$TM:%1$TS", System.currentTimeMillis())  + ": " + message + "\r\n\n");
    }
}
