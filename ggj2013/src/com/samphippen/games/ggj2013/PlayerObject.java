package com.samphippen.games.ggj2013;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class PlayerObject implements GameObject {

    private static PlayerObject sInstance = null;
    private float mHeartbeatRadius = (float) 1.0;
    private Vector2 mPrevPosition = new Vector2(0, 0);
    private Vector2 mPosition = new Vector2(0, 0);
    private int mTicks = 0;
    private HeartBeatParameters mHeartBeatParameters = new HeartBeatParameters();
    private final double mIncrementTicks = Constants.sConstants
            .get("heartbeat_speed_multiplier");

    private final Double NUMPER_FAST_PULSES = Constants.sConstants
            .get("number_fast_pulses");

    private Sprite mCurrentSprite;
    private final List<Sprite> mUpSprites = new ArrayList<Sprite>();
    private final List<Sprite> mDownSprites = new ArrayList<Sprite>();
    private final List<Sprite> mLeftSprites = new ArrayList<Sprite>();
    private final List<Sprite> mRightSprites = new ArrayList<Sprite>();

    private Sprite currentSprite(List<Sprite> sourceList) {
        final int framesPerFrame = 1;
        int frame = (int) (GameServices.getTicks()
                % (framesPerFrame * sourceList.size()) / framesPerFrame);
        return sourceList.get(frame);
    }

    private void loadFrames(List<Sprite> out, int start, int end,
            String prefix, String suffix) {
        for (int i = start; i <= end; i++) {
            String filename = "" + i;
            while (filename.length() < 3) {
                filename = "0" + filename;
            }

            filename = prefix + filename + suffix;
            Sprite s = GameServices.loadSprite(filename);
            out.add(s);
        }
    }

    public static void reset() {
        sInstance = null;
    }

    private PlayerObject() {
        loadFrames(mLeftSprites, 0, 0, "player_left_", ".png");
        loadFrames(mRightSprites, 0, 0, "player_right_", ".png");
        loadFrames(mUpSprites, 0, 0, "player_back_", ".png");
        loadFrames(mDownSprites, 0, 0, "player_", ".png");
    }

    public static PlayerObject getInstance() {
        if (sInstance == null) {
            sInstance = new PlayerObject();
        }

        return sInstance;
    }

    @Override
    public void update() {
        mPrevPosition = mPosition.cpy();
        mPosition.add(InputSystem.mouseSpeedVector());
        selectSprite();
        mCurrentSprite.setPosition(mPosition.x - 10, mPosition.y - 30);
        mHeartbeatRadius = calculateHeartBeatRadius();
    }

    private void selectSprite() {
        Vector2 delta = new Vector2(mPosition).sub(mPrevPosition);
        if (Math.abs(delta.y) >= Math.abs(delta.x)) {
            if (delta.y > 0) {
                mCurrentSprite = currentSprite(mUpSprites);
            } else {
                mCurrentSprite = currentSprite(mDownSprites);
            }
        } else {
            if (delta.x < 0) {
                mCurrentSprite = currentSprite(mLeftSprites);
            } else {
                mCurrentSprite = currentSprite(mRightSprites);
            }
        }
    }

    public void rejectMovement() {
        mPosition = mPrevPosition;
    }

    public float calculateHeartBeatRadius() {
        mTicks += mIncrementTicks
                * (1.0f + GameHolder.getInstance().getRadialAdjust());
        if (mTicks >= mHeartBeatParameters.getMaxRadius()) {
            mTicks = 0;
        }

        if (mTicks == 0) {
            GameHolder.getInstance().getSoundManager().beatHeart();
        }

        if (mHeartBeatParameters.isFastHeartbeat()) {
            if (mTicks == 0) {
                mHeartBeatParameters.setChaserPulseCount(mHeartBeatParameters
                        .getChaserPulseCount() + 1);
                mHeartBeatParameters.setElapsedFastPulses(mHeartBeatParameters
                        .getElapsedFastPulses() + 1);
                mHeartBeatParameters.setTreePulseCount(mHeartBeatParameters
                        .getTreePulseCount() + 1);
            }

            if (mHeartBeatParameters.getElapsedFastPulses() >= NUMPER_FAST_PULSES) {
                mHeartBeatParameters.setHeartBeatSlow();
                InputSystem.getInstance().setNormal();
                GameHolder.getInstance().whitePulse();
            }
        } else {
            GameHolder.getInstance().whitePulse();
        }

        return (float) (mTicks * mIncrementTicks);
    }

    public Vector2 getPosition() {
        return mPosition;
    }

    @Override
    public void emitRenderables(RenderQueueProxy renderables) {
        renderables
                .add(new SpriteRenderable(mCurrentSprite), (int) mPosition.y);
    }

    public float getHeartbeatRadius() {
        return mHeartbeatRadius;
    }

    public void setHeartbeatRadius(float mHeartbeatRadius) {
        this.mHeartbeatRadius = mHeartbeatRadius;
    }

    public HeartBeatParameters getHeartBeatParameters() {
        return mHeartBeatParameters;
    }

    public void setHeartBeatParameters(HeartBeatParameters heartBeatParameters) {
        mHeartBeatParameters = heartBeatParameters;
    }

}
