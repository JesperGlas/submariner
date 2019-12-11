package main;

import javafx.scene.canvas.GraphicsContext;

public class MovingSpriteFX extends SpriteFX {

    private double xVelocity = 0d;
    private double yVelocity = 0d;
    private double xSpeedModifier = 1d;
    private double ySpeedModifier = 1d;
    private double xVelocityLimit = 2d;
    private double yVelocityLimit = 2d;

    public MovingSpriteFX(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
    }

    public double getVelocityX() {
        return xVelocity;
    }

    public void setVelocityX(double value) {
        this.xVelocity = value;
    }

    public double getVelocityY() {
        return yVelocity;
    }

    public void setVelocityY(double value) {
        this.yVelocity = value;
    }

    public double getSpeedModifierX() {
        return xSpeedModifier;
    }

    public void setxSpeedModifier(double value) {
        this.xSpeedModifier = value;
    }

    public double getSpeedModifierY() {
        return ySpeedModifier;
    }

    public void setySpeedModifier(double value) {
        this.ySpeedModifier = value;
    }

    public double getVelocityXLimit() {
        return xVelocityLimit;
    }

    public void setxVelocityLimit(double value) {
        this.xVelocityLimit = value;
    }

    public double getVelocityYLimit() {
        return yVelocityLimit;
    }

    public void setyVelocityLimit(double value) {
        this.yVelocityLimit = value;
    }

    public void transformX(double delta) {
        setStartX(getStartX() + delta * getSpeedModifierX() * getVelocityX());
    }

    public void transformY(double delta) {
        setStartY(getStartY() + delta * getVelocityY() * getSpeedModifierY());
    }

    public void transformPos(double delta) {
        transformX(delta);
        transformY(delta);
    }

    public void transformVelocityX(double value) {
        setVelocityX(transformVelocity(value, getVelocityX(), getVelocityXLimit()));
    }

    public void transformVelocityY(double value) {
        setVelocityY(transformVelocity(value, getVelocityY(), getVelocityYLimit()));
    }

    @Override
    public void drawGraphics(GraphicsContext gc) {
        double xOffset = getStartX();
        double widthOffset = getWidth();
        if (getVelocityX() < 0) {
            xOffset += widthOffset;
            widthOffset = -widthOffset;
        }
        gc.drawImage(getImage(), xOffset, getStartY(), widthOffset, getHeight());
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

    public String toString() {
        return "sX: " + getStartX() + " sY: " + getStartY() + " eX: " + getEndX() + " eY: " + getEndY();
    }

    public void print(String msg) {
        System.out.println(msg + toString());
    }
}
