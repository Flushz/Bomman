package uet.thainguyen.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import uet.thainguyen.game.controllers.AnimationController;

import java.util.HashMap;

public class Flame {

    private static final int FLAME_WIDTH = 32;
    private static final int FLAME_HEIGHT = 32;
    private static final String CENTER_KEY = "center";
    private static final String VERTICAL_KEY = "vertical";
    private static final String HORIZONTAL_KEY = "horizontal";

    public enum Position {
        CENTER,
        VERTICAL,
        HORIZONTAL
    }

    private Rectangle body;
    private Position position;
    private float elapsedTime;
    private boolean isFinished;

    private HashMap<String, Animation<TextureRegion>> animationSet;

    public Flame(float posX, float posY, Position position) {
        this.body = new Rectangle(posX,posY, FLAME_WIDTH, FLAME_HEIGHT);
        this.position = position;
        this.animationSet = new HashMap<>();
        this.elapsedTime = 0;
        this.isFinished = false;
        AnimationController.loadFlameAnimations(this.animationSet);
    }

    public Rectangle getBody() {
        return body;
    }

    public void setBody(Rectangle body) {
        this.body = body;
    }

    public void draw(SpriteBatch spriteBatch) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        switch (position) {
            case CENTER:
                spriteBatch.draw(animationSet.get(CENTER_KEY).getKeyFrame(elapsedTime), body.getX(), body.getY());
                if (animationSet.get(CENTER_KEY).isAnimationFinished(elapsedTime)) {
                    isFinished = true;
                }
                break;
            case VERTICAL:
                spriteBatch.draw(animationSet.get(VERTICAL_KEY).getKeyFrame(elapsedTime), body.getX(), body.getY());
                if (animationSet.get(VERTICAL_KEY).isAnimationFinished(elapsedTime)) {
                    isFinished = true;
                }
                break;
            case HORIZONTAL:
                spriteBatch.draw(animationSet.get(HORIZONTAL_KEY).getKeyFrame(elapsedTime), body.getX(), body.getY());
                if (animationSet.get(HORIZONTAL_KEY).isAnimationFinished(elapsedTime)) {
                    isFinished = true;
                }
                break;
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}
