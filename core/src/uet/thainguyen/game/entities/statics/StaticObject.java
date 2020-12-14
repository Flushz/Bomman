package uet.thainguyen.game.entities.statics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uet.thainguyen.game.entities.GameObject;

public abstract class StaticObject extends GameObject {

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    public StaticObject(float posX, float posY) {
        super(posX, posY, TILE_WIDTH, TILE_HEIGHT);
    }

    public abstract void render(SpriteBatch spriteBatch);
}
