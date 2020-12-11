package uet.thainguyen.game.entities.items;

import uet.thainguyen.game.entities.AnimatedObject;

public abstract class Item extends AnimatedObject {

    protected static final int ITEM_WIDTH = 32;
    protected static final int ITEM_HEIGHT = 32;
    protected static final String ITEM_BACKGROUND_KEY = "background";
    protected static final String ITEM_IMAGE_KEY = "image";

    public Item(float posX, float posY) {
        super(posX, posY, ITEM_WIDTH, ITEM_HEIGHT);
    }

    public abstract void activate();
}
