package main;

import controllers.MovingSpriteController;
import controllers.SpawnController;
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

    private final double mineWidth = 30d;
    private final double mineHeight = mineWidth * 1.34d;
    private MovingSpriteController mines;
    private SpawnController mineSpawnController;

    private final double torpedoWidth = 30d;
    private final double torpedoHeight = torpedoWidth / 2.4d;
    private MovingSpriteController torpedoes;
    private SpawnController torpedoSpawnController;

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
        double width = 80d;
        double height = 30d;
        player = new MovingSpriteFX(WINDOW_WIDTH / 2d, WINDOW_HEIGHT / 2d, width, height);
        player.setSpeedModifierY(2d);
        player.setSpeedModifierX(2d);
        player.setVelocityLimitX(2d);
        player.setVelocityLimitY(2d);
        player.setImgUrl("/img/sprites/uboat.png");
    }

    private void initMines() {
        final double spacing = 2d;
        mines = new MovingSpriteController(0, 0, GAME_WIDTH, GAME_HEIGHT);
        mineSpawnController = new SpawnController(0, 0, GAME_WIDTH, mineHeight * spacing);
    }

    private void initTorpedoes() {
        final double spacing = 4d;
        torpedoes = new MovingSpriteController(0, 0, GAME_WIDTH, GAME_HEIGHT);
        torpedoSpawnController = new SpawnController(0, 0, torpedoWidth * spacing, GAME_HEIGHT);
    }

    public void update() {
        gameScene.setOnKeyPressed(keyEvent -> keys.put(keyEvent.getCode(), true));
        gameScene.setOnKeyReleased(keyEvent -> keys.put(keyEvent.getCode(), false));

        movePlayer();

        spawn();

        player.transformPos(delta);
        mines.updateAllPos(delta);
        torpedoes.updateAllPos(delta);
    }

    public void render() {
        background.drawGraphics(gameGraphics);
        mines.render(gameGraphics);
        torpedoes.render(gameGraphics);
        player.drawGraphics(gameGraphics);
    }

    public void spawn() {
        if (mineSpawnController.spawnAreaClear(mines.getArray())) {
            MovingSpriteFX mine = new MovingSpriteFX(mineSpawnController.getRandomX(), mineHeight / 2d, mineWidth, mineHeight);
            mine.setImgUrl("/img/sprites/barrel.png");
            mine.setVelocityY(2d);
            mines.add(mine);
        }
        if (torpedoSpawnController.spawnAreaClear(torpedoes.getArray())) {
            MovingSpriteFX torpedo = new MovingSpriteFX(torpedoWidth / 2d, torpedoSpawnController.getRandomY(), torpedoWidth, torpedoHeight);
            torpedo.setImgUrl("/img/sprites/torpedo.png");
            torpedo.setVelocityX(3d);
            torpedoes.add(torpedo);
        }
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
        Boolean xModified = false;
        Boolean yModified = false;

        if (isPressed(KeyCode.W)) {
            yModified = true;
            player.transformVelocityY((-1d) * acceleration);
        }

        if (isPressed(KeyCode.S)) {
            yModified = true;
            player.transformVelocityY(acceleration);
        }

        if (isPressed(KeyCode.A)) {
            xModified = true;
            player.transformVelocityX((-1d) * acceleration);
        }

        if (isPressed(KeyCode.D)) {
            xModified = true;
            player.transformVelocityX(acceleration);
        }

        if (!yModified) {
            if (player.getVelocityY() > 0) {
                player.setVelocityY(player.decreaseOrLimit(player.getVelocityY(), acceleration, 0));
            } else {
                player.setVelocityY(player.increaseOrLimit(player.getVelocityY(), acceleration, 0));
            }
        }

        if (!xModified) {
            if (player.getVelocityX() > 0) {
                player.setVelocityX(player.decreaseOrLimit(player.getVelocityX(), acceleration, 0));
            } else {
                player.setVelocityX(player.increaseOrLimit(player.getVelocityX(), acceleration, 0));
            }
        }
    }

    public void printInfo() {
        System.out.println("Ticks and Frames: " + ticks);
        player.print("Player: ");
        mines.print("Mines: ");
        torpedoSpawnController.print("TSpawn: ");
        torpedoes.print("Torpedoes: ");
        System.out.println("Delta: " + delta);
    }

    /**
     * Initiates the games animation timer
     */
    private void initAnimation() {
        timer = new AnimationTimer() {

            int FPS = 30;
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
        System.out.println("Hej");
        launch(args);
    }
}
