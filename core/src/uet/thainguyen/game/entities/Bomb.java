package uet.thainguyen.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import uet.thainguyen.game.controllers.AnimationController;

public class Bomb {

    private static final float BOMB_WIDTH = 32;
    private static final float BOMB_HEIGHT = 32;

    private enum State {
        NORMAL,
        EXPLODING
    }

    private Rectangle body;
    private float elapsedTime;
    private int power;

    private Animation<TextureRegion> bombAnimation;

    public Bomb(int posX, int posY) {
        body = new Rectangle(posX, posY, BOMB_WIDTH, BOMB_HEIGHT);
        elapsedTime = 0;
        power = 1;
        bombAnimation = AnimationController.loadBombAnimation();
    }

    public void draw(SpriteBatch spriteBatch, float elapsedTime) {
        spriteBatch.draw(bombAnimation.getKeyFrame(elapsedTime), body.getX(), body.getY());
    }
}
