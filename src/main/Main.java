package main;

import controllers.AnimationController;
import controllers.MovingSpriteController;
import controllers.SoundController;
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
import javafx.scene.paint.Color;
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
    private GameUI gameUI;
    private double delta = 1d;
    private int ticks = 0;
    private long elapsedFrames = 0L;
    private Boolean running = true;

    private AnimationController animations = new AnimationController(0, 0, GAME_WIDTH, GAME_HEIGHT);
    private SoundController soundController = new SoundController();

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private SpriteFX gameBackground;
    private Player player;
    private final Sprite surfaceDetectionZone = new Sprite(0, 0, GAME_WIDTH, GAME_HEIGHT);
    private final Sprite crushDepthZone = new Sprite(0, 0, GAME_WIDTH, GAME_HEIGHT);

    private final double mineWidth = 20d;
    private final double mineHeight = mineWidth * 1.34d;
    private MovingSpriteController mineController;

    private final double torpedoWidth = 30d;
    private final double torpedoHeight = torpedoWidth / 2.4d;
    private MovingSpriteController torpedoController;

    private final double intelWidth = 20d;
    private final double intelHeight = intelWidth;
    private MovingSpriteController intelController;

    private final double repairWidth = 20d;
    private final double repairHeight = repairWidth;
    private MovingSpriteController repairController;

    private final double missileWidth = 20d;
    private final double missileHeight = missileWidth * 1.6d;
    private MovingSpriteController missileController;

    private void initScenes() throws Exception {

        // Menu Scene
        Parent menuRoot = FXMLLoader.load(getClass().getResource("/views/mainMenu.fxml"));
        startMenuScene = new Scene(menuRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
        startMenuScene.getStylesheets().addAll(getClass().getResource("/css/style.css").toExternalForm());

        // Game Scene
        GridPane gameRoot = new GridPane();
        gameRoot.setMinSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        Canvas gameCanvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        gameUI = new GameUI(UI_WIDTH, UI__HEIGHT);

        GridPane.setRowIndex(gameCanvas, 0);
        GridPane.setColumnIndex(gameCanvas, 0);
        GridPane.setColumnIndex(gameUI, 1);

        gameGraphics = gameCanvas.getGraphicsContext2D();
        gameRoot.getChildren().addAll(gameCanvas, gameUI);

        gameScene = new Scene(gameRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void initSound() {
        soundController.addMedia("boom", "/sounds/boom.mp3");
        soundController.addMedia("intel", "/sounds/intel.mp3");
        soundController.addMedia("repair", "/sounds/repair.mp3");
        soundController.addMedia("launch", "/sounds/launch.mp3");
    }

    public void initBackground() {
        gameBackground = new SpriteFX(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gameBackground.setStartPos(0, 0);
        gameBackground.setImgUrl("/img/backgrounds/ocean.jpg");
        gameBackground.drawGraphics(gameGraphics);
    }

    private void initPlayer() {
        double playerWidth = 90d;
        double playerHeight = playerWidth / 2.28d;
        player = new Player(GAME_WIDTH / 2d, GAME_HEIGHT / 2d, playerWidth, playerHeight);
        player.setSpeedModifier(2d);
        player.setVelocityLimit(3d);
        player.setImgUrl("/img/sprites/uboat.png");
    }

    private void initPermanentCollisionZones() {
        surfaceDetectionZone.setStartPos(0, 0);
        surfaceDetectionZone.setHeight(GAME_HEIGHT / 2d);

        crushDepthZone.setHeight(GAME_HEIGHT / 4d);
        crushDepthZone.setStartPos(0, GAME_HEIGHT - crushDepthZone.getHeight());
    }

    private void initMineController() {
        mineController = new MovingSpriteController(0, (-mineHeight), GAME_WIDTH, (GAME_HEIGHT + (2d * mineHeight)));
        double mineSpawnDelay = 1 * FPS;
        mineController.setSpawnDelay(mineSpawnDelay);
    }

    private void initTorpedoController() {
        torpedoController = new MovingSpriteController((-torpedoWidth), 0, (GAME_WIDTH + (2d * torpedoWidth)), GAME_HEIGHT);
        double torpedoSpawnDelay = 0.5 * FPS;
        torpedoController.setSpawnDelay(torpedoSpawnDelay);
    }

    private void initIntelController() {
        final int delayFrames = 5 * FPS;
        intelController = new MovingSpriteController(0, (-intelHeight), GAME_WIDTH, (GAME_HEIGHT + (2d * intelHeight)));
        intelController.setSpawnDelay(delayFrames);
    }

    private void initRepairController() {
        final int delayFrames = 10 * FPS;
        repairController = new MovingSpriteController(0, (-repairHeight), GAME_WIDTH, (GAME_HEIGHT + (2d * repairHeight)));
        repairController.setSpawnDelay(delayFrames);
    }

    private void initMissileController() {
        final int delayFrames = 5 * FPS;
        missileController = new MovingSpriteController(0, (-missileHeight), GAME_WIDTH, (GAME_HEIGHT + (2d * missileHeight)));
        missileController.setSpawnDelay(delayFrames);
    }

    /**
     * Initiates the games animation timer
     */
    private void initAnimation() {
        // Needed to avoid bug with high delta at the start of the game.
        AnimationTimer timer = new AnimationTimer() {

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

                    if (running) {
                        update();
                        spawn();
                        collision();
                        render();
                    }

                    ticks++;
                    elapsedFrames++;
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

    private void initGameResources() throws Exception {
        initScenes();
        initSound();
        gameUI.init();
        initBackground();
        initPlayer();
        initPermanentCollisionZones();
        initMineController();
        initTorpedoController();
        initIntelController();
        initRepairController();
        initMissileController();
        initAnimation();
    }

    public void updateUI() {
        gameUI.setScoreLabel("Data collected: " + gameUI.getScore());
        gameUI.setHealthLabel("Hull Integrity: " + player.getHealth() / 1000d * 100 + "%");

        String detectionStr = player.getDetected() ? "[DETECTED] Surface Sensors" : "[STEALTH] Surface Sensors";
        gameUI.setDetectionLabel(detectionStr);

        String nukeStr = missileController.onDelay(elapsedFrames) ? "[RELOADING] Missile Bay" : "[READY] Missile Bay";
        gameUI.setNukeLabel(nukeStr);
    }

    public void handlePlayerCollision() {
        ArrayList<MovingSpriteFX> mineCollisions = mineController.checkCollisions(player, true);
        if (mineCollisions.size() > 0) {
            mineCollisions.forEach(collision -> {
                triggerExplosive(collision);
                player.modifyHealth(-50);
            });
        }

        ArrayList<MovingSpriteFX> torpedoCollisions = torpedoController.checkCollisions(player, true);
        if (torpedoCollisions.size() > 0) {
            torpedoCollisions.forEach(collision -> {
                triggerExplosive(collision);
                player.modifyHealth(-25);
            });
        }

        ArrayList<MovingSpriteFX> intelCollisions = intelController.checkCollisions(player, true);
        if (intelCollisions.size() > 0) {
            gameUI.setScore(gameUI.getScore() + intelCollisions.size());
            soundController.play("intel");
        }

        ArrayList<MovingSpriteFX> repairCollisions = repairController.checkCollisions(player, true);
        if (repairCollisions.size() > 0) {
            player.modifyHealth(25);
            soundController.play("repair");
        }

        // Check if the player is in the detection zone
        player.setDetected(surfaceDetectionZone.checkCollision(player));

        gameUI.setDepthLabel(player.checkCollision(crushDepthZone) ? "[CRITICAL HULL PRESSURE]" : "[SAFE HULL PRESSURE]");
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
        intelController.checkOutOfBounds().forEach(intel -> intelController.remove(intel));
        repairController.checkOutOfBounds().forEach(repair -> repairController.remove(repair));
        missileController.checkOutOfBounds().forEach(missile -> {
            missileController.remove(missile);
            gameUI.setScore(gameUI.getScore() + 10);
        });
    }

    private void handleMineTorpedoCollisions() {
        mineController.getArray().forEach(mine -> {
            torpedoController.checkCollisions(mine, true).forEach(this::triggerExplosive);
            missileController.checkCollisions(mine, true).forEach(this::triggerNuke);
        });
        torpedoController.getArray().forEach(torpedo -> {
            missileController.checkCollisions(torpedo, true).forEach(this::triggerNuke);
        });
    }

    private void handleExplosiveZoneCollision() {
        ArrayList<MovingSpriteFX> explosiveSprites = new ArrayList<>();
        ArrayList<MovingSpriteFX> nukeSprites = new ArrayList<>();
        animations.getArray().forEach(explosion -> {
            if (explosion.checkCircularCollision(player, 4d)) {
                player.modifyHealth(-10d);
                player.transformVelocityX(player.getCenterX() > explosion.getCenterX() ? 0.4d : -0.4d);
                player.transformVelocityY(player.getCenterY() > explosion.getCenterY() ? 0.4d : -0.4d);
            }
            mineController.getArray().stream().filter(mine -> explosion.checkCircularCollision(mine, 2.5d)).forEach(explosiveSprites::add);
            torpedoController.getArray().stream().filter(torpedo -> explosion.checkCircularCollision(torpedo, 2.5d)).forEach(explosiveSprites::add);
            missileController.getArray().stream().filter(missile -> explosion.checkCircularCollision(missile, 5d)).forEach(nukeSprites::add);
        });

        explosiveSprites.forEach(explosiveSprite -> {
            triggerExplosive(explosiveSprite);
            explosiveSprite.setActive(false);
        });
        nukeSprites.forEach(nukeSprite -> {
            triggerNuke(nukeSprite);
            nukeSprite.setActive(false);
        });
    }

    private void triggerExplosive(MovingSpriteFX sprite) {
        animations.add(new AnimatedSpriteFX(sprite, "/img/animations/explosion_2", 2.5d, 23));
        sprite.setActive(false);
        soundController.play("boom");
    }

    private void triggerNuke(MovingSpriteFX sprite) {
        animations.add(new AnimatedSpriteFX(sprite, "/img/animations/explosion_1", 7.5d, 48));
        sprite.setActive(false);
        soundController.play("boom");
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

        if (isPressed(KeyCode.F) && !missileController.onDelay(elapsedFrames)) {
            MovingSpriteFX missile = new MovingSpriteFX(player.getCenterX(), player.getCenterY(), mineWidth, mineHeight, "/img/sprites/missile .png");
            missile.setVelocityLimit(10d);
            missile.setVelocityY((-1) * 0.1d);
            player.launch();
            soundController.play("launch");
            missileController.spawn(missile, elapsedFrames);
        }

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
        mineController.print("Mines: ");
        torpedoController.print("Torpedoes: ");
        intelController.print("Intel: ");
        repairController.print("Repair: ");
        missileController.print("Missile: ");
        animations.print("Animations: ");
    }

    public void update() {
        updateUI();
        gameScene.setOnKeyPressed(keyEvent -> keys.put(keyEvent.getCode(), true));
        gameScene.setOnKeyReleased(keyEvent -> keys.put(keyEvent.getCode(), false));

        movePlayer();

        player.update(delta);

        mineController.update(delta);
        torpedoController.update(delta);
        intelController.update(delta);
        repairController.update(delta);
        missileController.update(delta);
        missileController.getArray().forEach(torpedo -> torpedo.transformVelocityY(-0.1d));

        animations.update();

        if (player.getHealth() <= 0) {
            running = false;
        }
    }

    public void render() {
        gameBackground.drawGraphics(gameGraphics);
        mineController.render(gameGraphics);
        torpedoController.render(gameGraphics);
        intelController.render(gameGraphics);
        repairController.render(gameGraphics);
        missileController.render(gameGraphics);
        player.render(gameGraphics);
        animations.render(gameGraphics);

        if (running == false) {
            gameGraphics.setFill(Color.WHITE);
            gameGraphics.fillText("[GAME OVER COMRADE] Final Score: " + gameUI.getScore(), GAME_WIDTH / 2d, GAME_HEIGHT / 2d);
        }
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
            torpedo.setVelocityX(5d);
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
        handleExplosiveZoneCollision();
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
