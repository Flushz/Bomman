package uet.thainguyen.game.entities.dynamics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import uet.thainguyen.game.controllers.AnimationController;
import uet.thainguyen.game.controllers.MapController;
import uet.thainguyen.game.controllers.SoundController;
import uet.thainguyen.game.entities.explosion.Bomb;
import uet.thainguyen.game.entities.explosion.Flame;

import java.util.ArrayList;
import java.util.Iterator;

public class Bomman extends DynamicObject {

    private static final float PLAYER_RESPAWN_X = 64;
    private static final float PLAYER_RESPAWN_Y = 352;
    private static final float PLAYER_WIDTH = 32;
    private static final float PLAYER_HEIGHT = 32;

    public static final int DEFAULT_PLAYER_LIFE_LEFT = 1;
    public static final int DEFAULT_PLAYER_SPEED = 2;
    public static final int DEFAULT_PLAYER_BOMB_LIMIT = 1;
    public static final int DEFAULT_PLAYER_BOMB_POWER = 1;
    public static final int DEFAULT_PLAYER_INVINCIBILITY_DURATION = 3;
    public static final int DEFAULT_PLAYER_ITEM_DURATION = 10;
    public static final int ITEM_INEFFECTIVE_TIME = 0;

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int ACCEPTED_GO_THROUGH_PIXELS = 15;

    public enum State {
        WALKING_UP, WALKING_DOWN, WALKING_RIGHT, WALKING_LEFT,
        IDLING_UP, IDLING_DOWN, IDLING_RIGHT, IDLING_LEFT,
        DYING
    }

    private SoundController soundController;

    private State currentState;
    private int lifeLeft;
    private int bombLimit;
    private int bombPower;
    private float itemDuration;
    private float invincibilityDuration;
    private boolean isMoving;
    private boolean isPoweredUp;
    private boolean isInvincible;

    private ArrayList<Bomb> bombs;
    private ArrayList<Flame> flames;

    public Bomman() {
        super(PLAYER_RESPAWN_X, PLAYER_RESPAWN_Y, PLAYER_WIDTH, PLAYER_HEIGHT, DEFAULT_PLAYER_SPEED);
        AnimationController.loadPlayerAnimation(getAnimationSet());
        this.soundController = new SoundController();
        this.currentState = State.IDLING_DOWN;
        this.lifeLeft = DEFAULT_PLAYER_LIFE_LEFT;
        this.bombLimit = DEFAULT_PLAYER_BOMB_LIMIT;
        this.bombPower = DEFAULT_PLAYER_BOMB_POWER;
        this.invincibilityDuration = DEFAULT_PLAYER_INVINCIBILITY_DURATION;
        this.itemDuration = DEFAULT_PLAYER_ITEM_DURATION;
        this.isMoving = false;
        this.isPoweredUp = false;
        this.isInvincible = false;
        this.bombs = new ArrayList<>();
        this.flames = new ArrayList<>();
    }

    public void resetProperties() {
        setSpeed(DEFAULT_PLAYER_SPEED);
        setBombLimit(DEFAULT_PLAYER_BOMB_LIMIT);
        setItemDuration(DEFAULT_PLAYER_ITEM_DURATION);
        setBombPower(DEFAULT_PLAYER_BOMB_POWER);
    }

    public boolean isInvincible() {
        return this.isInvincible;
    }

    public int getLifeLeft() {
        return this.lifeLeft;
    }

    public void setLifeLeft(int lifeLeft) {
        this.lifeLeft = lifeLeft;
    }

    public void decreaseLife() {
        if (!isInvincible) {
            this.lifeLeft -= 1;
            if (this.lifeLeft <= 0) {
                soundController.getPlayerDieSound().play();
                setCurrentState(State.DYING);
            }
            isInvincible = true;
        }
    }

    public void powerUp() {
        this.isPoweredUp = true;
    }

    public void powerDown() {
        this.isPoweredUp = false;
    }

    public float getItemDuration() {
        return this.itemDuration;
    }

    public void setItemDuration(float itemDuration) {
        this.itemDuration = itemDuration;
    }

    public State getCurrentState() {
        return currentState;
    }

    public int getBombLimit() {
        return this.bombLimit;
    }

    public void setBombLimit(int bombLimit) {
        this.bombLimit = bombLimit;
    }

    public int getBombPower() {
        return bombPower;
    }

    public void setBombPower(int bombPower) {
        this.bombPower = bombPower;
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

    public void increaseBombLimit(int bombLimit) {
        this.bombLimit += bombLimit;
    }

    public void increaseBombPower(int bombPower) {
        this.bombPower += bombPower;
    }

    @Override
    public void moveUp() {
        setY(getY() + getSpeed());
    }

    @Override
    public void moveDown() {
        setY(getY() - getSpeed());
    }

    @Override
    public void moveRight() {
        setX(getX() + getSpeed());
    }

    @Override
    public void moveLeft() {
        setX(getX() - getSpeed());
    }

    @Override
    public void update(MapController gameMap) {
        if (isPoweredUp) {
            updateItemDuration();
        }
        if (isInvincible) {
            invincibilityDuration -= Gdx.graphics.getDeltaTime();
            if (invincibilityDuration < 0) {
                isInvincible = false;
                invincibilityDuration = DEFAULT_PLAYER_INVINCIBILITY_DURATION;
            }
        }
        handleInput();
        checkState();
        checkBombState(gameMap);
        checkBombBlockMode();
        updateElapsedTime();
    }

    public void updateItemDuration() {
        itemDuration -= Gdx.graphics.getDeltaTime();
        if (itemDuration <= ITEM_INEFFECTIVE_TIME) {
            powerDown();
            resetProperties();
        }
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
                if (bombs.size() < bombLimit) {
                    soundController.getBombDropSound().play();
                    int bombRespawnX = getBombRespawnX();
                    int bombRespawnY = getBombRespawnY();
                    bombs.add(new Bomb(bombRespawnX, bombRespawnY, bombPower));
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
