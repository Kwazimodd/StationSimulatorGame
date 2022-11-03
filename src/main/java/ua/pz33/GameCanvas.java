package ua.pz33;

import java.awt.*;

public class GameCanvas extends Canvas {

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawString("Hello game", 5, 20);
        g.setColor(Color.cyan);
        g.fillArc(0, 50, 50, 50, 0, 90);
    }
}
