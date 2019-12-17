package main;

import java.util.ArrayList;

public class Player extends MovingSpriteFX {

    private ArrayList<MovingSpriteFX> torpedoes = new ArrayList<MovingSpriteFX>();
    private double health = 1000;

    public Player(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void modifyHealth(double value) {
        double modifiedHealth = getHealth() + value;
        if (value < 0) {
            setHealth(Math.max(modifiedHealth, 0));
        } else {
            setHealth(modifiedHealth);
        }
    }
}
