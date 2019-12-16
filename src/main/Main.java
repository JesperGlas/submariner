package main;

import controllers.AnimationController;
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
import java.util.Iterator;

public class Main extends Application {

    private final double WINDOW_WIDTH = 1600d;
    private final double WINDOW_HEIGHT = 1000d;

    private final double GAME_WIDTH = WINDOW_WIDTH;
    private final double GAME_HEIGHT = WINDOW_HEIGHT;

    private Scene startMenuScene;
    private Scene gameScene;

    private GraphicsContext gameGraphics;
    private AnimationTimer timer;
    private double delta = 1d;
    private int ticks = 0;

    private SpriteFX background;

    private MovingSpriteFX player;
    private final double playerWidth = 100d;
    private final double playerHeight = playerWidth / 2.28d;

    private final double mineWidth = 20d;
    private final double mineHeight = mineWidth * 1.34d;
    private MovingSpriteController mines;
    private SpawnController mineSpawnController;

    private final double torpedoWidth = 30d;
    private final double torpedoHeight = torpedoWidth / 2.4d;
    private MovingSpriteController torpedoes;
    private SpawnController torpedoSpawnController;

    private AnimationController animations = new AnimationController();

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
        player = new MovingSpriteFX(GAME_WIDTH / 2d, GAME_HEIGHT / 2d, playerWidth, playerHeight);
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

        player.transformPos(delta);

        mines.updateAllPos(delta);
        torpedoes.updateAllPos(delta);

        animations.update();
    }

    public void render() {
        background.drawGraphics(gameGraphics);
        mines.render(gameGraphics);
        torpedoes.render(gameGraphics);
        player.drawGraphics(gameGraphics);
        animations.render(gameGraphics);
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

    private void collision() {
        handleOutOfBoundsCollision();
        handlePlayerCollision();
        handleMineTorpedoCollisions();
    }

    public void handlePlayerCollision() {
        ArrayList<MovingSpriteFX> mineCollisions = mines.checkCollisions(player, true);
        ArrayList<MovingSpriteFX> torpedoCollisions = torpedoes.checkCollisions(player, true);
        if (mineCollisions.size() > 0) {
            mineCollisions.forEach(collision -> animations.add(new AnimatedSpriteFX(collision, "/img/animations/explosion_2", 2.5, 23)));
        }
        if (torpedoCollisions.size() > 0) {
            torpedoCollisions.forEach(collision -> animations.add(new AnimatedSpriteFX(collision, "/img/animations/explosion_2", 2.5, 23)));
        }
    }

    private void handleOutOfBoundsCollision() {
        // Player out of bounds
        if (player.getStartX() <= 0 && player.getVelocityX() < 0) {
            player.setVelocityX(0);
            player.setCenterX(player.getWidth() / 2d);
        } else if (player.getEndX() >= GAME_WIDTH && player.getVelocityX() > 0) {
            player.setVelocityX(0);
            player.setCenterX(GAME_WIDTH - player.getWidth() / 2d);
        } else if (player.getStartY() <= 0 && player.getVelocityY() < 0) {
            player.setVelocityY(0);
            player.setCenterY(player.getHeight() / 2d);
        } else if (player.getEndY() >= GAME_HEIGHT && player.getVelocityY() > 0) {
            player.setVelocityY(0);
            player.setCenterY(GAME_HEIGHT - player.getHeight() / 2d);
        }

        // Controllers out of bounds
        mines.checkOutOfBounds().forEach(mine -> mines.remove(mine));
        torpedoes.checkOutOfBounds().forEach(torpedo -> torpedoes.remove(torpedo));
    }

    private void handleMineTorpedoCollisions() {
        Iterator<MovingSpriteFX> minesIterator = mines.getArray().iterator();

        while (minesIterator.hasNext()) {
            MovingSpriteFX mine = minesIterator.next();
            ArrayList<MovingSpriteFX> mineCollisions = torpedoes.checkCollisions(mine, true);
            mineCollisions.forEach(collision -> animations.add(new AnimatedSpriteFX(collision, "/img/animations/explosion_2", 2.5, 23)));
            if (!mineCollisions.isEmpty()) {
                minesIterator.remove();
            }
        }
    }

    public Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    public void movePlayer() {
        double acceleration = 0.08d;
        double deceleration = acceleration / 2d;
        int xDirection = 0;
        int yDirection = 0;

        yDirection = isPressed(KeyCode.W) ? -1 : yDirection;
        yDirection = isPressed(KeyCode.S) ? 1 : yDirection;
        xDirection = isPressed(KeyCode.A) ? -1 : xDirection;
        xDirection = isPressed(KeyCode.D) ? 1 : xDirection;

        if (yDirection == 0) {
            if (player.getVelocityY() > 0) {
                player.setVelocityY(player.decreaseOrLimit(player.getVelocityY(), deceleration, 0));
            } else {
                player.setVelocityY(player.increaseOrLimit(player.getVelocityY(), deceleration, 0));
            }
        } else {
            player.transformVelocityY(yDirection * acceleration);
        }

        if (xDirection == 0) {
            if (player.getVelocityX() > 0) {
                player.setVelocityX(player.decreaseOrLimit(player.getVelocityX(), deceleration, 0));
            } else {
                player.setVelocityX(player.increaseOrLimit(player.getVelocityX(), deceleration, 0));
            }
        } else {
            player.transformVelocityX(xDirection * acceleration);
        }
    }

    public void printInfo() {
        System.out.println("Ticks and Frames: " + ticks);
        System.out.println("Delta: " + delta);
        player.print("Player: ");
        mines.print("Mines: ");
        torpedoes.print("Torpedoes: ");
        animations.print("Animations: ");
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
                    spawn();
                    collision();
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
