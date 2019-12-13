package main;

public class Sprite {

    private double centerX;
    private double centerY;
    private double width;
    private double height;

    private String imgUrl = "/img/error.png";

    public Sprite(double xPos, double yPos, double width, double height) {
        this.centerX = xPos;
        this.centerY = yPos;
        this.width = width;
        this.height = height;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getStartX() {
        return this.centerX - this.width / 2d;
    }

    public void setStartX(double x) {
        this.centerX = x + this.width / 2d;
    }

    public double getStartY() {
        return this.centerY - this.height / 2d;
    }

    public void setStartY(double y) {
        this.centerY = y + this.height / 2d;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public void print(String msg) {
        System.out.println(msg + "sX: " + getStartX() + " sY: " + getStartY() + " eX: " + getEndX() + " eY: " + getEndY());
    }
}
