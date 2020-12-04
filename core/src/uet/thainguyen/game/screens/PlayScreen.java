package uet.thainguyen.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import uet.thainguyen.game.controllers.MapController;
import uet.thainguyen.game.entities.Player;

public class PlayScreen implements Screen {

    private static final float PLAYER_RESPAWN_X = 64;
    private static final float PLAYER_RESPAWN_Y = 32;
    private static final float PLAYER_WIDTH = 32;
    private static final float PLAYER_HEIGHT = 32;
    private static final float PLAYER_SPEED = 4;

    SpriteBatch spriteBatch;
    OrthographicCamera camera;
    MapController gameMap;
    MapLayer collisionLayer;
    Player player;

    private float elapsedTime = 0;

    public PlayScreen() {
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        gameMap = new MapController(camera);
        collisionLayer = gameMap.getTiledMap().getLayers().get(3);
        player = new Player(PLAYER_RESPAWN_X, PLAYER_RESPAWN_Y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_SPEED);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += Gdx.graphics.getDeltaTime();

        player.update();

        detectCollisions();

        spriteBatch.begin();
        gameMap.render();
        player.render(spriteBatch, elapsedTime);
        spriteBatch.end();
    }

    public void detectCollisions() {

        MapObjects collisionObjects = collisionLayer.getObjects();
        for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
            if (Intersector.overlaps(collisionObject.getRectangle(), player.getBody())) {
                player.returnPreviousPos();
            }
        }
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
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
