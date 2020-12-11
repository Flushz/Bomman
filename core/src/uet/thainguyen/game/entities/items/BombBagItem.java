package uet.thainguyen.game.entities.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uet.thainguyen.game.entities.dynamics.Bomman;

public class BombBagItem extends Item {

    private static final String BOMB_BAG_ITEM_KEY = "bomb_bag_item";

    private static final int BOMB_BAG_LIMIT = 3;

    public BombBagItem(float posX, float posY) {
        super(posX, posY,BOMB_BAG_ITEM_KEY);
    }

    @Override
    public void activate(Bomman bomman) {
        if (bomman.getBombLimit() < BOMB_BAG_LIMIT) {
            bomman.increaseBombLimit(1);
        }
    }
}
