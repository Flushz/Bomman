package uet.thainguyen.game.entities.statics;

import com.badlogic.gdx.maps.tiled.TiledMap;
import uet.thainguyen.game.entities.statics.StaticLayer;

public class WallLayer extends StaticLayer {

    public WallLayer(TiledMap tiledMap) {
        super(tiledMap, "Wall");
    }
}
