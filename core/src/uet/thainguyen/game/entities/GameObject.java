package uet.thainguyen.game.entities;

import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {

    private final Rectangle body;

    public GameObject(float posX, float posY, float width, float height) {
        body = new Rectangle(posX, posY, width, height);
    }

    public Rectangle getBody() {
        return body;
    }

    public float getX() {
        return body.getX();
    }

    public float getY() {
        return body.getY();
    }

    public void setX(float x) {
        body.setX(x);
    }

    public void setY(float y) {
        body.setY(y);
    }

    public float getWidth() {
        return body.getWidth();
    }

    public float getHeight() {
        return body.getHeight();
    }
}
