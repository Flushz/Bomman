package uet.thainguyen.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import uet.thainguyen.game.controllers.AnimationController;

import java.util.HashMap;

public class Flame {

    private static final int FLAME_WIDTH = 32;
    private static final int FLAME_HEIGHT = 32;
    public enum Position {
        CENTER,
        VERTICAL,
        HORIZONTAL
    }

    private Rectangle body;
    private Position position;

    private HashMap<String, Animation<TextureRegion>> animationSet;

    public Flame(float posX, float posY, Position position) {
        this.body = new Rectangle(posX,posY, FLAME_WIDTH, FLAME_HEIGHT);
        this.position = position;
        this.animationSet = new HashMap<>();
        AnimationController.loadFlameAnimations(this.animationSet);
    }

    public Rectangle getBody() {
        return body;
    }

    public void setBody(Rectangle body) {
        this.body = body;
    }

    public void draw(SpriteBatch spriteBatch, float elapsedTime) {
        switch (position) {
            case CENTER:
                spriteBatch.draw(animationSet.get("center").getKeyFrame(elapsedTime), body.getX(), body.getY());
                break;
            case VERTICAL:
                spriteBatch.draw(animationSet.get("vertical").getKeyFrame(elapsedTime), body.getX(), body.getY());
                break;
            case HORIZONTAL:
                spriteBatch.draw(animationSet.get("horizontal").getKeyFrame(elapsedTime), body.getX(), body.getY());
                break;
        }
    }
}
