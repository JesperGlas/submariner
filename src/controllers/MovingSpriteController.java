package controllers;

import javafx.scene.canvas.GraphicsContext;
import main.MovingSpriteFX;
import main.Sprite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MovingSpriteController {

    private final Random random = new Random();

    private double minBoundX;
    private double minBoundY;
    private double maxBoundX;
    private double maxBoundY;

    private double spawnDelay = 0d;
    private long lastSpawn = 0L;

    private ArrayList<MovingSpriteFX> sprites = new ArrayList<MovingSpriteFX>();

    public MovingSpriteController(double minBoundX, double minBoundY, double maxBoundX, double maxBoundY) {
        this.minBoundX = minBoundX;
        this.minBoundY = minBoundY;
        this.maxBoundX = maxBoundX;
        this.maxBoundY = maxBoundY;
    }

    public double getMinBoundX() {
        return minBoundX;
    }

    public void setMinBoundX(double minBoundX) {
        this.minBoundX = minBoundX;
    }

    public double getMinBoundY() {
        return minBoundY;
    }

    public void setMinBoundY(double minBoundY) {
        this.minBoundY = minBoundY;
    }

    public double getMaxBoundX() {
        return maxBoundX;
    }

    public void setMaxBoundX(double maxBoundX) {
        this.maxBoundX = maxBoundX;
    }

    public double getMaxBoundY() {
        return maxBoundY;
    }

    public void setMaxBoundY(double maxBoundY) {
        this.maxBoundY = maxBoundY;
    }

    public double getSpawnDelay() {
        return spawnDelay;
    }

    public void setSpawnDelay(double delayFrames) {
        this.spawnDelay = delayFrames;
    }

    public long getLastSpawn() {
        return lastSpawn;
    }

    public void setLastSpawn(long lastSpawn) {
        this.lastSpawn = lastSpawn;
    }

    public double getRandomX() {
        return getRandom(getMinBoundX(), getMaxBoundX());
    }

    public double getRandomY() {
        return getRandom(getMinBoundY(), getMaxBoundY());
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

    public void update(double delta) {
        Iterator<MovingSpriteFX> spriteFXIterator = sprites.iterator();
        while (spriteFXIterator.hasNext()) {
            MovingSpriteFX sprite = spriteFXIterator.next();
            if (sprite.getActive()) {
                sprite.transformPos(delta);
            } else {
                spriteFXIterator.remove();
            }
        }
    }

    public void render(GraphicsContext gc) {
        sprites.forEach(movingSpriteFX -> movingSpriteFX.drawGraphics(gc));
    }

    public void spawn(MovingSpriteFX sprite, long currentFrame) {
        add(sprite);
        setLastSpawn(currentFrame);
    }

    public void spawnAt(MovingSpriteFX sprite, long currentTime, double x, double y) {
        if(!onDelay(currentTime)) {
            sprite.setStartX(x);
            sprite.setStartY(y);
            spawn(sprite, currentTime);
        }
    }

    public void spawnAtRandomX(MovingSpriteFX sprite, long currentTime) {
        if (!onDelay(currentTime)) {
            sprite.setStartX(getRandomX());
            sprite.setStartY(getMinBoundY());
            spawn(sprite, currentTime);
        }
    }

    public void spawnAtRandomY(MovingSpriteFX sprite, long currentFrame) {
        if (!onDelay(currentFrame)) {
            sprite.setStartX(getMinBoundX());
            sprite.setStartY(getRandomY());
            spawn(sprite, currentFrame);
        }
    }

    public void transformVelocityX(double value) {
        sprites.forEach(spriteFX -> spriteFX.transformVelocityX(value));
    }

    public void print(String msg) {
        System.out.println(msg + sprites.size());
    }

    public int count() {
        return this.sprites.size();
    }

    public Boolean onDelay(long currentFrame) {
        return getLastSpawn() + getSpawnDelay() >= currentFrame;
    }

    public void remove(MovingSpriteFX sprite) {
        if (sprites.contains(sprite)) {
            sprites.remove(sprite);
        }
    }

    private double getRandom(double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }
}
