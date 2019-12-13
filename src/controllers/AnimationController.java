package controllers;

import javafx.scene.canvas.GraphicsContext;
import main.AnimatedSpriteFX;

import java.util.ArrayList;
import java.util.Iterator;

public class AnimationController {

    ArrayList<AnimatedSpriteFX> sprites = new ArrayList<AnimatedSpriteFX>();

    public void add(AnimatedSpriteFX sprite) {
        sprites.add(sprite);
    }

    public void render(GraphicsContext gc) {
        sprites.forEach(animatedSpriteFX -> {
            animatedSpriteFX.drawGraphics(gc);
        });
    }

    public void update() {
        Iterator<AnimatedSpriteFX> spriteIterator = sprites.iterator();

        while (spriteIterator.hasNext()) {
            AnimatedSpriteFX sprite = spriteIterator.next();
            sprite.nextFrame();
            if (sprite.isFinished()) {
                spriteIterator.remove();
            }
        }
    }

    public void print(String msg) {
        System.out.println(msg + sprites.size());
    }
}
