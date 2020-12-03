package uet.thainguyen.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uet.thainguyen.game.controllers.AnimationController;

public class Player extends MovableObject {

    private enum State {
        WALKING_UP,
        WALKING_DOWN,
        WALKING_RIGHT,
        WALKING_LEFT,
        IDLING_UP,
        IDLING_DOWN,
        IDLING_RIGHT,
        IDLING_LEFT,
        DYING
    }

    private State currentState;

    public Player(float posX, float posY, float width, float height, float speed) {
        super(posX, posY, width, height, speed);
        AnimationController.loadPlayerAnimation(animations);
        currentState = State.IDLING_DOWN;
    }

    public void moveUp() {
        setY(getY() + speed);
    }

    public void moveDown() {
        setY(getY() - speed);
    }
    public void moveRight() {
        setX(getX() + speed);
    }
    public void moveLeft() {
        setX(getX() - speed);
    }

    public void update() {
        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveUp();
        }
    }

    public void render(SpriteBatch spriteBatch, float elapsedTime) {
        spriteBatch.draw(animations.get("walking_left").getKeyFrame(elapsedTime), getX(), getY());
    }
}
