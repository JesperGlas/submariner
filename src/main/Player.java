package main;

import java.util.ArrayList;

public class Player extends MovingSpriteFX {

    private ArrayList<MovingSpriteFX> torpedoes = new ArrayList<MovingSpriteFX>();
    private double maxHealth = 1000;
    private double health = maxHealth;

    public Player(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void modifyHealth(double value) {
        double modifiedHealth = getHealth() + value;
        if (value < 0) {
            setHealth(Math.max(modifiedHealth, 0));
        } else {
            setHealth(Math.min(modifiedHealth, getMaxHealth()));
        }
    }
}
