package uet.thainguyen.game.entities.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uet.thainguyen.game.controllers.AnimationController;

public class SpeedItem extends Item{

    private static final String SPEED_ITEM_KEY = "speed_item";

    public SpeedItem(float posX, float posY) {
        super(posX, posY);
        AnimationController.loadItemAnimations(getAnimationSet(), SPEED_ITEM_KEY);
    }

    @Override
    public void activate() {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        updateElapsedTime();
        spriteBatch.draw(getAnimationSet().get(ITEM_BACKGROUND_KEY).getKeyFrame(getElapsedTime()), getX(), getY());
        spriteBatch.draw(getAnimationSet().get(ITEM_IMAGE_KEY).getKeyFrame(getElapsedTime()), getX(), getY());
    }
}
