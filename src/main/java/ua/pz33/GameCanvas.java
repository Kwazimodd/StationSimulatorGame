package ua.pz33;

import ua.pz33.registries.SpriteRegistry;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GameCanvas extends JPanel {

    private final SpriteRegistry registry = SpriteRegistry.getInstance();

    public GameCanvas(){
        super(true);

        this.setIgnoreRepaint(false);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        var g = (Graphics2D)graphics.create();

        g.addRenderingHints(Map.of(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g.clearRect(0, 0, getWidth(), getHeight());

        for (var sprite : registry.getRegisteredSprites()) {
            sprite.paint(g);
        }

        g.dispose();
    }
}
