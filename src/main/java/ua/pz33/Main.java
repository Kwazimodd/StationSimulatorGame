package ua.pz33;

import ua.pz33.registries.SpriteRegistry;
import ua.pz33.sprites.DumbCircle;
import ua.pz33.timers.RenderTimer;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setTitle("bruh");
        frame.setBounds(100, 100, 400, 200);

        var canvas = new GameCanvas();
        canvas.setBounds(0, 0, 200, 200);

        var content = new JPanel(true);
        content.add(new JLabel("Hello, world"));
        content.add(canvas);
        content.add(new JLabel("loooooooooooooooooooooong"));
        content.setLayout(new FlowLayout());
        frame.setContentPane(content);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        startRenderTimer(canvas);

        var spriteRegistry = SpriteRegistry.getInstance();
        DumbCircle magentaCircle = new DumbCircle(Color.magenta);
        magentaCircle.setBounds(new Rectangle(50, 50, 50, 50));

        DumbCircle orangeCircle = new DumbCircle(Color.orange);
        orangeCircle.setZIndex(10);
        orangeCircle.setBounds(new Rectangle(85, 75, 20, 20));

        DumbCircle pinkCircle = new DumbCircle(Color.pink);
        pinkCircle.setZIndex(5);
        pinkCircle.setBounds(new Rectangle(75, 75, 90, 90));

        DumbCircle blueCircle = new DumbCircle(Color.blue);
        blueCircle.setZIndex(3);
        blueCircle.setBounds(new Rectangle(55, 75, 90, 90));

        DumbCircle greenCircle = new DumbCircle(Color.green);
        greenCircle.setZIndex(7);
        greenCircle.setBounds(new Rectangle(55, 45, 90, 90));

        spriteRegistry.registerSprite(magentaCircle);
        spriteRegistry.registerSprite(orangeCircle);
        spriteRegistry.registerSprite(pinkCircle);
        spriteRegistry.registerSprite(blueCircle);
        spriteRegistry.registerSprite(greenCircle);
    }

    private static void startRenderTimer(GameCanvas canvas) {
        new RenderTimer(canvas).run();
    }
}