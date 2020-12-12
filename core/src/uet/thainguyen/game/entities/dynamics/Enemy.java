package uet.thainguyen.game.entities.dynamics;

public abstract class Enemy extends DynamicObject{

    public enum State {
        WALKING_UP, WALKING_DOWN, WALKING_RIGHT, WALKING_LEFT,
        DYING
    }

    private State currentState;

    public Enemy(float posX, float posY, float width, float height, int speed) {
        super(posX, posY, width, height, speed);
        this.currentState = State.WALKING_LEFT;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getCurrentState() {
        return this.currentState;
    }
}
