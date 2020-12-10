package uet.thainguyen.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import uet.thainguyen.game.controllers.MapController;
import uet.thainguyen.game.entities.Bomb;
import uet.thainguyen.game.entities.Flame;
import uet.thainguyen.game.entities.Hare;
import uet.thainguyen.game.entities.Player;

import java.util.ArrayList;

public class PlayScreen implements Screen {

    private static int TILE_WIDTH = 32;
    private static int TILE_HEIGHT = 32;

    SpriteBatch spriteBatch;
    OrthographicCamera camera;
    MapController gameMap;
    MapLayer collisionLayer;
    Hare hare;
    Player player;
    ArrayList<Bomb> bombs;

    private float elapsedTime = 0;

    public PlayScreen() {
        spriteBatch = new SpriteBatch();

        camera = new OrthographicCamera();
        gameMap = new MapController(camera);
        collisionLayer = gameMap.getTiledMap().getLayers().get(0);
        hare = new Hare();
        player = new Player();
        bombs = player.getBombs();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += Gdx.graphics.getDeltaTime();

        hare.update();
        player.update(gameMap);

        detectCollisions();

        spriteBatch.begin();
        gameMap.render();
        hare.render(spriteBatch, elapsedTime);
        player.render(spriteBatch, elapsedTime);
        spriteBatch.end();
    }

    public void detectCollisions() {

        MapObjects collisionObjects = collisionLayer.getObjects();
        for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
            if (Intersector.overlaps(collisionObject.getRectangle(), player.getBody())) {
                player.returnPreviousPos(collisionObject.getRectangle());
            }
        }

        //Detect collision between flames and player
        if (!bombs.isEmpty()) {
            for (Bomb bomb : bombs) {
                if (bomb.getCurrentState() == Bomb.State.EXPLODING) {
                    ArrayList<Flame> flames = bomb.getFlames();
                    for (Flame flame : flames) {
                        if (Intersector.overlaps(player.getBody(), flame.getBody())) {
                            player.setCurrentState(Player.State.DYING);
                        }

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
