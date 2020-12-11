package uet.thainguyen.game.entities.dynamics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import uet.thainguyen.game.controllers.AnimationController;
import uet.thainguyen.game.controllers.MapController;

public class Hare extends DynamicObject {

    private static final float HARE_RESPAWN_X = 96;
    private static final float HARE_RESPAWN_Y = 32;
    private static final float HARE_WIDTH = 32;
    private static final float HARE_HEIGHT = 32;
    private static final int HARE_SPEED = 1;

    public enum State {
        WALKING_UP, WALKING_DOWN, WALKING_RIGHT, WALKING_LEFT,
        DYING
    }

    private State currentState;
    private Rectangle boundingBox;

    public Hare() {
        super(HARE_RESPAWN_X, HARE_RESPAWN_Y, HARE_WIDTH, HARE_HEIGHT, HARE_SPEED);
        this.boundingBox = new Rectangle(64, 32, 128, 32);
        AnimationController.loadHareAnimations(getAnimationSet());
        currentState = State.WALKING_LEFT;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    @Override
    public void moveUp() {
        setY(getY() + getSpeed());
    }

    @Override
    public void moveDown() {
        setY(getY() - getSpeed());
    }

    @Override
    public void moveRight() {
        setX(getX() + getSpeed());
    }

    @Override
    public void moveLeft() {
        setX(getX() - getSpeed());
    }

    @Override
    public void update(MapController gameMap) {
        updateElapsedTime();
        if (getX() == boundingBox.getX()) {
            setCurrentState(State.WALKING_RIGHT);
        } else if (getX() + getWidth() == boundingBox.getX() + boundingBox.getWidth()) {
            setCurrentState(State.WALKING_LEFT);
        }

        if (currentState == State.WALKING_RIGHT) {
            moveRight();
        } else {
            moveLeft();
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        TextureRegion frame = getAnimationSet().get("walking_down").getKeyFrame(getElapsedTime());
        switch (currentState) {
            case WALKING_UP:
                frame = getAnimationSet().get("walking_up").getKeyFrame(getElapsedTime());
                break;
            case WALKING_DOWN:
                frame = getAnimationSet().get("walking_down").getKeyFrame(getElapsedTime());
                break;
            case WALKING_RIGHT:
                frame = getAnimationSet().get("walking_right").getKeyFrame(getElapsedTime());
                break;
            case WALKING_LEFT:
                frame = getAnimationSet().get("walking_left").getKeyFrame(getElapsedTime());
                break;
        }
        spriteBatch.draw(frame,getX(), getY());
    }
}
