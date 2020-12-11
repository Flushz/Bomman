package uet.thainguyen.game.entities.items;

import uet.thainguyen.game.entities.dynamics.Bomman;

public class SlowItem extends Item {

    private static final String SLOW_ITEM_KEY = "slow_item";

    public SlowItem(float posX, float posY) {
        super(posX, posY, SLOW_ITEM_KEY);
    }

    @Override
    public void activate(Bomman bomman) {
        bomman.increaseSpeed(-1);
    }
}
