package uet.thainguyen.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uet.thainguyen.game.BommanGame;
import uet.thainguyen.game.controllers.SoundController;


public class GameOverScreen implements Screen {

    BommanGame game;

    Stage gameOverStage;

    SoundController soundController;

    Label gameOverLabel;

    Texture gameOverBackground;
    TextButton playAgainButton;
    TextButton backToMenuButton;
    TextButton exitButton;

    public GameOverScreen(final BommanGame game) {

        this.game = game;

        soundController = new SoundController();
        soundController.getGameOverMusic().play();

        gameOverStage = new Stage(new ScreenViewport());

        gameOverBackground = new Texture(Gdx.files.internal("ui/img/game_over_background_00.png"));

        game.labelStyle.fontColor = Color.valueOf("ffffff");
        gameOverLabel = new Label("You lose :(", game.labelStyle);
        gameOverLabel.setPosition(Gdx.graphics.getWidth() / 2.0f - 50, Gdx.graphics.getHeight() / 2.0f + 60);
        gameOverLabel.setAlignment(Align.center);
        gameOverLabel.setFontScale(3);

        playAgainButton = new TextButton("Try again", game.buttonStyle);
        playAgainButton.setPosition(Gdx.graphics.getWidth() / 2.0f - 50, Gdx.graphics.getHeight() / 2.0f - 50);
        playAgainButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GamePlayScreen(game, 1));
                dispose();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        backToMenuButton = new TextButton("Menu", game.buttonStyle);
        backToMenuButton.setPosition(Gdx.graphics.getWidth() / 2.0f - 50, Gdx.graphics.getHeight() / 2.0f - 114);
        backToMenuButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                soundController.getMenuScreenMusic().stop();
                game.setScreen(new GameMenuScreen(game));
                dispose();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        exitButton = new TextButton("Exit", game.buttonStyle);
        exitButton.setPosition(Gdx.graphics.getWidth() / 2.0f - 50, Gdx.graphics.getHeight() / 2.0f - 178);
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
        Gdx.gl.glClearColor(255, 202, 123, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.spriteBatch.begin();
        game.spriteBatch.draw(gameOverBackground, 0, 0);
        game.spriteBatch.end();

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
        soundController.dispose();
        gameOverStage.dispose();
    }
}
