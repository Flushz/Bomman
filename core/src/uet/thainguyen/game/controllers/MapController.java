package uet.thainguyen.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class MapController {

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private static final float PLAYER_RESPAWN_X_LEVEL_1 = 64;
    private static final float PLAYER_RESPAWN_Y_LEVEL_1 = 352;

    private static final float PLAYER_RESPAWN_X_LEVEL_2 = 64;
    private static final float PLAYER_RESPAWN_Y_LEVEL_2 = 320;

    public static final float PORTAL_LEVEL_1_X = 384;
    public static final float PORTAL_LEVEL_1_Y = 32;

    public static final float PORTAL_LEVEL_2_X = 480;
    public static final float PORTAL_LEVEL_2_Y = 64;

    public static final float HARE1_LEVEL_1_X = 64;
    public static final float HARE1_LEVEL_1_Y = 32;

    public static final float HARE2_LEVEL_1_X = 448;
    public static final float HARE2_LEVEL_1_Y = 32;

    public static final float HARE3_LEVEL_1_X = 384;
    public static final float HARE3_LEVEL_1_Y = 352;

    public static final float HARE1_LEVEL_2_X = 224;
    public static final float HARE1_LEVEL_2_Y = 32;

    public static final float HARE2_LEVEL_2_X = 416;
    public static final float HARE2_LEVEL_2_Y = 160;

    public static final float OCTOPUS1_LEVEL_1_X = 64;
    public static final float OCTOPUS1_LEVEL_1_Y = 224;

    public static final float OCTOPUS2_LEVEL_1_X = 64;
    public static final float OCTOPUS2_LEVEL_1_Y = 32;

    public static final float OCTOPUS1_LEVEL_2_X = 64;
    public static final float OCTOPUS1_LEVEL_2_Y = 32;

    public static final float OCTOPUS2_LEVEL_2_X = 320;
    public static final float OCTOPUS2_LEVEL_2_Y = 64;

    public static final float OCTOPUS3_LEVEL_2_X = 448;
    public static final float OCTOPUS3_LEVEL_2_Y = 352;

    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private TiledMapRenderer renderer;

    private Vector2 playerPos;

    public MapController(OrthographicCamera camera, int level) {

        this.camera = camera;

        float viewportWidth = Gdx.graphics.getWidth();
        float viewportHeight = Gdx.graphics.getHeight();

        camera.setToOrtho(false, viewportWidth, viewportHeight);
        camera.update();

        playerPos = new Vector2();

        switch (level) {
            case 1:
                playerPos.set(PLAYER_RESPAWN_X_LEVEL_1, PLAYER_RESPAWN_Y_LEVEL_1);
                tiledMap = new TmxMapLoader().load("map/level_01.tmx");
                break;
            case 2:
                playerPos.set(PLAYER_RESPAWN_X_LEVEL_2, PLAYER_RESPAWN_Y_LEVEL_2);
                tiledMap = new TmxMapLoader().load("map/level_02.tmx");
                break;
        }

        renderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public Vector2 getPlayerPos() {
        return playerPos;
    }

    public float generateX() {
        Random randomX = new Random();
        return randomX.nextInt(16) * TILE_WIDTH;
    }

    public float generateY() {
        Random randomX = new Random();
        return randomX.nextInt(12) * TILE_WIDTH;
    }

    public void setPlayerPos(Vector2 playerPos) {
        this.playerPos = playerPos;
    }

    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public TiledMapRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(TiledMapRenderer renderer) {
        this.renderer = renderer;
    }

    public TiledMap getTiledMap() {
        return this.tiledMap;
    }

    public void update() {
        camera.update();
        renderer.setView(camera);
    }

    public void render() {
        renderer.render();
    }
}
