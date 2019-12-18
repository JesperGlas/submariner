package main;

import controllers.MovingSpriteController;

import java.util.ArrayList;

public class Player extends MovingSpriteFX {

    private double maxHealth = 1000;
    private double health = maxHealth;
    private Boolean detected = false;

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

    public Boolean getDetected() {
        return detected;
    }

    public void setDetected(Boolean detected) {
        this.detected = detected;
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
