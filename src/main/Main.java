package main;

import controllers.AnimationController;
import controllers.MovingSpriteController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Main extends Application {

    private final double WINDOW_WIDTH = 1800d;
    private final double WINDOW_HEIGHT = 1000d;

    private final double UI_WIDTH = 300d;
    private final double UI__HEIGHT = WINDOW_HEIGHT;

    private final double GAME_WIDTH = WINDOW_WIDTH - UI_WIDTH;
    private final double GAME_HEIGHT = WINDOW_HEIGHT;

    private final int FPS = 30;

    private Scene startMenuScene;
    private Scene gameScene;

    private GraphicsContext gameGraphics;
    private AnimationTimer timer;
    private double delta = 1d;
    private int ticks = 0;
    private int elapsedSeconds = 0;
    private long elapsedFrames = 0L;

    private AnimationController animations = new AnimationController();

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private SpriteFX gameBackground;

    private Player player;
    private final double playerWidth = 90d;
    private final double playerHeight = playerWidth / 2.28d;

    private final double mineWidth = 20d;
    private final double mineHeight = mineWidth * 1.34d;
    private double mineSpawnDelay = 1.5 * FPS;
    private MovingSpriteController mineController;

    private final double torpedoWidth = 30d;
    private final double torpedoHeight = torpedoWidth / 2.4d;
    private double torpedoSpawnDelay = 0.8 * FPS;
    private MovingSpriteController torpedoController;

    private final double intelWidth = 20d;
    private final double intelHeight = intelWidth;
    private MovingSpriteController intelController;

    private final double repairWidth = 20d;
    private final double repairHeight = repairWidth;
    private MovingSpriteController repairController;

    private final Sprite surfaceDetectionZone = new SpriteFX(0, 0, GAME_WIDTH, GAME_HEIGHT);

    private int score = 0;
    private UILabel scoreLabel;
    private UILabel healthLabel;
    private UILabel detectionLabel;

    private void initScenes() throws Exception {

        // Menu Scene
        Parent menuRoot = FXMLLoader.load(getClass().getResource("/views/mainMenu.fxml"));
        startMenuScene = new Scene(menuRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
        startMenuScene.getStylesheets().addAll(getClass().getResource("/css/style.css").toExternalForm());

        // Game Scene
        GridPane gameRoot = new GridPane();
        gameRoot.setMinSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        Canvas gameCanvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        GameUI gameUI = new GameUI(UI_WIDTH, UI__HEIGHT);
        
        // Broken off in to separate method for easier reading.
        initUI(gameUI);

        GridPane.setRowIndex(gameCanvas, 0);
        GridPane.setColumnIndex(gameCanvas, 0);
        GridPane.setColumnIndex(gameUI, 1);

        gameGraphics = gameCanvas.getGraphicsContext2D();
        gameRoot.getChildren().addAll(gameCanvas, gameUI);

        gameScene = new Scene(gameRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void initUI(GameUI gameUI) {
        scoreLabel = new UILabel("Score: " + score, UI_WIDTH, 0.8d, 10);
        healthLabel = new UILabel("Hull Points  : 1000/1000", UI_WIDTH, 0.8d, 10);
        detectionLabel = new UILabel("Detection: ", UI_WIDTH, 0.8d, 10);
        gameUI.getChildren().addAll(scoreLabel, healthLabel, detectionLabel);
    }

    public void initBackground() {
        gameBackground = new SpriteFX(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gameBackground.setStartPos(0, 0);
        gameBackground.setImgUrl("/img/backgrounds/ocean.jpg");
        gameBackground.drawGraphics(gameGraphics);
    }

    private void initPlayer() {
        player = new Player(GAME_WIDTH / 2d, GAME_HEIGHT / 2d, playerWidth, playerHeight);
        player.setSpeedModifier(2d);
        player.setVelocityLimit(3d);
        player.setImgUrl("/img/sprites/uboat.png");
        healthLabel.setText("Hull Points: " + player.getHealth() + "/1000");
    }

    private void initSurfaceDetectionZone() {
        surfaceDetectionZone.setWidth(GAME_WIDTH);
        surfaceDetectionZone.setHeight(GAME_HEIGHT / 2d);
        surfaceDetectionZone.setStartPos(0, 0);
    }

    private void initMineController() {
        mineController = new MovingSpriteController(0, (-mineHeight), GAME_WIDTH, (GAME_HEIGHT + (2d * mineHeight)));
        mineController.setSpawnDelay(mineSpawnDelay);
    }

    private void initTorpedoController() {
        torpedoController = new MovingSpriteController((-torpedoWidth), 0, (GAME_WIDTH + (2d * torpedoWidth)), GAME_HEIGHT);
        torpedoController.setSpawnDelay(torpedoSpawnDelay);
    }

    private void initIntelController() {
        final int delayFrames = 10 * FPS;
        intelController = new MovingSpriteController(0, (-intelHeight), GAME_WIDTH, (GAME_HEIGHT + (2d * intelHeight)));
        intelController.setSpawnDelay(delayFrames);
    }

    private void initRepairController() {
        final int delayFrames = 10 * FPS;
        repairController = new MovingSpriteController(0, (-repairHeight), GAME_WIDTH, (GAME_HEIGHT + (2d * repairHeight)));
        repairController.setSpawnDelay(delayFrames);
    }

    private void initGameResources() throws Exception {
        initScenes();
        initBackground();
        initPlayer();
        initSurfaceDetectionZone();
        initMineController();
        initTorpedoController();
        initIntelController();
        initRepairController();
        initAnimation();
    }

    public void update() {
        gameScene.setOnKeyPressed(keyEvent -> keys.put(keyEvent.getCode(), true));
        gameScene.setOnKeyReleased(keyEvent -> keys.put(keyEvent.getCode(), false));

        movePlayer();

        player.transformPos(delta);

        mineController.updateAllPos(delta);
        torpedoController.updateAllPos(delta);
        intelController.updateAllPos(delta);
        repairController.updateAllPos(delta);

        animations.update();
    }

    public void render() {
        gameBackground.drawGraphics(gameGraphics);

        mineController.render(gameGraphics);
        torpedoController.render(gameGraphics);
        intelController.render(gameGraphics);
        repairController.render(gameGraphics);

        player.drawGraphics(gameGraphics);
        animations.render(gameGraphics);
    }

    public void spawn() {
        if (!mineController.onDelay(elapsedFrames)) {
            MovingSpriteFX mine = new MovingSpriteFX(0, 0, mineWidth, mineHeight, "/img/sprites/barrel.png");
            mine.setVelocityY(2d);
            // Assign a temporary variable that determines if the mines are dropped above the player or at random based on player y position.
            double mineXPos = player.getDetected() ? player.getCenterX() : mineController.getRandomX();
            mineController.spawnAt(mine, elapsedFrames, mineXPos, mineController.getMinBoundY());

        }
        if (!torpedoController.onDelay(elapsedFrames)) {
            MovingSpriteFX torpedo = new MovingSpriteFX(0, 0, torpedoWidth, torpedoHeight, "/img/sprites/torpedo.png");
            torpedo.setVelocityX(6d);
            torpedoController.spawnAtRandomY(torpedo, elapsedFrames);
        }
        if (!intelController.onDelay(elapsedFrames)) {
            MovingSpriteFX intel = new MovingSpriteFX(0, 0, intelWidth, intelHeight, "/img/sprites/folder.png");
            intel.setVelocityY(1d);
            intelController.spawnAtRandomX(intel, elapsedFrames);
        }
        if (!repairController.onDelay(elapsedFrames)) {
            MovingSpriteFX repair = new MovingSpriteFX(0, 0, repairWidth, repairHeight, "/img/sprites/tools.png");
            repair.setVelocityY(4d);
            repairController.spawnAtRandomX(repair, elapsedFrames);
        }
    }

    private void collision() {
        handleOutOfBoundsCollision();
        handlePlayerCollision();
        handleMineTorpedoCollisions();
    }

    public void handlePlayerCollision() {
        ArrayList<MovingSpriteFX> mineCollisions = mineController.checkCollisions(player, true);
        if (mineCollisions.size() > 0) {
            mineCollisions.forEach(collision -> {
                animations.add(new AnimatedSpriteFX(collision, "/img/animations/explosion_2", 2.5, 23));
                player.modifyHealth(-50);
            });
        }

        ArrayList<MovingSpriteFX> torpedoCollisions = torpedoController.checkCollisions(player, true);
        if (torpedoCollisions.size() > 0) {
            torpedoCollisions.forEach(collision -> {
                animations.add(new AnimatedSpriteFX(collision, "/img/animations/explosion_2", 2.5, 23));
                player.modifyHealth(-20);
            });
        }

        ArrayList<MovingSpriteFX> intelCollisions = intelController.checkCollisions(player, true);
        if (intelCollisions.size() > 0) {
            score += intelCollisions.size();
        }

        ArrayList<MovingSpriteFX> repairCollisions = repairController.checkCollisions(player, true);
        if (repairCollisions.size() > 0) {
            player.modifyHealth(25);
        }

        player.setDetected(surfaceDetectionZone.checkCollision(player));

        scoreLabel.setText("Score: " + score);
        healthLabel.setText("Hull Points: " + player.getHealth() + "/1000");

        String detectionStr = player.getDetected() ? "WARNING! Surface Detection!" : "HIDDEN";
        detectionLabel.setText(detectionStr);
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
        mineController.checkOutOfBounds().forEach(mine -> mineController.remove(mine));
        torpedoController.checkOutOfBounds().forEach(torpedo -> torpedoController.remove(torpedo));
    }

    private void handleMineTorpedoCollisions() {
        Iterator<MovingSpriteFX> minesIterator = mineController.getArray().iterator();

        while (minesIterator.hasNext()) {
            MovingSpriteFX mine = minesIterator.next();
            ArrayList<MovingSpriteFX> mineCollisions = torpedoController.checkCollisions(mine, true);
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
        System.out.println("Score: " + score);
        System.out.println("Delta: " + delta);
        player.print("Player: ");
        mineController.print("Mines: ");
        torpedoController.print("Torpedoes: ");
        intelController.print("Intel: ");
        animations.print("Animations: ");
    }

    /**
     * Initiates the games animation timer
     */
    private void initAnimation() {
        timer = new AnimationTimer() {

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
                    elapsedFrames++;
                    deltaT = 0;
                }

                if (timer >= 1_000_000_000L) {
                    elapsedSeconds++;
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
        initGameResources();
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
