package ua.pz33;

import ua.pz33.utils.logs.LogDestination;

import javax.swing.*;
import java.awt.*;

public class LogsPanel extends JPanel implements LogDestination {
    private JTextArea textArea;

    public LogsPanel() {
        super(true);

        initializeContent();
    }

    private void initializeContent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 600));

        JLabel logsLabel = new JLabel("Logs");
        logsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logsLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(logsLabel);

        textArea = new JTextArea();
        textArea.setFont(textArea.getFont().deriveFont(10f));
        textArea.setEditable(false);

        JScrollPane logsScroller = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        logsScroller.setBorder(BorderFactory.createEmptyBorder(0, 12, 32, 12));
        add(logsScroller);
    }

    @Override
    public void logMessage(String message) {
        textArea.append(System.currentTimeMillis() + " ms : " + message + "\r\n");
    }
}
