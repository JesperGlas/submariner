package main;

public class MovingSprite extends Sprite {

    private double xVelocity, yVelocity = 0d;
    private double xSpeedModifier, ySpeedModifier = 1d;
    private double xVelocityLimit, yVelocityLimit = 1d;

    public MovingSprite(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
    }

    public double getVelocityX() {
        return xVelocity;
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getVelocityY() {
        return yVelocity;
    }

    public void setyVelocity(double yVelocity) {
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
        xVelocity = transformVelocity(value, this.xVelocity, xVelocityLimit);
    }
    
    public void transformVelocityY(double value) {
        yVelocity = transformVelocity(value, this.yVelocity, yVelocityLimit);
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
}
