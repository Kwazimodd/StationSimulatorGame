package ua.pz33.sprites;

import java.awt.image.RescaleOp;

public class CashRegisterSprite extends ImageSprite {
    private int id;

    public CashRegisterSprite(String image) {
        super(image);
    }

    public CashRegisterSprite(String image ,int zIndex) {
        super(image, zIndex);
    }

    public CashRegisterSprite(int id, String image) {
        super(image);
        id = id;
    }

    public int getId(){
        return id;
    }

    public void test(){

    }
}
