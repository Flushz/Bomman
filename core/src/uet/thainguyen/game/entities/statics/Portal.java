package uet.thainguyen.game.entities.statics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Portal extends StaticObject{

    private final Texture portalTexture;

    public Portal(float posX, float posY) {
        super(posX, posY);
        portalTexture = new Texture(Gdx.files.internal("map/portal.png"));
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(portalTexture, getX(), getY());
    }
}
