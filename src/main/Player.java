package main;

import java.util.ArrayList;

public class Player extends MovingSpriteFX {

    private ArrayList<MovingSpriteFX> torpedoes = new ArrayList<MovingSpriteFX>();

    public Player(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
    }
}
