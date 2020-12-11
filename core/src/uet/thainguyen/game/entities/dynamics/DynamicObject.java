package uet.thainguyen.game.entities.dynamics;

import uet.thainguyen.game.controllers.MapController;
import uet.thainguyen.game.entities.AnimatedObject;

public abstract class DynamicObject extends AnimatedObject {

    private int speed;

    public DynamicObject(float posX, float posY, float width, float height, int speed) {
        super(posX, posY, width, height);
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed += speed;
    }

    public abstract void update(MapController gameMap);

    public abstract void moveUp();

    public abstract void moveDown();

    public abstract void moveRight();

    public abstract void moveLeft();
}
