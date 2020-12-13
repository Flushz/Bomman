package uet.thainguyen.game.entities.items;

import uet.thainguyen.game.entities.dynamics.Bomman;

public class SpeedItem extends Item{

    private static final String SPEED_ITEM_KEY = "speed_item";

    public SpeedItem(float posX, float posY) {
        super(posX, posY, SPEED_ITEM_KEY);
    }

    @Override
    public void activate(Bomman bomman) {
        bomman.increaseSpeed(2);
    }
}
