package uet.thainguyen.game.entities.statics;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public abstract class StaticLayer {

    private TiledMap tiledMap;
    private final TiledMapTileLayer staticLayer;

    public StaticLayer(TiledMap tiledMap, String staticLayerName) {
        this.tiledMap = tiledMap;
        this.staticLayer = (TiledMapTileLayer) this.tiledMap.getLayers().get(staticLayerName);
    }

    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

    public TiledMapTileLayer getStaticLayer() {
        return this.staticLayer;
    }
}
