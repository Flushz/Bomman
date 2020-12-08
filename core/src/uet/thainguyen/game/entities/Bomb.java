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
import uet.thainguyen.game.screens.PlayScreen;

import java.util.ArrayList;

public class Bomb extends MovableObject {

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private static final float BOMB_WIDTH = 32;
    private static final float BOMB_HEIGHT = 32;
    private static final float BOMB_TIME_LIMIT = 2;

    public enum State {
        ACTIVATED,
        EXPLODING
    }

    public enum BlockMode {
        ON, OFF
    }

    private State currentState;
    private BlockMode currentMode;
    private float elapsedTime;
    private int power;

    private Animation<TextureRegion> bombAnimation;
    private ArrayList<Flame> flames;

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

    public void checkState(MapController gameMap) {
        this.elapsedTime += Gdx.graphics.getDeltaTime();

        if (this.elapsedTime > BOMB_TIME_LIMIT) {
            setCurrentState(State.EXPLODING);
            generateFlame(gameMap);
        }
    }

    private void generateFlame(MapController gameMap) {
        Flame centerFlame = new Flame(getX(), getY(), Flame.Position.CENTER);
        flames.add(centerFlame);
        for (int i = -power; i <= power; ++i) {
            if (i != 0) {
                Flame flameX = new Flame(getX() + i * TILE_WIDTH, getY(), Flame.Position.HORIZONTAL);
                Flame flameY = new Flame(getX(), getY() + i * TILE_HEIGHT, Flame.Position.VERTICAL);

                flames.add(flameX);
                flames.add(flameY);

                MapObjects collisionObjects = gameMap.getTiledMap().getLayers().get(3).getObjects();
                for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
                    if (Intersector.overlaps(collisionObject.getRectangle(), flameX.getBody())) {
                        flames.remove(flameX);
                    }
                    if (Intersector.overlaps(collisionObject.getRectangle(), flameY.getBody())) {
                        flames.remove(flameY);
                    }
                }
            }
        }
    }

    public void draw(SpriteBatch spriteBatch, float elapsedTime) {
        if (currentState == State.ACTIVATED) {
            spriteBatch.draw(bombAnimation.getKeyFrame(elapsedTime), getX(), getY());
        } else {
            for (Flame flame : flames) {
                flame.draw(spriteBatch, elapsedTime);
            }
        }
    }
}
