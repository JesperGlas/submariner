package main;

public class Sprite {

    private double startX;
    private double startY;
    private double width;
    private double height;

    private String imgUrl = "/img/error.png";

    public Sprite(double startX, double startY, double width, double height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double x) {
        this.startX = x;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double y) {
        this.startY = y;
    }

    public double getEndX() {
        return this.startX + this.width;
    }

    public double getEndY() {
        return this.startY + this.height;
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
}
