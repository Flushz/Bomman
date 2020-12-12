package uet.thainguyen.game.entities.explosion;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import uet.thainguyen.game.controllers.AnimationController;
import uet.thainguyen.game.controllers.MapController;
import uet.thainguyen.game.entities.AnimatedObject;

import java.util.ArrayList;

public class Bomb extends AnimatedObject {

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private static final float BOMB_WIDTH = 32;
    private static final float BOMB_HEIGHT = 32;
    public static final float BOMB_TIME_LIMIT = 2.5f;

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
    private int power;

    private final Animation<TextureRegion> bombAnimation;
    private final ArrayList<Flame> flames;

    public Bomb(int posX, int posY, int power) {
        super(posX, posY, BOMB_WIDTH, BOMB_HEIGHT);
        this.power = power;
        this.bombAnimation = AnimationController.loadBombAnimation();
        this.currentState = State.ACTIVATED;
        this.currentMode = BlockMode.OFF;
        this.flames = new ArrayList<>();
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

    public void increasePower(int power) {
        this.power += power;
    }

    public void checkState(MapController gameMap) {
        updateElapsedTime();
        if (getElapsedTime() > BOMB_TIME_LIMIT) {
            setCurrentState(State.EXPLODING);
            if (flames.isEmpty()) {
                generateFlame(gameMap);
            }
        }
    }

    private void generateFlame(MapController gameMap) {
        MapObjects collisionObjects = gameMap.getTiledMap().getLayers().get(0).getObjects();

        Flame centerFlame = new Flame(getX(), getY(), Flame.Position.CENTER);
        flames.add(centerFlame);

        //generate left flames
        for (int i = -1; i >= -power; --i) {
            Flame horizontalFlame = new Flame(getX() + i * TILE_WIDTH, getY(), Flame.Position.HORIZONTAL);

            boolean isBlocked = false;
            for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
                if (Intersector.overlaps(collisionObject.getRectangle(), horizontalFlame.getBody())
                        && collisionObject.getName().equals("Wall")) {
                    isBlocked = true;
                    break;
                } else if (Intersector.overlaps(collisionObject.getRectangle(), horizontalFlame.getBody())
                        && collisionObject.getName().equals("Brick")) {
                    isBlocked = true;
                    flames.add(horizontalFlame);
                    break;
                }
            }

            if (isBlocked) {
                break;
            } else {
                flames.add(horizontalFlame);
            }
        }

        //generate right flames
        for (int i = 1; i <= power; ++i) {
            Flame horizontalFlame = new Flame(getX() + i * TILE_WIDTH, getY(), Flame.Position.HORIZONTAL);

            boolean isBlocked = false;
            for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
                if (Intersector.overlaps(collisionObject.getRectangle(), horizontalFlame.getBody())
                        && collisionObject.getName().equals("Wall")) {
                    isBlocked = true;
                    break;
                } else if (Intersector.overlaps(collisionObject.getRectangle(), horizontalFlame.getBody())
                        && collisionObject.getName().equals("Brick")) {
                    isBlocked = true;
                    flames.add(horizontalFlame);
                    break;
                }
            }

            if (isBlocked) {
                break;
            } else {
                flames.add(horizontalFlame);
            }
        }

        //generate up flames
        for (int i = 1; i <= power; ++i) {
            Flame verticalFlame = new Flame(getX(), getY() + i * TILE_HEIGHT, Flame.Position.VERTICAL);

            boolean isBlocked = false;
            for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
                if (Intersector.overlaps(collisionObject.getRectangle(), verticalFlame.getBody())
                        && collisionObject.getName().equals("Wall")) {
                    isBlocked = true;
                    break;
                } else if (Intersector.overlaps(collisionObject.getRectangle(), verticalFlame.getBody())
                        && collisionObject.getName().equals("Brick")) {
                    isBlocked = true;
                    flames.add(verticalFlame);
                    break;
                }
            }

            if (isBlocked) {
                break;
            } else {
                flames.add(verticalFlame);
            }
        }

        //generate down flames
        for (int i = -1; i >= -power; --i) {
            Flame verticalFlame = new Flame(getX(), getY() + i * TILE_HEIGHT, Flame.Position.VERTICAL);

            boolean isBlocked = false;
            for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
                if (Intersector.overlaps(collisionObject.getRectangle(), verticalFlame.getBody())
                        && collisionObject.getName().equals("Wall")) {
                    isBlocked = true;
                    break;
                } else if (Intersector.overlaps(collisionObject.getRectangle(), verticalFlame.getBody())
                        && collisionObject.getName().equals("Brick")) {
                    isBlocked = true;
                    flames.add(verticalFlame);
                    break;
                }
            }

            if (isBlocked) {
                break;
            } else {
                flames.add(verticalFlame);
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        updateElapsedTime();
        if (currentState == State.ACTIVATED) {
            spriteBatch.draw(bombAnimation.getKeyFrame(getElapsedTime()), getX(), getY());
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
