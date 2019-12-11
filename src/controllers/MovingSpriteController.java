package controllers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.MovingSprite;
import main.MovingSpriteFX;
import main.Sprite;

import java.util.ArrayList;
import java.util.Iterator;

public class MovingSpriteController {

    private double minBoundX;
    private double minBoundY;
    private double maxBoundX;
    private double maxBoundY;

    private ArrayList<MovingSpriteFX> sprites = new ArrayList<MovingSpriteFX>();

    public MovingSpriteController(double minBoundX, double minBoundY, double maxBoundX, double maxBoundY) {
        this.minBoundX = minBoundX;
        this.minBoundY = minBoundY;
        this.maxBoundX = maxBoundX;
        this.maxBoundY = maxBoundY;
    }

    public void add(MovingSpriteFX sprite) {
        sprites.add(sprite);
    }

    public ArrayList<MovingSpriteFX> checkOutOfBounds() {
        ArrayList<MovingSpriteFX> outOfBounds = new ArrayList<MovingSpriteFX>();
        Iterator<MovingSpriteFX> spritesIterator = sprites.iterator();

        while (spritesIterator.hasNext()) {
            MovingSpriteFX sprite = spritesIterator.next();

            if (sprite.getStartX() < minBoundX ||
                sprite.getStartY() < minBoundY ||
                sprite.getEndX() > maxBoundX ||
                sprite.getEndY() > maxBoundY
            ) {
                outOfBounds.add(sprite);
            }
        }
        return outOfBounds;
    }

    public void remove(Sprite sprite) {
        sprites.remove(sprite);
    }

    public void updateAllPos(double delta) {
        sprites.forEach(movingSpriteFX -> movingSpriteFX.transformPos(delta));
    }

    public void render(GraphicsContext gc) {
        sprites.forEach(movingSpriteFX -> movingSpriteFX.drawGraphics(gc));
    }

    public void print(String msg) {
        System.out.println(msg + sprites.size());
        sprites.forEach(movingSpriteFX -> movingSpriteFX.print("Sprite info: "));
    }
}
