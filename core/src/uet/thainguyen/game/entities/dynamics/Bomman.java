package uet.thainguyen.game.entities.dynamics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import uet.thainguyen.game.controllers.AnimationController;
import uet.thainguyen.game.controllers.MapController;
import uet.thainguyen.game.entities.Bomb;
import uet.thainguyen.game.entities.Flame;
import uet.thainguyen.game.entities.dynamics.DynamicObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Bomman extends DynamicObject {

    private static final float PLAYER_RESPAWN_X = 64;
    private static final float PLAYER_RESPAWN_Y = 352;
    private static final float PLAYER_WIDTH = 32;
    private static final float PLAYER_HEIGHT = 32;
    private static final int PLAYER_SPEED = 2;

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int ACCEPTED_GO_THROUGH_PIXELS = 15;

    public enum State {
        WALKING_UP, WALKING_DOWN, WALKING_RIGHT, WALKING_LEFT,
        IDLING_UP, IDLING_DOWN, IDLING_RIGHT, IDLING_LEFT,
        DYING
    }

    private State currentState;
    private int bombLeft;
    private boolean isMoving;

    private ArrayList<Bomb> bombs;
    private ArrayList<Flame> flames;

    public Bomman() {
        super(PLAYER_RESPAWN_X, PLAYER_RESPAWN_Y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_SPEED);
        AnimationController.loadPlayerAnimation(getAnimationSet());
        currentState = State.IDLING_DOWN;
        isMoving = false;
        bombLeft = 1;
        bombs = new ArrayList<>();
        flames = new ArrayList<>();
    }

    public State getCurrentState() {
        return currentState;
    }

    public int getBombLeft() {
        return bombLeft;
    }

    public void setBombLeft(int bombLeft) {
        this.bombLeft = bombLeft;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public void setBombs(ArrayList<Bomb> bombs) {
        this.bombs = bombs;
    }

    public ArrayList<Flame> getFlames() {
        return flames;
    }

    public void setFlames(ArrayList<Flame> flames) {
        this.flames = flames;
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

    @Override
    public void update(MapController gameMap) {
        handleInput();
        checkState();
        checkBombState(gameMap);
        checkBombBlockMode();
        updateElapsedTime();
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
                if (bombs.size() < bombLeft) {
                    int bombRespawnX = getBombRespawnX();
                    int bombRespawnY = getBombRespawnY();
                    bombs.add(new Bomb(bombRespawnX, bombRespawnY));
                }
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
        for (Bomb bomb : bombs) {
            if (bomb.getCurrentMode() == Bomb.BlockMode.OFF
                && !Intersector.overlaps(getBody(), bomb.getBody())) {
                bomb.setCurrentMode(Bomb.BlockMode.ON);
            }
            if (bomb.getCurrentMode() == Bomb.BlockMode.ON
                && Intersector.overlaps(getBody(), bomb.getBody())) {
                returnPreviousPos(bomb.getBody());
            }
        }
    }

    private void checkBombState(MapController gameMap) {
        if (!bombs.isEmpty()) {
            for (Bomb bomb : bombs) {
                bomb.checkState(gameMap);
            }
        }
    }

    //if the player is colliding with a static object, return him to his previous position
    public void returnPreviousPos(Rectangle objectBody) {
        switch (currentState) {
            case WALKING_UP:
                if (Math.abs(objectBody.getX() + TILE_WIDTH - getX()) < ACCEPTED_GO_THROUGH_PIXELS) {
                    setX(Math.abs(objectBody.getX() + TILE_WIDTH));
                } else if (Math.abs(getX() + TILE_WIDTH - objectBody.getX()) < ACCEPTED_GO_THROUGH_PIXELS) {
                    setX(Math.abs(objectBody.getX() - TILE_WIDTH));
                }
                moveDown();
                break;
            case WALKING_DOWN:
                if (Math.abs(objectBody.getX() + TILE_WIDTH - getX()) < ACCEPTED_GO_THROUGH_PIXELS) {
                    setX(Math.abs(objectBody.getX() + TILE_WIDTH));
                } else if (Math.abs(getX() + TILE_WIDTH - objectBody.getX()) < ACCEPTED_GO_THROUGH_PIXELS) {
                    setX(Math.abs(objectBody.getX() - TILE_WIDTH));
                }
                moveUp();
                break;
            case WALKING_RIGHT:
                if (Math.abs(objectBody.getY() + TILE_HEIGHT - getY()) < ACCEPTED_GO_THROUGH_PIXELS) {
                    setY(Math.abs(objectBody.getY() + TILE_HEIGHT));
                } else if (Math.abs(getY() + TILE_HEIGHT - objectBody.getY()) < ACCEPTED_GO_THROUGH_PIXELS) {
                    setY(Math.abs(objectBody.getY() - TILE_HEIGHT));
                }
                moveLeft();
                break;
            case WALKING_LEFT:
                if (Math.abs(objectBody.getY() + TILE_HEIGHT - getY()) < ACCEPTED_GO_THROUGH_PIXELS) {
                    setY(Math.abs(objectBody.getY() + TILE_HEIGHT));
                } else if (Math.abs(getY() + TILE_HEIGHT - objectBody.getY()) < ACCEPTED_GO_THROUGH_PIXELS) {
                    setY(Math.abs(objectBody.getY() - TILE_HEIGHT));
                }
                moveRight();
                break;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        Iterator<Bomb> bombIterator = bombs.iterator();
        while (bombIterator.hasNext()) {
            Bomb bomb = bombIterator.next();
            bomb.render(spriteBatch);
            if (bomb.getCurrentState() == Bomb.State.EXPLODED) {
                bombIterator.remove();
            }
        }

        TextureRegion frame = getAnimationSet().get("idling_down").getKeyFrame(getElapsedTime());
        switch (currentState) {
            case WALKING_UP:
                frame = getAnimationSet().get("walking_up").getKeyFrame(getElapsedTime());
                break;
            case WALKING_DOWN:
                frame = getAnimationSet().get("walking_down").getKeyFrame(getElapsedTime());
                break;
            case WALKING_RIGHT:
                frame = getAnimationSet().get("walking_right").getKeyFrame(getElapsedTime());
                break;
            case WALKING_LEFT:
                frame = getAnimationSet().get("walking_left").getKeyFrame(getElapsedTime());
                break;
            case IDLING_UP:
                frame = getAnimationSet().get("idling_up").getKeyFrame(getElapsedTime());
                break;
            case IDLING_DOWN:
                frame = getAnimationSet().get("idling_down").getKeyFrame(getElapsedTime());
                break;
            case IDLING_RIGHT:
                frame = getAnimationSet().get("idling_right").getKeyFrame(getElapsedTime());
                break;
            case IDLING_LEFT:
                frame = getAnimationSet().get("idling_left").getKeyFrame(getElapsedTime());
                break;
            case DYING:
                frame = getAnimationSet().get("dying").getKeyFrame(getElapsedTime());
        }
        spriteBatch.draw(frame, getX(), getY());
    }
}
