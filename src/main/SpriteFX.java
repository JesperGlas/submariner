package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteFX extends Sprite {

    private Image img = new Image(String.valueOf(getClass().getResource(getImgUrl())));

    public SpriteFX(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
    }

    @Override
    public void setImgUrl(String url) {
        this.img = new Image(String.valueOf(getClass().getResource(url)));
    }

    public Image getImage() {
        return this.img;
    }

    public void drawGraphics(GraphicsContext gc) {
        gc.drawImage(this.img, getStartX(), getStartY(), getWidth(), getHeight());
    }
}
