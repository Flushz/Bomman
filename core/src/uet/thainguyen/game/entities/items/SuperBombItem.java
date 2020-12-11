package uet.thainguyen.game.entities.items;

import uet.thainguyen.game.controllers.AnimationController;
import uet.thainguyen.game.entities.dynamics.Bomman;
import uet.thainguyen.game.entities.explosion.Bomb;

import java.util.ArrayList;

public class SuperBombItem extends Item {

    private static final String SUPER_BOMB_ITEM_KEY = "super_bomb_item";
    private static final int BOMB_POWER_INCREMENT = 1;

    public SuperBombItem(float posX, float posY) {
        super(posX, posY, SUPER_BOMB_ITEM_KEY);
        AnimationController.loadItemAnimations(getAnimationSet(), SUPER_BOMB_ITEM_KEY);
    }

    @Override
    public void activate(Bomman bomman) {
        bomman.increaseBombPower(BOMB_POWER_INCREMENT);
    }
}
