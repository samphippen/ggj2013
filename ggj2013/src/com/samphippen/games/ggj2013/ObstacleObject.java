package com.samphippen.games.ggj2013;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class ObstacleObject implements GameObject {

    private final Sprite mSprite;

    public ObstacleObject(Vector2 position) {
        mSprite = GameServices.loadSprite("tree.png");
        mSprite.setScale(Constants.getFloat("tree_sprite_scale"));
        mSprite.setPosition(position.x, position.y);
    }

    @Override
    public void update() {
        Vector2 playerPosition = PlayerObject.getInstance().getPosition();
        if (playerPosition.dst(mSprite.getX() + mSprite.getWidth() / 2 - 16f,
                mSprite.getY() + mSprite.getHeight() * 0.2f) < Constants
                .getDouble("obstacle_width")) {
            PlayerObject.getInstance().rejectMovement();
            PlayerObject.getInstance().getHeartBeatParameters()
                    .setHeartBeatFast();
            GameHolder.getInstance().getSoundManager().thunk();
            PlayerObject.getInstance().getHeartBeatParameters()
                    .setTreePulseCount(0);
            InputSystem.getInstance().setSlow();
        }
    }

    @Override
    public void emitRenderables(RenderQueueProxy renderQueue) {
        renderQueue.add(new SpriteRenderable(mSprite),
                (int) (mSprite.getY() + mSprite.getHeight() * 0.15));
    }

}
