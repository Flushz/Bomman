package uet.thainguyen.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import uet.thainguyen.game.controllers.MapController;
import uet.thainguyen.game.entities.dynamics.DynamicObject;
import uet.thainguyen.game.entities.dynamics.Enemy;
import uet.thainguyen.game.entities.explosion.Bomb;
import uet.thainguyen.game.entities.explosion.Flame;
import uet.thainguyen.game.entities.items.*;
import uet.thainguyen.game.entities.statics.BrickLayer;
import uet.thainguyen.game.entities.statics.GrassLayer;
import uet.thainguyen.game.entities.statics.WallLayer;
import uet.thainguyen.game.entities.dynamics.Hare;
import uet.thainguyen.game.entities.dynamics.Bomman;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayScreen implements Screen {

    private static int TILE_WIDTH = 32;
    private static int TILE_HEIGHT = 32;

    SpriteBatch spriteBatch;

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
    ArrayList<Float> itemDuration;

    private float elapsedTime = 0;

    public PlayScreen() {
        spriteBatch = new SpriteBatch();

        camera = new OrthographicCamera();
        gameMap = new MapController(camera);
        renderer = new OrthogonalTiledMapRenderer(gameMap.getTiledMap());
        renderer.setView(camera);
        collisionLayer = gameMap.getTiledMap().getLayers().get(0);
        grassLayer = new GrassLayer(gameMap.getTiledMap());
        wallLayer = new WallLayer(gameMap.getTiledMap());
        brickLayer = new BrickLayer(gameMap.getTiledMap());

        items = new ArrayList<>();
        items.add(new SlowItem(64, 128));
        items.add(new SpeedItem(64, 64));
        items.add(new SpeedItem(64, 32));
        items.add(new BombBagItem(64, 160));
        items.add(new SuperBombItem(96, 160));

        bomman = new Bomman();
        bombs = bomman.getBombs();

        enemies = new ArrayList<>();
        enemies.add(new Hare());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += Gdx.graphics.getDeltaTime();

        gameMap.update();
        for (Enemy enemy : enemies) {
            enemy.update(gameMap);
        }
        bomman.update(gameMap);

        detectCollisions();

        renderer.getBatch().begin();
        renderer.renderTileLayer(grassLayer.getStaticLayer());
        renderer.renderTileLayer(wallLayer.getStaticLayer());
        for (Item item : items) {
            item.render((SpriteBatch) renderer.getBatch());
        }
        renderer.renderTileLayer(brickLayer.getStaticLayer());
        renderer.getBatch().end();

        spriteBatch.begin();
        for (Enemy enemy : enemies) {
            enemy.render(spriteBatch);
        }
        bomman.render(spriteBatch);
        spriteBatch.end();
    }

    public void detectCollisions() {

        MapObjects collisionObjects = collisionLayer.getObjects();

        checkCollisionPlayerAndMap(collisionObjects);

        if (!bombs.isEmpty()) {
            for (Bomb bomb : bombs) {
                if (bomb.getCurrentState() == Bomb.State.EXPLODING) {
                    ArrayList<Flame> flames = bomb.getFlames();
                    for (Flame flame : flames) {
                        checkCollisionFlamesAndPlayer(flame);

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
                bomman.setCurrentState(Bomman.State.DYING);
                break;
            }
        }
    }

    public void checkCollisionPlayerAndItems() {
        ArrayList<Item> usedItems = new ArrayList<>();
        for (Item item : items) {
            if (Intersector.overlaps(item.getBody(), bomman.getBody())) {
                item.activate(bomman);
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
            bomman.setCurrentState(Bomman.State.DYING);
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
        spriteBatch.dispose();
        gameMap.getTiledMap().dispose();
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
