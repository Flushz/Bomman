package uet.thainguyen.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class MapController {

    private static final float PLAYER_RESPAWN_X = 64;
    private static final float PLAYER_RESPAWN_Y = 352;

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

        switch (level) {
            case 1:
            case 2:
                tiledMap = new TmxMapLoader().load("map/level_2.tmx");
                break;
        }

        renderer = new OrthogonalTiledMapRenderer(tiledMap);
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
