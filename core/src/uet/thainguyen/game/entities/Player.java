package uet.thainguyen.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import uet.thainguyen.game.controllers.AnimationController;

public class Player extends MovableObject {

    private enum State {
        WALKING_UP, WALKING_DOWN, WALKING_RIGHT, WALKING_LEFT,
        IDLING_UP, IDLING_DOWN, IDLING_RIGHT, IDLING_LEFT,
        DYING
    }

    private State currentState;
    private boolean isMoving;

    public Player(float posX, float posY, float width, float height, float speed) {
        super(posX, posY, width, height, speed);
        AnimationController.loadPlayerAnimation(getAnimationSet());
        currentState = State.IDLING_UP;
        isMoving = false;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public void moveUp() {
        setY(getY() + getSpeed());
    }

    public void moveDown() {
        setY(getY() - getSpeed());
    }

    public void moveRight() {
        setX(getX() + getSpeed());
    }

    public void moveLeft() {
        setX(getX() - getSpeed());
    }

    public void update() {
        handleInput();
        checkState();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            isMoving = true;

            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                setCurrentState(State.WALKING_UP);
                moveUp();
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                setCurrentState(State.WALKING_DOWN);
                moveDown();
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                setCurrentState(State.WALKING_RIGHT);
                moveRight();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                setCurrentState(State.WALKING_LEFT);
                moveLeft();
            }
        } else {
            isMoving = false;
        }
    }

    private void checkState() {
        if (!isMoving) {
            switch (currentState) {
                case WALKING_UP:
                    setCurrentState(State.IDLING_UP);
                    break;
                case WALKING_DOWN:
                    setCurrentState(State.IDLING_DOWN);
                    break;
                case WALKING_RIGHT:
                    setCurrentState(State.IDLING_RIGHT);
                    break;
                case WALKING_LEFT:
                    setCurrentState(State.IDLING_LEFT);
                    break;
            }
        }
    }

    //if the player is colliding with a static object, then return him to his previous position
    public void returnPreviousPos() {
        switch (currentState) {
            case WALKING_UP:
                moveDown();
                break;
            case WALKING_DOWN:
                moveUp();
                break;
            case WALKING_RIGHT:
                moveLeft();
                break;
            case WALKING_LEFT:
                moveRight();
                break;
        }
    }

    public void render(SpriteBatch spriteBatch, float elapsedTime) {
        TextureRegion frame = getAnimationSet().get("idling_down").getKeyFrame(elapsedTime);
        switch (currentState) {
            case WALKING_UP:
                frame = getAnimationSet().get("walking_up").getKeyFrame(elapsedTime);
                break;
            case WALKING_DOWN:
                frame = getAnimationSet().get("walking_down").getKeyFrame(elapsedTime);
                break;
            case WALKING_RIGHT:
                frame = getAnimationSet().get("walking_right").getKeyFrame(elapsedTime);
                break;
            case WALKING_LEFT:
                frame = getAnimationSet().get("walking_left").getKeyFrame(elapsedTime);
                break;
            case IDLING_UP:
                frame = getAnimationSet().get("idling_up").getKeyFrame(elapsedTime);
                break;
            case IDLING_DOWN:
                frame = getAnimationSet().get("idling_down").getKeyFrame(elapsedTime);
                break;
            case IDLING_RIGHT:
                frame = getAnimationSet().get("idling_right").getKeyFrame(elapsedTime);
                break;
            case IDLING_LEFT:
                frame = getAnimationSet().get("idling_left").getKeyFrame(elapsedTime);
                break;
        }
        spriteBatch.draw(frame,getX(), getY());
    }
}
