package uet.thainguyen.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uet.thainguyen.game.controllers.MapController;
import uet.thainguyen.game.entities.Player;

public class PlayScreen implements Screen {

    private static final float PLAYER_RESPAWN_X = 0;
    private static final float PLAYER_RESPAWN_Y = 0;
    private static final float PLAYER_WIDTH = 32;
    private static final float PLAYER_HEIGHT = 32;
    private static final float PLAYER_SPEED = 2;

    SpriteBatch spriteBatch;
    OrthographicCamera camera;
    MapController gameMap;
    Player player;

    private float elapsedTime = 0;

    public PlayScreen() {
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        gameMap = new MapController(camera);
        player = new Player(PLAYER_RESPAWN_X, PLAYER_RESPAWN_Y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_SPEED);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += Gdx.graphics.getDeltaTime();

        player.update();

        gameMap.render();
        spriteBatch.begin();
        player.render(spriteBatch, elapsedTime);
        spriteBatch.end();
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
