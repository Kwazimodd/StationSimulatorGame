package ua.pz33;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();

        window.setTitle("bruh");

        var content = new JPanel(true);
        content.add(new JLabel("Hello, world"));
        content.add(new JLabel("loooooooooooooooooooooong"));
        content.setLayout(new FlowLayout());
        window.setContentPane(content);

        window.setVisible(true);
    }
}