package uet.thainguyen.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class SoundController implements Disposable {

    private final Music menuScreenMusic;
    private final Music gameMusic;
    private final Music gameOverMusic;

    private final Sound menuOpenSound;
    private final Sound menuSelectSound;
    private final Sound menuOverSound;

    private final Sound bombDropSound;
    private final Sound bombExplodeSound;

    private final Sound itemPickUpSound;

    private final Sound playerDieSound;
    private final Sound playerWinSound;

    public SoundController() {
        menuScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu-music-01.mp3"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/play-music-01.mp3"));
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("music/lose_game.mp3"));

        menuOpenSound = Gdx.audio.newSound(Gdx.files.internal("sounds/menu_open.wav"));
        menuSelectSound = Gdx.audio.newSound(Gdx.files.internal("sounds/menu_select.ogg"));
        menuOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/menu_move.ogg"));

        bombDropSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bomb_drop.ogg"));
        bombExplodeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bomb_explode1.ogg"));

        itemPickUpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/item_pickup.ogg"));

        playerDieSound = Gdx.audio.newSound(Gdx.files.internal("sounds/player_die.ogg"));
        playerWinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/player_win.ogg"));
    }

    public Music getGameMusic() {
        return this.gameMusic;
    }

    public Music getMenuScreenMusic() {
        return this.menuScreenMusic;
    }

    public Music getGameOverMusic() {
        return this.gameOverMusic;
    }

    public Sound getMenuOpenSound() {
        return this.menuOpenSound;
    }

    public Sound getMenuSelectSound() {
        return this.menuSelectSound;
    }

    public Sound getMenuOverSound() {
        return this.menuOverSound;
    }

    public Sound getBombDropSound() {
        return this.bombDropSound;
    }

    public Sound getBombExplodeSound() {
        return this.bombExplodeSound;
    }

    public Sound getItemPickUpSound() {
        return this.itemPickUpSound;
    }

    public Sound getPlayerDieSound() {
        return this.playerDieSound;
    }

    public Sound getPlayerWinSound() {
        return this.playerWinSound;
    }

    @Override
    public void dispose() {
        menuScreenMusic.dispose();
        gameMusic.dispose();

        menuOpenSound.dispose();
        menuSelectSound.dispose();
        menuOverSound.dispose();

        bombDropSound.dispose();
    }
}
