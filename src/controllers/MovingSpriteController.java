package controllers;

import javafx.scene.canvas.GraphicsContext;
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

    public ArrayList<MovingSpriteFX> getArray() {
        return this.sprites;
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

    public ArrayList<MovingSpriteFX> checkCollisions(MovingSpriteFX sprite, Boolean removeIfCollision) {
        ArrayList<MovingSpriteFX> spritesCollision = new ArrayList<MovingSpriteFX>();
        Iterator<MovingSpriteFX> movingSpriteFXIterator = sprites.iterator();

        while (movingSpriteFXIterator.hasNext()) {
            MovingSpriteFX current = movingSpriteFXIterator.next();

            if (current.checkCollision(sprite)) {
                spritesCollision.add(current);
                if (removeIfCollision) {
                    movingSpriteFXIterator.remove();
                }
            }
        }
        return spritesCollision;
    }

    public void remove(Sprite sprite) {
        if (sprite != null) {
            sprites.remove(sprite);
        }
    }

    public void updateAllPos(double delta) {
        sprites.forEach(movingSpriteFX -> movingSpriteFX.transformPos(delta));
    }

    public void render(GraphicsContext gc) {
        sprites.forEach(movingSpriteFX -> movingSpriteFX.drawGraphics(gc));
    }

    public void transformVelocityX(double value) {
        sprites.forEach(spriteFX -> spriteFX.transformVelocityX(value));
    }

    public void print(String msg) {
        System.out.println(msg + sprites.size());
        sprites.forEach(movingSpriteFX -> movingSpriteFX.print("Sprite info: "));
    }
}
