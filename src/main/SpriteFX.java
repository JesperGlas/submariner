package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteFX extends Sprite {

    private String imgUrl = "/img/error.png";
    private Image img = new Image(String.valueOf(getClass().getResource(getImgUrl())));

    public SpriteFX(double xPos, double yPos, double width, double height) {
        super(xPos, yPos, width, height);
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String url) {
        this.img = new Image(String.valueOf(getClass().getResource(url)));
    }

    public void setImage(Image img) {
        this.img = img;
    }

    public Image getImage() {
        return this.img;
    }

    public void drawGraphics(GraphicsContext gc) {
        gc.drawImage(this.img, getStartX(), getStartY(), getWidth(), getHeight());
    }
}
