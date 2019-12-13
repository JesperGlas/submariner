package main;

import javafx.scene.canvas.GraphicsContext;

public class MovingSpriteFX extends SpriteFX {

    private double xVelocity = 0d;
    private double yVelocity = 0d;
    private double xSpeedModifier = 1d;
    private double ySpeedModifier = 1d;
    private double xVelocityLimit = 1d;
    private double yVelocityLimit = 1d;

    public MovingSpriteFX(double x, double y, double width, double height) {
        super(x, y, width, height);
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

    public void setSpeedModifierX(double value) {
        this.xSpeedModifier = value;
    }

    public double getSpeedModifierY() {
        return ySpeedModifier;
    }

    public void setSpeedModifierY(double value) {
        this.ySpeedModifier = value;
    }

    public double getVelocityLimitX() {
        return xVelocityLimit;
    }

    public void setVelocityLimitX(double value) {
        this.xVelocityLimit = value;
    }

    public double getVelocityLimitY() {
        return yVelocityLimit;
    }

    public void setVelocityLimitY(double value) {
        this.yVelocityLimit = value;
    }

    public void transformX(double delta) {
        setCenterX(getCenterX() + delta * getSpeedModifierX() * getVelocityX());
    }

    public void transformY(double delta) {
        setCenterY(getCenterY() + delta * getSpeedModifierY() * getVelocityY());
    }

    public void transformPos(double delta) {
        transformX(delta);
        transformY(delta);
    }

    public void transformVelocityX(double value) {
        setVelocityX(transformVelocity(value, getVelocityX(), getVelocityLimitX()));
    }

    public void transformVelocityY(double value) {
        setVelocityY(transformVelocity(value, getVelocityY(), getVelocityLimitY()));
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
