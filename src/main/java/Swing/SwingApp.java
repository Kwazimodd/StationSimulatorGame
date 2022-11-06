package Swing;

import javax.swing.*;

public class SwingApp {
    private final JFrame frame;

    public SwingApp() {
        frame = new JFrame("My First GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280,720);

        AddAllControls();
    }

    private void AddAllControls() {

    }
}
