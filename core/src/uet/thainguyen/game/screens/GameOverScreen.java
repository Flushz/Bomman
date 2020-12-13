package uet.thainguyen.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uet.thainguyen.game.BommanGame;


public class GameOverScreen implements Screen {

    BommanGame game;

    Stage gameOverStage;
    Label gameOverLabel;

    TextButton playAgainButton;
    TextButton backToMenuButton;
    TextButton exitButton;

    public GameOverScreen(final BommanGame game) {

        this.game = game;
        gameOverStage = new Stage(new ScreenViewport());

        gameOverLabel = new Label("You lose :(", game.labelStyle);
        gameOverLabel.setPosition(Gdx.graphics.getWidth() / 2.0f - 50, Gdx.graphics.getHeight() / 2.0f);
        gameOverLabel.setAlignment(Align.center);
        gameOverLabel.setFontScale(3);

        playAgainButton = new TextButton("Again", game.buttonStyle);
        playAgainButton.setPosition(Gdx.graphics.getWidth() / 2.0f - 50, Gdx.graphics.getHeight() / 2.0f - 100);
        playAgainButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GamePlayScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        backToMenuButton = new TextButton("Back to Menu", game.buttonStyle);
        backToMenuButton.setPosition(Gdx.graphics.getWidth() / 2.0f - 50, Gdx.graphics.getHeight() / 2.0f - 164);
        backToMenuButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameMenuScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        exitButton = new TextButton("Exit", game.buttonStyle);
        exitButton.setPosition(Gdx.graphics.getWidth() / 2.0f - 50, Gdx.graphics.getHeight() / 2.0f - 228);
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

        gameOverStage.addActor(gameOverLabel);
        gameOverStage.addActor(playAgainButton);
        gameOverStage.addActor(backToMenuButton);
        gameOverStage.addActor(exitButton);

        Gdx.input.setInputProcessor(gameOverStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameOverStage.act();
        gameOverStage.draw();
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

    @Override
    public void dispose() {
        game.spriteBatch.dispose();
        gameOverStage.dispose();
    }
}
