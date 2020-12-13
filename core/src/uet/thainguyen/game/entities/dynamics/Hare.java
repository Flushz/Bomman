package uet.thainguyen.game.entities.dynamics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import uet.thainguyen.game.controllers.AnimationController;
import uet.thainguyen.game.controllers.MapController;

public class Hare extends Enemy {

    private static final float HARE_WIDTH = 32;
    private static final float HARE_HEIGHT = 32;
    private static final int HARE_SPEED = 1;

    public Hare(float spawnX, float spawnY) {
        super(spawnX, spawnY, HARE_WIDTH, HARE_HEIGHT, HARE_SPEED);
        AnimationController.loadHareAnimations(getAnimationSet());
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
        updateElapsedTime();

        MapObjects collisionObjects = gameMap.getTiledMap().getLayers().get("CollisionLayer").getObjects();
        checkCollisionHareAndMap(collisionObjects);
    }

    public void checkCollisionHareAndMap(MapObjects collisionObjects) {
        for(RectangleMapObject collisionObject : collisionObjects.getByType(RectangleMapObject.class)) {
            if (Intersector.overlaps(collisionObject.getRectangle(), this.getBody())) {
                if (getCurrentState() == State.WALKING_LEFT) {
                    setCurrentState(State.WALKING_RIGHT);
                } else {
                    setCurrentState(State.WALKING_LEFT);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        TextureRegion frame = getAnimationSet().get("walking_down").getKeyFrame(getElapsedTime());
        switch (getCurrentState()) {
            case WALKING_UP:
                frame = getAnimationSet().get("walking_up").getKeyFrame(getElapsedTime());
                break;
            case WALKING_DOWN:
                frame = getAnimationSet().get("walking_down").getKeyFrame(getElapsedTime());
                break;
            case WALKING_RIGHT:
                moveRight();
                frame = getAnimationSet().get("walking_right").getKeyFrame(getElapsedTime());
                break;
            case WALKING_LEFT:
                moveLeft();
                frame = getAnimationSet().get("walking_left").getKeyFrame(getElapsedTime());
                break;
        }
        spriteBatch.draw(frame,getX(), getY());
    }
}
