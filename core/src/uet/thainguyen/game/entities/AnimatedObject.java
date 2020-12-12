package uet.thainguyen.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import uet.thainguyen.game.controllers.MapController;

import java.util.HashMap;

public abstract class AnimatedObject extends GameObject{

    private final HashMap<String, Animation<TextureRegion>> animationSet;
    private float elapsedTime;

    public AnimatedObject(float posX, float posY, float width, float height) {
        super(posX, posY, width, height);
        this.animationSet = new HashMap<>();
        this.elapsedTime = 0;
    }

    public HashMap<String, Animation<TextureRegion>> getAnimationSet() {
        return animationSet;
    }

    public float getElapsedTime() {
        return this.elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void updateElapsedTime() {
        this.elapsedTime += Gdx.graphics.getDeltaTime();
    }

    public abstract void render(SpriteBatch spriteBatch);
}
