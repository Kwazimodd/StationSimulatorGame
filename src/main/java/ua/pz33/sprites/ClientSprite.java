package ua.pz33.sprites;

import ua.pz33.clients.Client;
import ua.pz33.clients.ClientStatus;
import ua.pz33.rendering.animation.AnimationController;
import ua.pz33.rendering.animation.IntAnimation;
import ua.pz33.rendering.animation.Storyboard;
import ua.pz33.rendering.animation.interpolation.Interpolators;
import ua.pz33.utils.DistanceCounter;

import java.util.Map;

public class ClientSprite extends ImageSprite {
    //100 per second
    private int speed = 100;
    private int id;
    private static final Map<ClientStatus, String> clientStatusImageMap = Map.of(
            ClientStatus.REGULAR,"Body200X200.png",
            ClientStatus.INVALID,"BodyExempt200X200.png",
            ClientStatus.VIP,"BodyVIP200X200.png",
            ClientStatus.HAS_KIDS,"BodyWithChild200X200.png");

    public ClientSprite(String image) {
        super(image);
    }
    public ClientSprite(String image, int zIndex) {
        super(image, zIndex);
    }
    public ClientSprite(Client client) {
        super(clientStatusImageMap.get(client.getStatus()), 10);
        id = client.getId();
    }
    public int getId(){
        return id;
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
