package ua.pz33.sprites;

import ua.pz33.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.RescaleOp;

public class CashRegisterSprite extends ImageSprite {
    private final int id;

    private final Image openedImage, closedImage;

    public CashRegisterSprite(int id, String openedImage, String closedImage) {
        super(openedImage);

        this.openedImage = super.spriteImage;
        this.closedImage = ResourceLoader.getInstance().loadImage(closedImage);
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setOpened() {
        setImage(openedImage);
    }

    public void setClosed() {
        setImage(closedImage);
    }
}
