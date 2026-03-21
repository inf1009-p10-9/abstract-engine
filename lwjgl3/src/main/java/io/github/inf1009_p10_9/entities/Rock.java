package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IRenderable;

public class Rock extends Entity implements IRenderable {
    private static final Color ROCK_COLOR  = new Color(0.55f, 0.50f, 0.42f, 1f);
    private static final Color ROCK_SHADOW = new Color(0.35f, 0.30f, 0.25f, 1f);

    private final float size;

    public Rock(float x, float y, float size) {
        this.size = size;
        super.position.set(x, y);
        super.bounds.set(x, y, size, size * 0.6f);
    }

    @Override
    public void update() {
        bounds.setPosition(position.x, position.y);
    }

    @Override
    public void render(SpriteBatch batch) {}

    @Override
    public void renderShapes(ShapeRenderer sr) {
        // shadow
        sr.setColor(ROCK_SHADOW);
        sr.ellipse(position.x + size * 0.1f, position.y, size * 0.8f, size * 0.3f);

        // rock body
        sr.setColor(ROCK_COLOR);
        sr.ellipse(position.x, position.y + size * 0.1f, size, size * 0.6f);
    }

    @Override public int getZIndex() { return 3; }
    @Override public void onCollision(ICollidable other) {}
    @Override public int getCollisionLayer() { return 0; }
}