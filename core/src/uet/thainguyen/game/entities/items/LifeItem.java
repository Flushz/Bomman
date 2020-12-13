package uet.thainguyen.game.entities.items;

import uet.thainguyen.game.controllers.AnimationController;
import uet.thainguyen.game.entities.dynamics.Bomman;

public class LifeItem extends Item{

    private static final String LIFE_ITEM_KEY = "life_item";
    private static final int LIFE_INCREMENT = 1;

    public LifeItem(float posX, float posY) {
        super(posX, posY, LIFE_ITEM_KEY);
        AnimationController.loadItemAnimations(getAnimationSet(), LIFE_ITEM_KEY);
    }

    @Override
    public void activate(Bomman bomman) {
        int playerLife = bomman.getLifeLeft();
        playerLife += LIFE_INCREMENT;
        bomman.setLifeLeft(playerLife);
    }
}
