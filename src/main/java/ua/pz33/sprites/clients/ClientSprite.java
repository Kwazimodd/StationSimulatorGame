package ua.pz33.sprites.clients;

import ua.pz33.rendering.animation.AnimationController;
import ua.pz33.rendering.animation.IntAnimation;
import ua.pz33.rendering.animation.Storyboard;
import ua.pz33.rendering.animation.interpolation.Interpolators;
import ua.pz33.sprites.ImageSprite;
import ua.pz33.sprites.Sprite;
import ua.pz33.utils.DistanceCounter;

public class ClientSprite extends ImageSprite {
    //100 per second
    private int speed = 100;

    public ClientSprite(String image) {
        super(image);
    }

    public ClientSprite(String image ,int zIndex) {
        super(image, zIndex);
    }

    private int countTimeForMove(int x, int y){
        int distance = DistanceCounter.getDistance(getX(), getY(), x, y);
        return (distance / speed) * 1000;
    }

    public void moveTo(int x, int y){
        int duration = countTimeForMove(x, y);

        var moveAnimation = new Storyboard.Builder()
                .withDuration(duration)
                .withInterpolator(Interpolators.SIN_PI_X_HALF)
                .withAnimations(
                        new IntAnimation.Builder()
                                .withBounds(getX(), x)
                                .withProperty(Sprite::setX)
                                .build(),
                        new IntAnimation.Builder()
                                .withBounds(getY(), y)
                                .withProperty(Sprite::setY)
                                .build())
                .build();

        AnimationController.getInstance().beginAnimation(this, moveAnimation);
    }
}
