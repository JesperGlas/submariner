package main;

import controllers.AnimationController;
import controllers.MovingSpriteController;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Player extends MovingSpriteFX {

    private double maxHealth = 1000;
    private double health = maxHealth;
    private Boolean detected = false;
    private AnimationController launchAnimation;

    public Player(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
        launchAnimation = new AnimationController(startX - width, startY - width, width * 2, height * 2);
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

    public void render(GraphicsContext gc) {
        launchAnimation.render(gc);
        super.drawGraphics(gc);
    }

    public void launch() {
        launchAnimation.add(new AnimatedSpriteFX(this, "/img/animations/launch", 1, 16));
    }

    public void update(double delta) {
        super.transformPos(delta);
        launchAnimation.update();
        launchAnimation.getArray().forEach(animation -> animation.setCenterPos(this.getCenterX(), this.getCenterY()));
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
