package com.samphippen.games.ggj2013;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class InputSystem {
    private static InputSystem sInstance = null;

    private static float mSpeed = 1.0f;
    private static int mRunTime = 0;
    private static boolean mExhausted = false;

    private static final float DEAD_ZONE_SIZE = (float) (1.0f * Constants.sConstants
            .get("dead_zone_size"));

    public static void reset() {
        mSpeed = 1.0f;
        mRunTime = 0;
        mExhausted = false;
    }

    public static InputSystem getInstance() {
        if (sInstance == null) {
            sInstance = new InputSystem();
        }

        return sInstance;
    }

    private static boolean mEnabled = false;

    public static void enable() {
        mEnabled = true;
    }

    public static void disable() {
        mEnabled = false;
    }

    public static Vector2 mouseSpeedVector() {
        float x = Gdx.input.getX() * 1.0f / (GameServices.WIDTH / 2) - 1;
        float y = 1.0f - Gdx.input.getY() * 1.0f / (GameServices.HEIGHT / 2);

        if (Math.hypot(x, y) < DEAD_ZONE_SIZE) {
            x = 0;
            y = 0;
        }

        if (!Gdx.input.isTouched()) {
            x = 0;
            y = 0;
        }

        Vector2 result = new Vector2(x, y);
        if (result.len() > 0) {
            double desiredSize = Constants.sConstants.get("player_speed");
            result.mul((float) (desiredSize / result.len()));
        }

        if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
            result.set(result.x, 1.0f);
        }

        if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
            result.set(result.x, -1.0f);
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            result.set(-1.0f, result.y);
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT)
                || Gdx.input.isKeyPressed(Keys.D)) {
            result.set(1.0f, result.y);
        }

        float targetSpeed;
        if (mExhausted) {
            if (GameServices.getTicks() % 2 == 1) {
                --mRunTime;
            }
            if (mRunTime == 0) {
                mExhausted = false;
            }
            GameHolder.getInstance().getSoundManager().setPantingTarget(0.4f);
            targetSpeed = Constants.getFloat("player_exhausted_speed");
            ;
        } else if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
                || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
            ++mRunTime;
            if (mRunTime > Constants.getInt("exhausted_run_time")) {
                mExhausted = true;

            }
            PlayerObject.getInstance().getHeartBeatParameters()
                    .setHeartBeatFast();
            GameHolder.getInstance().getSoundManager().setPantingTarget(1.0f);
            targetSpeed = Constants.getFloat("player_sprint_speed");
        } else {
            if (mRunTime > 0) {
                --mRunTime;
            }
            GameHolder.getInstance().getSoundManager().setPantingTarget(0.0f);
            targetSpeed = Constants.getFloat("player_speed");
        }
        float speedSmoothness = Constants.getFloat("speed_smoothness");
        mSpeed = speedSmoothness * mSpeed + (1.0f - speedSmoothness)
                * targetSpeed;
        if (mEnabled) {
            return result.nor().mul(mSpeed);
        } else {
            return new Vector2(0, 0);
        }
    }

    public void setSlow() {
        mSpeed = Constants.getFloat("tree_slow_speed");
    }

    public void setNormal() {
        mSpeed = 1.0f;
    }
}
