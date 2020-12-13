package uet.thainguyen.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uet.thainguyen.game.BommanGame;
import uet.thainguyen.game.controllers.SoundController;

public class GameMenuScreen extends InputAdapter implements Screen {

    BommanGame game;

    SoundController soundController;

    Stage menuStage;

    Texture backgroundTexture;
    TextButton playButton;
    TextButton exitButton;

    public GameMenuScreen(final BommanGame game) {
        this.game = game;

        soundController = new SoundController();
        soundController.getMenuOpenSound().play();
        soundController.getMenuScreenMusic().play();
        soundController.getMenuScreenMusic().setLooping(true);

        menuStage = new Stage(new ScreenViewport());

        backgroundTexture = new Texture(Gdx.files.internal("ui/img/background.png"));

        playButton = new TextButton("Play", game.buttonStyle);
        playButton.setPosition(350, 180);
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                soundController.getMenuScreenMusic().stop();
                game.setScreen(new GamePlayScreen(game, 1));
                dispose();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        exitButton = new TextButton("Exit", game.buttonStyle);
        exitButton.setPosition(350, 106);
        exitButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        menuStage.addActor(playButton);
        menuStage.addActor(exitButton);

        Gdx.input.setInputProcessor(menuStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.spriteBatch.begin();
        game.spriteBatch.draw(backgroundTexture, 0, 0);
        game.spriteBatch.end();

        menuStage.act();
        menuStage.draw();
    }

    @Override
    public void dispose() {
        soundController.dispose();
        backgroundTexture.dispose();
        menuStage.dispose();
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
