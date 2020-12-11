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
import uet.thainguyen.game.entities.explosion.Bomb;
import uet.thainguyen.game.entities.explosion.Flame;
import uet.thainguyen.game.entities.statics.BrickLayer;
import uet.thainguyen.game.entities.statics.GrassLayer;
import uet.thainguyen.game.entities.statics.WallLayer;
import uet.thainguyen.game.entities.dynamics.Hare;
import uet.thainguyen.game.entities.dynamics.Bomman;
import uet.thainguyen.game.entities.items.SpeedItem;

import java.util.ArrayList;

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

    Hare hare;
    Bomman bomman;
    SpeedItem speedItem;
    ArrayList<Bomb> bombs;

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

        speedItem = new SpeedItem(0,0);

        hare = new Hare();
        bomman = new Bomman();
        bombs = bomman.getBombs();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += Gdx.graphics.getDeltaTime();

        gameMap.update();
        hare.update(gameMap);
        bomman.update(gameMap);

        detectCollisions();

        renderer.getBatch().begin();
        renderer.renderTileLayer(grassLayer.getStaticLayer());
        renderer.renderTileLayer(wallLayer.getStaticLayer());
        speedItem.render((SpriteBatch) renderer.getBatch());
        renderer.renderTileLayer(brickLayer.getStaticLayer());
        renderer.getBatch().end();

        spriteBatch.begin();
        hare.render(spriteBatch);
        bomman.render(spriteBatch);
        spriteBatch.end();
    }

    public void detectCollisions() {

        MapObjects collisionObjects = collisionLayer.getObjects();
        for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
            if (Intersector.overlaps(collisionObject.getRectangle(), bomman.getBody())) {
                bomman.returnPreviousPos(collisionObject.getRectangle());
            }
        }

        //Detect collision between flames and player
        if (!bombs.isEmpty()) {
            for (Bomb bomb : bombs) {
                if (bomb.getCurrentState() == Bomb.State.EXPLODING) {
                    ArrayList<Flame> flames = bomb.getFlames();
                    for (Flame flame : flames) {
                        if (Intersector.overlaps(bomman.getBody(), flame.getBody())) {
                            bomman.setCurrentState(Bomman.State.DYING);
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
