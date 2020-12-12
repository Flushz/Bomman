package uet.thainguyen.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import uet.thainguyen.game.BommanGame;

import java.io.File;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.addIcon("ui/img/icon_01.jpg", Files.FileType.Internal);
		config.title = "Super Bomman";
		config.width = 544;
		config.height = 480;
		new LwjglApplication(new BommanGame(), config);
	}
}
