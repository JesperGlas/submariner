package main;

import controllers.MovingSpriteController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.HashMap;

public class Main extends Application {

    private double WINDOW_WIDTH = 1600d;
    private double WINDOW_HEIGHT = 1000d;

    private double GAME_WIDTH = WINDOW_WIDTH;
    private double GAME_HEIGHT = WINDOW_HEIGHT * 3d;

    private Scene startMenuScene;
    private Scene gameScene;

    private GraphicsContext gameGraphics;
    private AnimationTimer timer;
    private double delta = 1d;

    private int ticks = 0;

    private SpriteFX background;

    private MovingSpriteFX player;

    private MovingSpriteController mines;

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private void initScenes() throws Exception {

        // Menu Scene
        Parent menuRoot = FXMLLoader.load(getClass().getResource("/views/mainMenu.fxml"));
        startMenuScene = new Scene(menuRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
        startMenuScene.getStylesheets().addAll(getClass().getResource("/css/style.css").toExternalForm());

        // Game Scene
        Group gameRoot = new Group();
        Canvas gameCanvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);

        gameGraphics = gameCanvas.getGraphicsContext2D();
        gameRoot.getChildren().addAll(gameCanvas);

        gameScene = new Scene(gameRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void initBackground() {
        background = new SpriteFX(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        background.setImgUrl("/img/backgrounds/ocean.jpg");
        background.drawGraphics(gameGraphics);
    }

    private void initPlayer() {
        double width = 60;
        double height = 30;
        player = new MovingSpriteFX(WINDOW_WIDTH / 2 - width / 2, WINDOW_HEIGHT / 2 - height / 2, width, height);
        player.setImgUrl("/img/sprites/sub.png");
    }

    private void initMines() {
        mines = new MovingSpriteController(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        MovingSpriteFX mine = new MovingSpriteFX(0, 0, 40, 40);
        mine.setImgUrl("/img/sprites/mine.png");
        mine.setyVelocityLimit(1d);
        mine.transformVelocityY(0.02d);
        mines.add(mine);
    }

    /**
     * Initiates the games animation timer
     */
    private void initAnimation() {
        timer = new AnimationTimer() {

            int FPS = 60;
            double timesPerTick = 1_000_000_000L / FPS;
            double deltaT = 0L;
            long lastUpdate = 0L;
            long timer = 0L;

            @Override
            public void handle(long now) {

                deltaT += (now - lastUpdate) / timesPerTick;
                timer += now - lastUpdate;
                lastUpdate = now;

                delta = deltaT;

                if (deltaT >= 1) {

                    update();

                    render();

                    ticks++;
                    deltaT = 0;
                }

                if (timer >= 1_000_000_000L) {
                    printInfo();
                    ticks = 0;
                    timer = 0L;
                }
            }
        };
        timer.start();
    }

    public void update() {
        gameScene.setOnKeyPressed(keyEvent -> keys.put(keyEvent.getCode(), true));
        gameScene.setOnKeyReleased(keyEvent -> keys.put(keyEvent.getCode(), false));

        movePlayer();
        player.transformPos(delta);
        mines.updateAllPos(delta);
    }

    public void render() {
        background.drawGraphics(gameGraphics);
        player.drawGraphics(gameGraphics);
        mines.render(gameGraphics);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Submariner");

        initScenes();

        primaryStage.setScene(gameScene);
        primaryStage.show();

        initBackground();

        initPlayer();

        initMines();

        initAnimation();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    public void moveCamera() {
    }

    public void movePlayer() {
        double acceleration = 0.02d;
        if (isPressed(KeyCode.W)) {
            player.transformVelocityY((-1) * acceleration);
        }
        if (isPressed(KeyCode.S)) {
            player.transformVelocityY(acceleration);
        }
        if (isPressed(KeyCode.A)) {
            player.transformVelocityX((-1) * acceleration);
        }
        if (isPressed(KeyCode.D)) {
            player.transformVelocityX(acceleration);
        }
    }

    public void printInfo() {
        System.out.println("Ticks and Frames: " + ticks);
        mines.print("Mines: ");
    }
}
