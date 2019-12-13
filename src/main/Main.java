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

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {

    private double WINDOW_WIDTH = 1600d;
    private double WINDOW_HEIGHT = 1000d;

    private double GAME_WIDTH = WINDOW_WIDTH;
    private double GAME_HEIGHT = WINDOW_HEIGHT;

    private Scene startMenuScene;
    private Scene gameScene;

    private GraphicsContext gameGraphics;
    private AnimationTimer timer;
    private double delta = 1d;

    private int ticks = 0;

    private SpriteFX background;

    private MovingSpriteFX player;

    private MovingSpriteController mines;
    private MovingSpriteController torpedoes;

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
        background = new SpriteFX(GAME_WIDTH / 2d, GAME_HEIGHT / 2d, GAME_WIDTH, GAME_HEIGHT);
        background.setImgUrl("/img/backgrounds/ocean.jpg");
        background.drawGraphics(gameGraphics);
    }

    private void initPlayer() {
        double width = 60;
        double height = 30;
        player = new MovingSpriteFX(WINDOW_WIDTH / 2d, WINDOW_HEIGHT / 2d, width, height);
        player.setVelocityLimitX(2d);
        player.setVelocityLimitY(2d);
        player.setImgUrl("/img/sprites/sub.png");
    }

    private void initMines() {
        mines = new MovingSpriteController(0, 0, GAME_WIDTH, GAME_HEIGHT);
        MovingSpriteFX mine = new MovingSpriteFX(20, 20, 40, 40);
        mine.setImgUrl("/img/sprites/mine.png");
        mine.setVelocityY(1d);
        mines.add(mine);
    }

    private void initTorpedoes() {
        torpedoes = new MovingSpriteController(0, 0, GAME_WIDTH, GAME_HEIGHT);
        MovingSpriteFX torpedo = new MovingSpriteFX(15, 20, 30, 15);
        torpedo.setImgUrl("/img/sprites/torpedo.png");
        torpedo.setVelocityX(2d);
        MovingSpriteFX torpedo2 = new MovingSpriteFX(WINDOW_WIDTH - 30, 50, 30, 15);
        torpedo2.setImgUrl("/img/sprites/torpedo.png");
        torpedo2.setVelocityX(-1d);
        torpedoes.add(torpedo);
        torpedoes.add(torpedo2);
    }

    public void update() {
        gameScene.setOnKeyPressed(keyEvent -> keys.put(keyEvent.getCode(), true));
        gameScene.setOnKeyReleased(keyEvent -> keys.put(keyEvent.getCode(), false));

        movePlayer();

        player.transformPos(delta);
        mines.updateAllPos(delta);
        torpedoes.updateAllPos(delta);
    }

    public void render() {
        background.drawGraphics(gameGraphics);
        player.drawGraphics(gameGraphics);

        mines.render(gameGraphics);
        torpedoes.render(gameGraphics);
    }

    public void collisionAndClear() {
        ArrayList<MovingSpriteFX> mineCollisions = mines.checkCollisions(player, true);
        ArrayList<MovingSpriteFX> torpedoCollisions = torpedoes.checkCollisions(player, true);
        if (mineCollisions.size() > 0) {
            System.out.println("Mine Collision");
        }
        if (torpedoCollisions.size() > 0) {
            System.out.println("Torpedoes Collision");
        }
        mines.checkOutOfBounds().forEach(mine -> mines.remove(mine));
        torpedoes.checkOutOfBounds().forEach(torpedo -> torpedoes.remove(torpedo));
    }

    public Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    public void movePlayer() {
        double acceleration = 0.05d;

        if (isPressed(KeyCode.W)) {
            player.transformVelocityY((-1d) * acceleration);
        }

        if (isPressed(KeyCode.S)) {
            player.transformVelocityY(acceleration);
        }

        if (isPressed(KeyCode.A)) {
            player.transformVelocityX((-1d) * acceleration);
        }

        if (isPressed(KeyCode.D)) {
            player.transformVelocityX(acceleration);
        }
    }

    public void printInfo() {
        System.out.println("Ticks and Frames: " + ticks);
        player.print("Player: ");
        mines.print("Mines: ");
        torpedoes.print("Torpedoes: ");
        System.out.println("Delta: " + delta);
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

                if (deltaT >= 1) {

                    // Needed to avoid bug with high delta at the start of the game.
                    delta = deltaT <= 10 ? deltaT : delta;

                    update();
                    render();
                    collisionAndClear();

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Submariner");

        initScenes();

        primaryStage.setScene(gameScene);
        primaryStage.show();

        initBackground();

        initPlayer();

        initMines();
        initTorpedoes();

        initAnimation();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
