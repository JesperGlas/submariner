package main;

public class Sprite {

    private double centerX;
    private double centerY;
    private double width;
    private double height;

    public Sprite(double centerX, double centerY, double width, double height) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
    }

    /**
     * Getter for the sprites center x position.
     * @return Center x value
     */
    public double getCenterX() {
        return centerX;
    }

    /**
     * Setter for the sprites position using its center as the starting point.
     * @param value is the current x position of the sprites center.
     */
    public void setCenterX(double value) {
        this.centerX = value;
    }

    /**
     * Getter for the sprites center y position.
     * @return Center y value.
     */
    public double getCenterY() {
        return centerY;
    }

    /**
     * Setter for the sprites position using the center as the starting point.
     * @param value is the current y position of the sprites center.
     */
    public void setCenterY(double value) {
        this.centerY = value;
    }

    /**
     * Getter for the x value at the sprites top, left corner.
     * @return x value at the top, left corner of the sprite.
     */
    public double getStartX() {
        return this.centerX - this.width / 2d;
    }

    /**
     * Setter for the sprites position using its top, left corner as the starting point.
     * @param value is the current x position of the sprites top, left corner.
     */
    public void setStartX(double value) {
        this.centerX = value + this.width / 2d;
    }

    /**
     * Getter for the sprites top, left corners y position.
     * @return y value at the top, left corner of the sprite.
     */
    public double getStartY() {
        return this.centerY - this.height / 2d;
    }

    /**
     * Setter for the sprites position using its top, left corner as the starting point.
     * @param value is the current y position of the sprites top, left corner.
     */
    public void setStartY(double value) {
        this.centerY = value + this.height / 2d;
    }

    public void setCenterPos(double x, double y) {
        setCenterX(x);
        setCenterY(y);
    }

    /**
     * Setter for the sprites position using its top, left corner as the starting point. The method uses the setStartX and setStartY methods.
     * @param x is the current x position of the sprites top, left corner.
     * @param y is the current y position of the sprites top, left corner
     */
    public void setStartPos(double x, double y) {
        setStartX(x);
        setStartY(y);
    }

    public void setEndPos(double x, double y) {
        setWidth(getStartX() - x);
        setHeight(getStartY() - y);
    }

    public double getEndX() {
        return this.centerX + this.width / 2d;
    }

    public double getEndY() {
        return this.centerY + this.height / 2d;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double w) {
        this.width = w;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double h) {
        this.height = h;
    }

    public Boolean checkCollision(Sprite sprite) {
        return checkCollision(sprite.getStartX(), sprite.getStartY(), sprite.getStartX() + sprite.getWidth(), sprite.getStartY() + sprite.getHeight());
    }

    public Boolean checkCollision(double startX, double startY, double endX, double endY) {
        return (
                this.getStartX() + this.getWidth() > startX &&
                this.getStartY() + this.getHeight() > startY &&
                endX > this.getStartX() &&
                endY > this.getStartY()
        );
    }

    public Boolean checkCircularCollision(Sprite sprite, double localRadiusMultiplier) {
        return checkCircularCollision(centerX, centerY, width / 2d * localRadiusMultiplier, sprite.getCenterX(), sprite.getCenterY(), sprite.getWidth() / 2d);
    }

    public Boolean checkCircularCollision(double localCenterX, double localCenterY, double localRadius, double foreignCenterX, double foreignCenterY, double foreignRadius) {
        return Math.abs(Math.sqrt( Math.pow((localCenterX - foreignCenterX), 2) + Math.pow((localCenterY - foreignCenterY), 2))) <= Math.abs(localRadius + foreignRadius);
    }

    public void print(String msg) {
        System.out.println(msg + "sX: " + getStartX() + " sY: " + getStartY() + " eX: " + getEndX() + " eY: " + getEndY());
    }
}
