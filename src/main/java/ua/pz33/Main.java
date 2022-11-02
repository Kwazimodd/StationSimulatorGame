package ua.pz33;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setTitle("bruh");

        var content = new JPanel(true);
        content.add(new JLabel("Hello, world"));
        content.add(new JLabel("loooooooooooooooooooooong"));
        content.setLayout(new FlowLayout());
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}