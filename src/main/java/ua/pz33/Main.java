package ua.pz33;

import ua.pz33.generators.ClientGenerator;
import ua.pz33.rendering.RenderTimer;
import ua.pz33.rendering.SpriteRegistry;
import ua.pz33.rendering.animation.AnimationController;
import ua.pz33.rendering.animation.IntAnimation;
import ua.pz33.rendering.animation.Storyboard;
import ua.pz33.rendering.animation.interpolation.Interpolators;
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
        EventQueue.invokeLater(Main::initializeSprites);
        EventQueue.invokeLater(Main::initClient);

        Exit exit = new Exit();
        exit.setBounds(new Rectangle(260, 470, 80, 80));
        StationController.getInstance().addExit(exit);
        GameClock.getInstance().addObserver(ClientGenerator.getInstance());
        GameClock.getInstance().startTimer();

        LogMediator.getInstance().logMessage("Game started");
    }

    private static void initClient(){
        var spriteRegistry = SpriteRegistry.getInstance();

        var regularDude = new ClientSprite("BodyExempt200X200.png", 15);
        regularDude.setBounds(new Rectangle(300, 300, 90, 90));
        regularDude.moveTo(0, 0);

        var vipDude = new ClientSprite("BodyVIP200X200.png", 15);
        vipDude.setBounds(new Rectangle(55, 45, 90, 90));
        vipDude.moveTo(300, 300);

        var cashRegister = new CashRegisterSprite("CashRegister200X200.png", 15);
        cashRegister.setBounds(new Rectangle(0, 100, 90, 90));

        spriteRegistry.registerSprite(regularDude);
        spriteRegistry.registerSprite(vipDude);
        spriteRegistry.registerSprite(cashRegister);
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

        var anim = new Storyboard.Builder()
                .withDuration(800)
                .withInterpolator(Interpolators.SIN_PI_X_HALF)
                .withAnimations(
                        new IntAnimation.Builder()
                                .withBounds(155, 255)
                                .withProperty(Sprite::setX)
                                .build(),
                        new IntAnimation.Builder()
                                .withBounds(145, 245)
                                .withProperty(Sprite::setY)
                                .build())
                .build();


        AnimationController.getInstance().beginAnimation(greenCircle, anim);

        spriteRegistry.registerSprite(magentaCircle);
        spriteRegistry.registerSprite(orangeCircle);
        spriteRegistry.registerSprite(pinkCircle);
        spriteRegistry.registerSprite(blueCircle);
        spriteRegistry.registerSprite(greenCircle);
        spriteRegistry.registerSprite(dumbDude);
    }
}