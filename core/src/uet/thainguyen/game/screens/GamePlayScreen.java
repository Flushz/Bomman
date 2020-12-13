package uet.thainguyen.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uet.thainguyen.game.BommanGame;
import uet.thainguyen.game.controllers.MapController;
import uet.thainguyen.game.controllers.SoundController;
import uet.thainguyen.game.entities.dynamics.Enemy;
import uet.thainguyen.game.entities.dynamics.Octopus;
import uet.thainguyen.game.entities.explosion.Bomb;
import uet.thainguyen.game.entities.explosion.Flame;
import uet.thainguyen.game.entities.items.*;
import uet.thainguyen.game.entities.statics.BrickLayer;
import uet.thainguyen.game.entities.statics.GrassLayer;
import uet.thainguyen.game.entities.statics.WallLayer;
import uet.thainguyen.game.entities.dynamics.Hare;
import uet.thainguyen.game.entities.dynamics.Bomman;

import java.util.ArrayList;

public class GamePlayScreen implements Screen {

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    BommanGame game;

    SoundController soundController;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer renderer;

    MapController gameMap;
    MapLayer collisionLayer;
    GrassLayer grassLayer;
    WallLayer wallLayer;
    BrickLayer brickLayer;

    Bomman bomman;
    ArrayList<Enemy> enemies;
    ArrayList<Bomb> bombs;
    ArrayList<Item> items;

    Stage playStage;
    Label timeLabel;
    Label lifeLabel;
    Texture aliveTexture;
    Texture deadTexture;

    private float elapsedTime = 0;

    public GamePlayScreen(BommanGame game, int level) {
        this.game = game;

        soundController = new SoundController();
        soundController.getMenuOpenSound().play();
        soundController.getGameMusic().play();
        soundController.getGameMusic().setLooping(true);

        camera = new OrthographicCamera();
        gameMap = new MapController(camera, level);
        collisionLayer = gameMap.getTiledMap().getLayers().get("CollisionLayer");
        grassLayer = new GrassLayer(gameMap.getTiledMap());
        wallLayer = new WallLayer(gameMap.getTiledMap());
        brickLayer = new BrickLayer(gameMap.getTiledMap());
        renderer = new OrthogonalTiledMapRenderer(gameMap.getTiledMap());
        renderer.setView(camera);

        items = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            items.add(new SlowItem(gameMap.generateX(), gameMap.generateY()));
            items.add(new SpeedItem(gameMap.generateX(), gameMap.generateY()));
            items.add(new BombBagItem(gameMap.generateX(), gameMap.generateY()));
            items.add(new LifeItem(gameMap.generateX(), gameMap.generateY()));
            items.add(new SuperBombItem(gameMap.generateX(), gameMap.generateY()));
        }

        bomman = new Bomman(gameMap.getPlayerPos().x, gameMap.getPlayerPos().y);
        bombs = bomman.getBombs();

        enemies = new ArrayList<>();
        switch (level) {
            case 1:
                enemies.add(new Hare(MapController.HARE1_LEVEL_1_X,MapController.HARE1_LEVEL_1_Y));
                enemies.add(new Hare(MapController.HARE2_LEVEL_1_X,MapController.HARE2_LEVEL_1_Y));
                enemies.add(new Hare(MapController.HARE3_LEVEL_1_X,MapController.HARE3_LEVEL_1_Y));

                enemies.add(new Octopus(MapController.OCTOPUS1_LEVEL_1_X,MapController.OCTOPUS1_LEVEL_1_Y));
                enemies.add(new Octopus(MapController.OCTOPUS2_LEVEL_1_X,MapController.OCTOPUS2_LEVEL_1_Y));
                break;
            case 2:
                enemies.add(new Hare(MapController.HARE1_LEVEL_2_X,MapController.HARE1_LEVEL_2_Y));
                enemies.add(new Hare(MapController.HARE2_LEVEL_2_X,MapController.HARE2_LEVEL_2_Y));
                enemies.add(new Hare(MapController.HARE3_LEVEL_2_X,MapController.HARE3_LEVEL_2_Y));

                enemies.add(new Octopus(MapController.OCTOPUS1_LEVEL_2_X,MapController.OCTOPUS1_LEVEL_2_Y));
                enemies.add(new Octopus(MapController.OCTOPUS2_LEVEL_2_X,MapController.OCTOPUS2_LEVEL_2_Y));
                enemies.add(new Octopus(MapController.OCTOPUS3_LEVEL_2_X,MapController.OCTOPUS3_LEVEL_2_Y));
                break;

        }


        playStage = new Stage(new ScreenViewport());

        lifeLabel = new Label("x" + bomman.getLifeLeft(), game.labelStyle);
        lifeLabel.setPosition(64, 440, Align.center);

        timeLabel = new Label("Time: ", game.labelStyle);
        timeLabel.setPosition(200, 440, Align.center);

        deadTexture = new Texture(Gdx.files.internal("ui/img/dead_icon.png"));
        aliveTexture = new Texture(Gdx.files.internal("ui/img/alive_icon.png"));

        playStage.addActor(lifeLabel);
        playStage.addActor(timeLabel);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateTimer();
        elapsedTime += Gdx.graphics.getDeltaTime();

        if (bomman.getCurrentState() == Bomman.State.DYING) {
            if (elapsedTime % 3.0f < 0.05) {
                soundController.getGameMusic().stop();
                game.setScreen(new GameOverScreen(game));
                dispose();
            }
        } else {
            bomman.update(gameMap);
            detectCollisions();
        }
        gameMap.update();
        for (Enemy enemy : enemies) {
            enemy.update(gameMap);
        }

