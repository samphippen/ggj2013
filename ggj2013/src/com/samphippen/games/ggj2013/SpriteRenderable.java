package com.samphippen.games.ggj2013;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteRenderable implements Renderable {

    private final Sprite mSprite;

    public SpriteRenderable(Sprite s) {
        mSprite = s;
    }

    public SpriteRenderable(String fileName) {
        this(GameServices.loadSprite(fileName));
    }

    @Override
    public void draw(SpriteBatch batch) {
        mSprite.draw(batch);
    }
}
