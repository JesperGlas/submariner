package main;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class AnimatedSpriteFX extends SpriteFX {

    private String directoryURL;
    private ArrayList<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    private int frames;

    public AnimatedSpriteFX(MovingSpriteFX sprite, String directoryURL, int frames) {
        super(sprite.getCenterX(), sprite.getCenterY(), 0, 0);
        double size = 2 * Math.max(sprite.getWidth(), sprite.getHeight());

        setWidth(size);
        setHeight(size);
        this.directoryURL = directoryURL;
        this.frames = frames;

        for (int n = 0; n < frames; n++) {
            String imagePath = directoryURL + "/" + n + ".png";
            images.add(new Image(String.valueOf(getClass().getResource(imagePath))));
        }
    }

    public Boolean nextFrame() {
        if (currentImageIndex < images.size()) {
            setImage(images.get(currentImageIndex));
            currentImageIndex++;
            return true;
        }
        return false;
    }

    public Boolean isFinished() {
        return currentImageIndex >= frames;
    }
}
