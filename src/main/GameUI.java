package main;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class GameUI extends VBox {

    private double width;
    private double height;
    private final double spacing = 10;
    private final String imgUrl = "/img/backgrounds/ui.jpg";

    private int score = 0;
    private UILabel scoreLabel;
    private UILabel healthLabel;
    private UILabel detectionLabel;
    private UILabel nukeLabel;
    private UILabel depthLabel;

    public GameUI(double width, double height) {
        this.width = width;
        this.height = height;
        setSpacing(this.spacing);
        setMinSize(width, height);
        setAlignment(Pos.TOP_CENTER);
        Image image = new Image(String.valueOf(getClass().getResource(imgUrl)));
        BackgroundSize bgSize = new BackgroundSize(width, height, false, false, true, true);
        BackgroundImage bgImage = new BackgroundImage(image, null, null, BackgroundPosition.DEFAULT, bgSize);
        setBackground(new Background(bgImage));
    }

    public void init() {
        scoreLabel = new UILabel("Score: " + score, width, 0.8d, 10);
        healthLabel = new UILabel("Hull Points  : 1000/1000", width, 0.8d, 10);
        detectionLabel = new UILabel("Detection: ", width, 0.8d, 10);
        nukeLabel = new UILabel("Missile Tube: ", width, 0.8d, 10);
        depthLabel = new UILabel("Depth: ", width, 0.8d, 10);
        this.getChildren().addAll(
                scoreLabel,
                healthLabel,
                detectionLabel,
                nukeLabel,
                depthLabel
        );
    }

    public void add(Node node) {
        this.getChildren().add(node);
    }

    public void addAll(ArrayList<Node> nodes) {
        this.getChildren().addAll(nodes);
    }

    public void setHealthLabel(String str) {
        healthLabel.setText(str);
    }

    public void setScoreLabel(String str) {
        scoreLabel.setText(str);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public void setDetectionLabel(String str) {
        detectionLabel.setText(str);
    }

    public void setNukeLabel(String str) {
        nukeLabel.setText(str);
    }
    public void setDepthLabel(String str) {
        depthLabel.setText(str);
    }
}
