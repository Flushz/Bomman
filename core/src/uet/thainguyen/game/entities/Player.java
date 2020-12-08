package uet.thainguyen.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import uet.thainguyen.game.controllers.AnimationController;
import uet.thainguyen.game.controllers.MapController;

import java.util.ArrayList;
import java.util.Iterator;

public class Player extends MovableObject {

    private static final float PLAYER_RESPAWN_X = 64;
    private static final float PLAYER_RESPAWN_Y = 352;
    private static final float PLAYER_WIDTH = 32;
    private static final float PLAYER_HEIGHT = 32;
    private static final float PLAYER_SPEED = 4;

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private enum State {
        WALKING_UP, WALKING_DOWN, WALKING_RIGHT, WALKING_LEFT,
        IDLING_UP, IDLING_DOWN, IDLING_RIGHT, IDLING_LEFT,
        DYING
    }

    private State currentState;
    private boolean isMoving;

    private ArrayList<Bomb> bombLeft;

    public Player() {
        super(PLAYER_RESPAWN_X, PLAYER_RESPAWN_Y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_SPEED);
        AnimationController.loadPlayerAnimation(getAnimationSet());
        currentState = State.IDLING_DOWN;
        isMoving = false;
        bombLeft = new ArrayList<>();
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public void moveUp() {
        setY(getY() + getSpeed());
    }

    public void moveDown() {
        setY(getY() - getSpeed());
    }

    public void moveRight() {
        setX(getX() + getSpeed());
    }

    public void moveLeft() {
        setX(getX() - getSpeed());
    }

    public void update(MapController gameMap) {
        handleInput();
        checkState();
        checkBombState(gameMap);
        checkBombBlockMode();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            isMoving = true;

            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                setCurrentState(State.WALKING_UP);
                moveUp();
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                setCurrentState(State.WALKING_DOWN);
                moveDown();
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                setCurrentState(State.WALKING_RIGHT);
                moveRight();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                setCurrentState(State.WALKING_LEFT);
                moveLeft();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                int bombRespawnX = getBombRespawnX();
                int bombRespawnY = getBombRespawnY();
                bombLeft.add(new Bomb(bombRespawnX, bombRespawnY));
            }
        } else {
            isMoving = false;
        }
    }

    // Get bomb respawn X and Y with the condition that the bomb is inside a tile.
    private int getBombRespawnX() {
        int leftX = (int) getX();
        while (leftX % TILE_WIDTH != 0) {
            leftX--;
        }
        int rightX = (int) getX();
        while (rightX % TILE_WIDTH != 0) {
            rightX++;
        }
        return (getX() - leftX < rightX - getX() ? leftX : rightX);
    }

    private int getBombRespawnY() {
        int upY = (int) getY();
        while (upY % TILE_HEIGHT != 0) {
            upY++;
        }

        int downY = (int) getY();
        while (downY % TILE_HEIGHT != 0) {
            downY--;
        }

        return (upY - getY() < getY() - downY ? upY : downY);
    }

    private void checkState() {
        if (!isMoving) {
            switch (currentState) {
                case WALKING_UP:
                    setCurrentState(State.IDLING_UP);
                    break;
                case WALKING_DOWN:
                    setCurrentState(State.IDLING_DOWN);
                    break;
                case WALKING_RIGHT:
                    setCurrentState(State.IDLING_RIGHT);
                    break;
                case WALKING_LEFT:
                    setCurrentState(State.IDLING_LEFT);
                    break;
            }
        }
    }

    //Allow player to move out of the bomb once, after that, there will be a collision detection.
    private void checkBombBlockMode() {
        for (Bomb bomb : bombLeft) {
            if (bomb.getCurrentMode() == Bomb.BlockMode.OFF
                && !Intersector.overlaps(getBody(), bomb.getBody())) {
                bomb.setCurrentMode(Bomb.BlockMode.ON);
            }
            if (bomb.getCurrentMode() == Bomb.BlockMode.ON
                && Intersector.overlaps(getBody(), bomb.getBody())) {
                returnPreviousPos();
            }
        }
    }

    private void checkBombState(MapController gameMap) {
        if (!bombLeft.isEmpty()) {
            for (Bomb bomb : bombLeft) {
                bomb.checkState(gameMap);
            }
        }
    }

    //if the player is colliding with a static object, return him to his previous position
    public void returnPreviousPos() {
        switch (currentState) {
            case WALKING_UP:
                moveDown();
                break;
            case WALKING_DOWN:
                moveUp();
                break;
            case WALKING_RIGHT:
                moveLeft();
                break;
            case WALKING_LEFT:
                moveRight();
                break;
        }
    }

    public void render(SpriteBatch spriteBatch, float elapsedTime) {
        Iterator<Bomb> bombIterator = bombLeft.iterator();
        while (bombIterator.hasNext()) {
            Bomb bomb = bombIterator.next();
            bomb.draw(spriteBatch, elapsedTime);
            if (bomb.getCurrentState() == Bomb.State.EXPLODING) {
                bombIterator.remove();
            }
        }

        TextureRegion frame = getAnimationSet().get("idling_down").getKeyFrame(elapsedTime);
        switch (currentState) {
            case WALKING_UP:
                frame = getAnimationSet().get("walking_up").getKeyFrame(elapsedTime);
                break;
            case WALKING_DOWN:
                frame = getAnimationSet().get("walking_down").getKeyFrame(elapsedTime);
                break;
            case WALKING_RIGHT:
                frame = getAnimationSet().get("walking_right").getKeyFrame(elapsedTime);
                break;
            case WALKING_LEFT:
                frame = getAnimationSet().get("walking_left").getKeyFrame(elapsedTime);
                break;
            case IDLING_UP:
                frame = getAnimationSet().get("idling_up").getKeyFrame(elapsedTime);
                break;
            case IDLING_DOWN:
                frame = getAnimationSet().get("idling_down").getKeyFrame(elapsedTime);
                break;
            case IDLING_RIGHT:
                frame = getAnimationSet().get("idling_right").getKeyFrame(elapsedTime);
                break;
            case IDLING_LEFT:
                frame = getAnimationSet().get("idling_left").getKeyFrame(elapsedTime);
                break;
        }
        spriteBatch.draw(frame, getX(), getY());
    }
}
