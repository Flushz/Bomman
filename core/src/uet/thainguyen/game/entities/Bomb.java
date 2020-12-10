package uet.thainguyen.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import uet.thainguyen.game.controllers.AnimationController;
import uet.thainguyen.game.controllers.MapController;

import java.util.ArrayList;

public class Bomb extends MovableObject {

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private static final float BOMB_WIDTH = 32;
    private static final float BOMB_HEIGHT = 32;
    private static final float BOMB_TIME_LIMIT = 2.5f;

    public enum State {
        ACTIVATED,
        EXPLODING,
        EXPLODED
    }

    public enum BlockMode {
        ON, OFF
    }

    private State currentState;
    private BlockMode currentMode;
    private float elapsedTime;
    private int power;

    private final Animation<TextureRegion> bombAnimation;
    private final ArrayList<Flame> flames;

    public Bomb(int posX, int posY) {
        super(posX, posY, BOMB_WIDTH, BOMB_HEIGHT, 0);
        this.elapsedTime = 0;
        this.power = 1;
        this.bombAnimation = AnimationController.loadBombAnimation();
        this.flames = new ArrayList<>();
        setCurrentState(State.ACTIVATED);
        setCurrentMode(BlockMode.OFF);
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public BlockMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(BlockMode currentMode) {
        this.currentMode = currentMode;
    }

    public int getPower() {
        return this.power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public ArrayList<Flame> getFlames() {
        return flames;
    }

    public void checkState(MapController gameMap) {
        this.elapsedTime += Gdx.graphics.getDeltaTime();

        if (this.elapsedTime > BOMB_TIME_LIMIT) {
            setCurrentState(State.EXPLODING);
            if (flames.isEmpty()) {
                generateFlame(gameMap);
            }
        }
    }

    private void generateFlame(MapController gameMap) {
        Flame centerFlame = new Flame(getX(), getY(), Flame.Position.CENTER);
        flames.add(centerFlame);
        for (int i = -power; i <= power; ++i) {
            if (i != 0) {
                Flame horizontalFlame = new Flame(getX() + i * TILE_WIDTH, getY(), Flame.Position.HORIZONTAL);
                Flame verticalFlame = new Flame(getX(), getY() + i * TILE_HEIGHT, Flame.Position.VERTICAL);

                flames.add(horizontalFlame);
                flames.add(verticalFlame);

                MapObjects collisionObjects = gameMap.getTiledMap().getLayers().get(0).getObjects();
                for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
                    if (Intersector.overlaps(collisionObject.getRectangle(), horizontalFlame.getBody())
                            && collisionObject.getName().equals("Wall")) {
                        flames.remove(horizontalFlame);
                    }
                    if (Intersector.overlaps(collisionObject.getRectangle(), verticalFlame.getBody())
                            && collisionObject.getName().equals("Wall")) {
                        flames.remove(verticalFlame);
                    }
                }
            }
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        this.elapsedTime += Gdx.graphics.getDeltaTime();

        if (currentState == State.ACTIVATED) {
            spriteBatch.draw(bombAnimation.getKeyFrame(this.elapsedTime), getX(), getY());
        } else {
            for (Flame flame : flames) {
                flame.draw(spriteBatch);
                if (flame.isFinished()) {
                    setCurrentState(State.EXPLODED);
                }
            }
        }
    }
}
