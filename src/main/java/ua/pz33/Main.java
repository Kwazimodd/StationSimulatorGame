package ua.pz33;

import ua.pz33.controllers.StationController;
import ua.pz33.generators.ClientGenerator;
import ua.pz33.rendering.RenderTimer;
import ua.pz33.rendering.SpriteRegistry;
import ua.pz33.sprites.*;
import ua.pz33.utils.clock.GameClock;
import ua.pz33.utils.configuration.ConfigurationMediator;
import ua.pz33.utils.logs.LogMediator;

import java.awt.*;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);

        RenderTimer.getInstance().setCanvasAndStart(frame.getCanvas());
        LogMediator.getInstance().addDestination(message -> System.out.println(LocalDateTime.now() + ": " + message));
        LogMediator.getInstance().addDestination(frame.getUiLogDestination());

        ConfigurationMediator.getInstance().addListener(pArgs -> LogMediator.getInstance().logMessage("Property " + pArgs.getPropertyName() + " has changed from '" + pArgs.getOldValue() + "' to '" + pArgs.getNewValue() + "'"));

        // Invoke on Main thread
        //EventQueue.invokeLater(Main::initializeSprites);
        //EventQueue.invokeLater(Main::initClient);
        Entrance entrance = new Entrance();
        entrance.setBounds(new Rectangle(170, 470, 80, 80));
        StationController.getInstance().addEntrance(entrance);
        StationController.getInstance().addCashRegister(100, 100);
        StationController.getInstance().addBackupCashRegister(300, 100);


        Exit exit = new Exit();
        exit.setBounds(new Rectangle(260, 470, 80, 80));
        StationController.getInstance().addExit(exit);
        GameClock.getInstance().addObserver(ClientGenerator.getInstance());
        GameClock.getInstance().startTimer();

        LogMediator.getInstance().logMessage("Game started");
    }

    private static void initializeSprites() {
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

        ImageSprite dumbDude = new ImageSprite("BodyVIP200X200.png", 15);
        dumbDude.setBounds(new Rectangle(55, 45, 50, 50));

        /*var anim = new Storyboard.Builder()
                .withDuration(800)
                .withInterpolator(Interpolators.SIN_PI_X_HALF)
                .withAnimations(
                        new PositionAnimation.Builder()
                        .withBounds(new Point(155, 145), new Point(255, 245))
                        .withProperty((s, p) -> s.getBounds().setLocation(p))
                        .build())
                .build();*/


        //AnimationController.getInstance().beginAnimation(greenCircle, anim);

        //spriteRegistry.registerSprite(magentaCircle);
        //spriteRegistry.registerSprite(orangeCircle);
        //spriteRegistry.registerSprite(pinkCircle);
        //spriteRegistry.registerSprite(blueCircle);
        //spriteRegistry.registerSprite(greenCircle);
        spriteRegistry.registerSprite(dumbDude);
    }
}