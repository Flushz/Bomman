package uet.thainguyen.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapController {

    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private TiledMapRenderer renderer;

    public MapController(OrthographicCamera camera) {

        this.camera = camera;

        float viewportWidth = Gdx.graphics.getWidth();
        float viewportHeight = Gdx.graphics.getHeight();

        camera.setToOrtho(false, viewportWidth, viewportHeight);
        camera.update();

        tiledMap = new TmxMapLoader().load("map/level_2.tmx");

        renderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void render() {
        camera.update();
        renderer.setView(camera);
        renderer.render();
    }
}
