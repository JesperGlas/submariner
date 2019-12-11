package main;

import javafx.scene.canvas.GraphicsContext;

public class MovingSpriteFX extends SpriteFX {

    private double xVelocity = 0d;
    private double yVelocity = 0d;
    private double xSpeedModifier = 1d;
    private double ySpeedModifier = 1d;
    private double xVelocityLimit = 1d;
    private double yVelocityLimit = 1d;

    public MovingSpriteFX(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
    }

    public double getVelocityX() {
        return xVelocity;
    }

    public void setVelocityX(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getVelocityY() {
        return yVelocity;
    }

    public void setVelocityY(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public double getSpeedModifierX() {
        return xSpeedModifier;
    }

    public void setxSpeedModifier(double xSpeedModifier) {
        this.xSpeedModifier = xSpeedModifier;
    }

    public double getSpeedModifierY() {
        return ySpeedModifier;
    }

    public void setySpeedModifier(double ySpeedModifier) {
        this.ySpeedModifier = ySpeedModifier;
    }

    public double getVelocityXLimit() {
        return xVelocityLimit;
    }

    public void setxVelocityLimit(double xVelocityLimit) {
        this.xVelocityLimit = xVelocityLimit;
    }

    public double getVelocityYLimit() {
        return yVelocityLimit;
    }

    public void setyVelocityLimit(double yVelocityLimit) {
        this.yVelocityLimit = yVelocityLimit;
    }

    public void transformX() {
        setStartX(getStartX() + getSpeedModifierX() * getVelocityX());
    }

    public void transformY() {
        setStartY(getStartY() + getVelocityY() * getSpeedModifierY());
    }

    public void transformPos() {
        transformX();
        transformY();
    }

    public void transformVelocityX(double value) {
        setVelocityX(transformVelocity(value, getVelocityX(), getVelocityXLimit()));
    }

    public void transformVelocityY(double value) {
        setVelocityY(transformVelocity(value, getVelocityY(), getVelocityYLimit()));
    }

    private double transformVelocity(double value, double current, double limit) {
        double transformedVelocity = current + value;
        if (value > 0) {
            return transformedVelocity > limit ? limit : transformedVelocity;
        } else if (value < 0) {
            return transformedVelocity < (-1) * limit ? (-1) * limit : transformedVelocity;
        } else {
            return current;
        }
    }

    @Override
    public void drawGraphics(GraphicsContext gc) {
        gc.drawImage(getImage(), getStartX(), getStartY(), getWidth(), getHeight());
    }
}
