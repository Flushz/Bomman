package uet.thainguyen.game.entities.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uet.thainguyen.game.controllers.AnimationController;
import uet.thainguyen.game.entities.AnimatedObject;
import uet.thainguyen.game.entities.dynamics.Bomman;

public abstract class Item extends AnimatedObject {

    protected static final int ITEM_WIDTH = 32;
    protected static final int ITEM_HEIGHT = 32;
    protected static final String ITEM_BACKGROUND_KEY = "background";
    protected static final String ITEM_IMAGE_KEY = "image";

    public Item(float posX, float posY, String itemKey) {
        super(posX, posY, ITEM_WIDTH, ITEM_HEIGHT);
        AnimationController.loadItemAnimations(getAnimationSet(), itemKey);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        updateElapsedTime();
        spriteBatch.draw(getAnimationSet().get(ITEM_BACKGROUND_KEY).getKeyFrame(getElapsedTime()), getX(), getY());
        spriteBatch.draw(getAnimationSet().get(ITEM_IMAGE_KEY).getKeyFrame(getElapsedTime()), getX(), getY());
    }

    public abstract void activate(Bomman bomman);
}
