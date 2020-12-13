package uet.thainguyen.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import uet.thainguyen.game.controllers.SoundController;
import uet.thainguyen.game.screens.GameMenuScreen;

public class BommanGame extends Game {

	public SpriteBatch spriteBatch;

	public Texture buttonOver;
	public Texture buttonReleased;
	public TextButtonStyle buttonStyle;
	public Label.LabelStyle labelStyle;
	
	@Override
	public void create () {
		spriteBatch = new SpriteBatch();

		buttonOver = new Texture(Gdx.files.internal("ui/buttons/play_over.png"));
		buttonReleased = new Texture(Gdx.files.internal("ui/buttons/play_up.png"));

		buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.over = new TextureRegionDrawable(new TextureRegion(buttonOver));
		buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonReleased));
		buttonStyle.font = generateBitmapFont();
		buttonStyle.fontColor = Color.valueOf("ffffff");
		buttonStyle.overFontColor = Color.valueOf("583d72");

		labelStyle = new Label.LabelStyle();
		labelStyle.font = generateBitmapFont();
		labelStyle.fontColor = Color.valueOf("ffffff");

		setScreen(new GameMenuScreen(this));
	}

	public BitmapFont generateBitmapFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/PressStart2P-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 12;
		BitmapFont menuFont = generator.generateFont(parameter);
		generator.dispose();
		return menuFont;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}
	
	@Override
	public void dispose () {
		spriteBatch.dispose();
		buttonOver.dispose();
		buttonReleased.dispose();
	}
}
