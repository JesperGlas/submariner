package controllers;

import main.MovingSpriteFX;
import main.Sprite;

import java.util.ArrayList;
import java.util.Random;

public class SpawnController extends Sprite {

    private Random random = new Random();
    private int delay = 0;
    private int lastSpawn = 0;

    public SpawnController(double xMin, double yMin, double xMax, double yMax, int delaySeconds) {
        super(0, 0, 0, 0);
        double width = xMax - xMin;
        double height = yMax - yMin;
        this.setCenterX(xMin + width / 2d);
        this.setCenterY(yMin + height / 2d);
        this.setWidth(width);
        this.setHeight(height);
        this.setDelay(delaySeconds);
    }

    public int getLastSpawn() {
        return lastSpawn;
    }

    public void setLastSpawn(int lastSpawn) {
        this.lastSpawn = lastSpawn;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Boolean spawnAreaClear(ArrayList<MovingSpriteFX> list) {
        for (MovingSpriteFX sprite : list) {
            if (this.checkCollision(sprite)) {
                return false;
            }
        }
        return true;
    }

    public Boolean delayFinished(int timestamp) {
        return timestamp > lastSpawn + getDelay();
    }

    public double getRandomX() {
        return getRandom(this.getStartX(), this.getEndX());
    }

    public double getRandomY() {
        return getRandom(this.getStartY(), this.getEndY());
    }

    private double getRandom(double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }
}
