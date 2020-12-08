package uet.thainguyen.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import uet.thainguyen.game.controllers.AnimationController;

public class Bomb {

    private static final float BOMB_WIDTH = 32;
    private static final float BOMB_HEIGHT = 32;
    private static final float BOMB_TIME_LIMIT = 2;

    public enum State {
        ACTIVATED,
        EXPLODING
    }

    public enum BlockMode {
        ON, OFF
    }

    private Rectangle body;
    private State currentState;
    private BlockMode currentMode;
    private float elapsedTime;
    private int power;

    private Animation<TextureRegion> bombAnimation;

    public Bomb(int posX, int posY) {
        body = new Rectangle(posX, posY, BOMB_WIDTH, BOMB_HEIGHT);
        setCurrentState(State.ACTIVATED);
        setCurrentMode(BlockMode.OFF);
        elapsedTime = 0;
        power = 1;
        bombAnimation = AnimationController.loadBombAnimation();
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public BlockMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(BlockMode currentMode) {
        this.currentMode = currentMode;
    }

    public Rectangle getBody() {
        return this.body;
    }

    public void checkState() {
        this.elapsedTime += Gdx.graphics.getDeltaTime();

        if (this.elapsedTime > BOMB_TIME_LIMIT) {
            setCurrentState(State.EXPLODING);
        }
    }

    public void draw(SpriteBatch spriteBatch, float elapsedTime) {
        if (currentState == State.ACTIVATED) {
            spriteBatch.draw(bombAnimation.getKeyFrame(elapsedTime), body.getX(), body.getY());
        }
    }
}
