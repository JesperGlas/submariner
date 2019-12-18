package controllers;

import javafx.scene.canvas.GraphicsContext;
import main.AnimatedSpriteFX;
import main.MovingSpriteFX;
import main.SpriteFX;

import java.util.ArrayList;
import java.util.Iterator;

public class AnimationController extends MovingSpriteController {

    public AnimationController(double minBoundX, double minBoundY, double maxBoundX, double maxBoundY) {
        super(minBoundX, minBoundY, maxBoundX, maxBoundY);
    }

    public void add(AnimatedSpriteFX sprite) {
        super.add(sprite);
    }

    public void update() {
        Iterator<MovingSpriteFX> spriteIterator = super.getArray().iterator();
        while (spriteIterator.hasNext()) {
            AnimatedSpriteFX sprite = (AnimatedSpriteFX) spriteIterator.next();
            sprite.nextFrame();
            if (sprite.animationFinished()) {
                spriteIterator.remove();
            }
        }
    }
}
