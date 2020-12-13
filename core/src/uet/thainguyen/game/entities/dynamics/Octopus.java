package uet.thainguyen.game.entities.dynamics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import uet.thainguyen.game.controllers.AnimationController;
import uet.thainguyen.game.controllers.MapController;

import java.util.Random;

public class Octopus extends Enemy {
    private static final float OCTOPUS_WIDTH = 32;
    private static final float OCTOPUS_HEIGHT = 32;
    private static final int DEFAULT_OCTOPUS_SPEED = 1;

    private boolean canMoveUp;
    private boolean canMoveDown;
    private boolean canMoveLeft;
    private boolean canMoveRight;

    public Octopus(float posX, float posY) {
        super(posX, posY, OCTOPUS_WIDTH, OCTOPUS_HEIGHT, DEFAULT_OCTOPUS_SPEED);
        AnimationController.loadOctopusAnimations(getAnimationSet());
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

        TextureRegion frame = getAnimationSet().get("dying").getKeyFrame(getElapsedTime());
        switch (getCurrentState()) {
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
            case DYING:
                frame = getAnimationSet().get("dying").getKeyFrame(getElapsedTime());
        }
        spriteBatch.draw(frame, getX(), getY());
    }

    @Override
    public void update(MapController gameMap) {
        updateElapsedTime();
        MapObjects collisionObjects = gameMap.getTiledMap().getLayers().get("CollisionLayer").getObjects();
        for (RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
            if (Intersector.overlaps(collisionObject.getRectangle(), this.getBody())) {
                returnToPreviousPosition();

                canMoveUp = checkUpPos(collisionObjects);
                canMoveDown = checkDownPos(collisionObjects);
                canMoveLeft = checkLeftPos(collisionObjects);
                canMoveRight = checkRightPos(collisionObjects);

                State oldState = getCurrentState();
                while (getCurrentState() == oldState) {
                    Random random = new Random();
                    int direction = random.nextInt(4);
                    switch (direction) {
                        case 0:
                            if (canMoveUp) {
                                setCurrentState(State.WALKING_UP);
                            }
                            break;
                        case 1:
                            if (canMoveDown) {
                                setCurrentState(State.WALKING_DOWN);
                            }
                            break;
                        case 2:
                            if (canMoveLeft) {
                                setCurrentState(State.WALKING_LEFT);
                            }
                            break;
                        case 3:
                            if (canMoveRight) {
                                setCurrentState(State.WALKING_RIGHT);
                            }
                            break;
                    }
                }
            }
        }

        updatePosition();
    }

    private void updatePosition() {
        switch (getCurrentState()) {
            case WALKING_UP:
                moveUp();
                break;
            case WALKING_DOWN:
                moveDown();
                break;
            case WALKING_RIGHT:
                moveRight();
                break;
            case WALKING_LEFT:
                moveLeft();
                break;
        }
    }

    private void returnToPreviousPosition() {
        switch (getCurrentState()) {
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

    private boolean checkUpPos(MapObjects collisionObjects) {
        moveUp();
        boolean check = true;
        for (RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
            if (Intersector.overlaps(collisionObject.getRectangle(), this.getBody())) {
                check = false;
                break;
            }
        }
        moveDown();
        return check;
    }

    private boolean checkDownPos(MapObjects collisionObjects) {
        moveDown();
        boolean check = true;
        for (RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
            if (Intersector.overlaps(collisionObject.getRectangle(), this.getBody())) {
                check = false;
                break;
            }
        }
        moveUp();
        return check;
    }

    private boolean checkLeftPos(MapObjects collisionObjects) {
        moveLeft();
        boolean check = true;
        for (RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
            if (Intersector.overlaps(collisionObject.getRectangle(), this.getBody())) {
                check = false;
                break;
            }
        }
        moveRight();
        return true;
    }

    private boolean checkRightPos(MapObjects collisionObjects) {
        moveRight();
        boolean check = true;
        for (RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
            if (Intersector.overlaps(collisionObject.getRectangle(), this.getBody())) {
                check = false;
                break;
            }
        }
        moveLeft();
        return true;
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
}
