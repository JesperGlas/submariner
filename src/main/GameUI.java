package main;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class GameUI extends VBox {

    private final double spacing = 10;
    private final String imgUrl = "/img/backgrounds/ui.jpg";

    public GameUI(double width, double height) {
        setSpacing(this.spacing);
        setMinSize(width, height);
        setAlignment(Pos.TOP_CENTER);
        Image image = new Image(String.valueOf(getClass().getResource(imgUrl)));
        BackgroundSize bgSize = new BackgroundSize(width, height, false, false, true, true);
        BackgroundImage bgImage = new BackgroundImage(image, null, null, BackgroundPosition.DEFAULT, bgSize);
        setBackground(new Background(bgImage));
    }

    public void add(Node node) {
        this.getChildren().add(node);
    }

    public void addAll(ArrayList<Node> nodes) {
        this.getChildren().addAll(nodes);
    }
}
