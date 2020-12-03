package uet.thainguyen.game.controllers;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimationController {

    private static final int PLAYER_FRAME_COLS = 4;
    private static final int PLAYER_FRAME_ROWS = 6;

    public static void loadPlayerAnimation(HashMap<String, Animation<TextureRegion>> playerAnimations) {

        Texture playerTexture = new Texture(Gdx.files.internal("sprites/bomberman1.png"));
        TextureRegion[][] playerSprites = TextureRegion.split(playerTexture,
                playerTexture.getWidth() / PLAYER_FRAME_COLS,
                playerTexture.getHeight() / PLAYER_FRAME_ROWS);

        TextureRegion[] walkingLeftFrames = new TextureRegion[3];
        TextureRegion[] walkingRightFrames = new TextureRegion[3];
        TextureRegion[] walkingUpFrames = new TextureRegion[3];
        TextureRegion[] walkingDownFrames = new TextureRegion[3];

        for (int i = 0; i <= 2; i++) {
            walkingLeftFrames[i] = playerSprites[0][i];
            walkingRightFrames[i] = playerSprites[1][i];
            walkingUpFrames[i] = playerSprites[2][i];
            walkingDownFrames[i] = playerSprites[3][i];
        }

        Animation<TextureRegion> walkingLeftAnimation = new Animation<>(0.2f, walkingLeftFrames);
        walkingLeftAnimation.setPlayMode(PlayMode.LOOP);
        Animation<TextureRegion> walkingRightAnimation = new Animation<>(0.2f, walkingRightFrames);
        walkingRightAnimation.setPlayMode(PlayMode.LOOP);
        Animation<TextureRegion> walkingUpAnimation = new Animation<>(0.2f, walkingUpFrames);
        walkingUpAnimation.setPlayMode(PlayMode.LOOP);
        Animation<TextureRegion> walkingDownAnimation = new Animation<>(0.2f, walkingDownFrames);
        walkingDownAnimation.setPlayMode(PlayMode.LOOP);

        playerAnimations.put("walking_left", walkingLeftAnimation);
        playerAnimations.put("walking_right", walkingRightAnimation);
        playerAnimations.put("walking_up", walkingUpAnimation);
        playerAnimations.put("walking_down", walkingDownAnimation);
    }
}
