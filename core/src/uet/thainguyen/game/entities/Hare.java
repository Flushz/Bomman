package uet.thainguyen.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uet.thainguyen.game.controllers.AnimationController;

public class Hare extends MovableObject{

    private enum State {
        WALKING_UP, WALKING_DOWN, WALKING_RIGHT, WALKING_LEFT,
        DYING
    }

    private State currentState;

    public Hare(float posX, float posY, float width, float height, float speed) {
        super(posX, posY, width, height, speed);
        AnimationController.loadHareAnimations(getAnimationSet());
        currentState = State.WALKING_LEFT;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public void render(SpriteBatch spriteBatch, float elapsedTime) {
        spriteBatch.draw(getAnimationSet().get("walking_right").getKeyFrame(elapsedTime), getX(), getY());
    }
}
