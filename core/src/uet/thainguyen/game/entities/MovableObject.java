package uet.thainguyen.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;

public abstract class MovableObject {

    protected Rectangle body;
    protected HashMap<String, Animation<TextureRegion>> animations;
    protected float speed;

    public MovableObject(float posX, float posY, float width, float height, float speed) {
        this.body = new Rectangle(posX, posY, width, height);
        this.animations = new HashMap<>();
        this.speed = speed;
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

    public float getWidth() {
        return body.getWidth();
    }

    public float getHeight() {
        return body.getHeight();
    }

    public void setX(float x) {
        body.setX(x);
    }

    public void setY(float y) {
        body.setY(y);
    }
}