        renderer.getBatch().begin();
        renderer.renderTileLayer(grassLayer.getStaticLayer());
        for (Item item : items) {
            item.render((SpriteBatch) renderer.getBatch());
        }
        renderer.renderTileLayer(wallLayer.getStaticLayer());
        renderer.renderTileLayer(brickLayer.getStaticLayer());
        renderer.getBatch().end();

        game.spriteBatch.begin();
        if (bomman.getCurrentState() == Bomman.State.DYING) {
            game.spriteBatch.draw(deadTexture, 20, 420);
        } else {
            game.spriteBatch.draw(aliveTexture, 20, 420);
        }
        for (Enemy enemy : enemies) {
            enemy.render(game.spriteBatch);
        }
        bomman.render(game.spriteBatch);
        game.spriteBatch.end();

        playStage.act();
        playStage.draw();
    }

    public void updateTimer() {
        int min = (299 - (int) elapsedTime) / 60;
        int sec = elapsedTime % 60 == 0 ? 59 : 59 - (int) elapsedTime % 60;

        String minutesLeft = String.format("%02d", min);
        String secondsLeft = String.format("%02d", sec);

        timeLabel.setText(minutesLeft + ":" + secondsLeft);

        if (min == 0 && sec == 0) {
            soundController.getGameMusic().stop();
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public void detectCollisions() {

        MapObjects collisionObjects = collisionLayer.getObjects();

        checkCollisionPlayerAndMap(collisionObjects);
        if (!bombs.isEmpty()) {
            for (Bomb bomb : bombs) {
                if (bomb.getCurrentState() == Bomb.State.EXPLODING) {
                    ArrayList<Flame> flames = bomb.getFlames();
                    for (Flame flame : flames) {
                        if (!bomman.isInvincible()) {
                            checkCollisionFlamesAndPlayer(flame);
                        }

                        checkCollisionFlamesAndEnemies(flame);

                        checkCollisionFlamesAndBombs(flame);

                        checkCollisionFlamesAndBricks(collisionObjects, flame);
                    }
                }
            }
        }

        checkCollisionPlayerAndItems();

        checkCollisionPlayerAndEnemies();
    }

    public void checkCollisionPlayerAndMap(MapObjects collisionObjects) {
        for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
            if (Intersector.overlaps(collisionObject.getRectangle(), bomman.getBody())) {
                bomman.returnPreviousPos(collisionObject.getRectangle());
            }
        }
    }

    public void checkCollisionPlayerAndEnemies() {
        for (Enemy enemy : enemies) {
            if (Intersector.overlaps(enemy.getBody(), bomman.getBody())) {
                bomman.decreaseLife();
                lifeLabel.setText("x" + bomman.getLifeLeft());
                break;
            }
        }
    }

    public void checkCollisionPlayerAndItems() {
        ArrayList<Item> usedItems = new ArrayList<>();
        for (Item item : items) {
            if (Intersector.overlaps(item.getBody(), bomman.getBody())) {
                soundController.getItemPickUpSound().play();
                item.activate(bomman);
                if (item instanceof LifeItem) {
                    lifeLabel.setText("x" + bomman.getLifeLeft());
                }
                bomman.setItemDuration(Bomman.DEFAULT_PLAYER_ITEM_DURATION);
                bomman.powerUp();
                usedItems.add(item);
            }
        }
        items.removeAll(usedItems);
        usedItems.clear();
    }

    public void checkCollisionFlamesAndPlayer(Flame flame) {
        if (Intersector.overlaps(bomman.getBody(), flame.getBody())) {
            bomman.decreaseLife();
            lifeLabel.setText("x" + bomman.getLifeLeft());
        }
    }

    public void checkCollisionFlamesAndEnemies(Flame flame) {
        ArrayList<Enemy> deadEnemies = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (Intersector.overlaps(flame.getBody(), enemy.getBody())) {
                enemy.setCurrentState(Enemy.State.DYING);
                deadEnemies.add(enemy);
            }
        }
        enemies.removeAll(deadEnemies);
        deadEnemies.clear();
    }

    public void checkCollisionFlamesAndBombs(Flame flame) {
        for (Bomb bomb : bombs) {
            if (Intersector.overlaps(bomb.getBody(), flame.getBody())) {
                bomb.setCurrentState(Bomb.State.EXPLODING);
                bomb.setElapsedTime(Bomb.BOMB_TIME_LIMIT);
            }
        }
    }

    public void checkCollisionFlamesAndBricks(MapObjects collisionObjects, Flame flame) {
        for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
            TiledMapTileLayer brickLayer = (TiledMapTileLayer) gameMap.getTiledMap().getLayers().get("Brick");
            if (Intersector.overlaps(collisionObject.getRectangle(), flame.getBody())
                    && collisionObject.getName().equals("Brick")) {
                Cell brickCell = brickLayer.getCell((int) (collisionObject.getRectangle().getX() / TILE_WIDTH),
                        (int) (collisionObject.getRectangle().getY() / TILE_HEIGHT));
                if (brickCell != null) {
                    brickCell.setTile(null);
                    collisionObjects.remove(collisionObject);
                }
            }
        }
    }

    @Override
    public void dispose() {
        gameMap.getTiledMap().dispose();
        soundController.dispose();
        playStage.dispose();
        aliveTexture.dispose();
        deadTexture.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
