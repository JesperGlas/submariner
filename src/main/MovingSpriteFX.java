package main;

import javafx.scene.canvas.GraphicsContext;

public class MovingSpriteFX extends SpriteFX {

    private double xVelocity = 0d;
    private double yVelocity = 0d;
    private double xSpeedModifier = 1d;
    private double ySpeedModifier = 1d;
    private double xVelocityLimit = 1d;
    private double yVelocityLimit = 1d;
    private int directionX = 1;

    public MovingSpriteFX(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public MovingSpriteFX(double x, double y, double width, double height, String imageUrl) {
        super(x, y, width, height);
        setImgUrl(imageUrl);
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

    public void setSpeedModifier(double value) {
        setSpeedModifierX(value);
        setSpeedModifierY(value);
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

    public void setVelocityLimit(double value) {
        setVelocityLimitX(value);
        setVelocityLimitY(value);
    }

    public int getDirectionX() {
        return directionX;
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }

    public void transformX(double delta) {
        setCenterX(getCenterX() + delta * getSpeedModifierX() * getVelocityX());
    }

    public void transformY(double delta) {
        setCenterY(getCenterY() + delta * getSpeedModifierY() * getVelocityY());
    }

    public void transformPos(double delta) {
        if (getVelocityX() > 0) {
            setDirectionX(1);
        } else if (getVelocityX() < 0) {
            setDirectionX(-1);
        }
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
        if (getDirectionX() < 0) {
            xOffset += widthOffset;
            widthOffset = -widthOffset;
        }
        gc.drawImage(getImage(), xOffset, getStartY(), widthOffset, getHeight());
    }

    private double transformVelocity(double value, double current, double limit) {
        double transformedVelocity = current + value;
        if (value > 0) {
            return Math.min(transformedVelocity, limit);
        } else if (value < 0) {
            return Math.max(transformedVelocity, (-1) * limit);
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

    public double increaseOrLimit(double current, double increase, double limit) {
        double newValue = current + increase;
        return newValue > limit ? limit : newValue;
    }

    public double decreaseOrLimit(double current, double decrease, double limit) {
        double newValue = current - decrease;
        return newValue < limit ? limit : newValue;
    }
}
