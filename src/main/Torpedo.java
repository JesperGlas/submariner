package main;

public class Torpedo extends MovingSpriteFX {

    private final String imgUrl = "/img/sprites/torpedo.png";
    private final double width = 30d;
    private final double height = 15d;

    public Torpedo(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
    }
}
