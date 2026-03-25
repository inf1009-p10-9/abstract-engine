package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Rock extends Entity {
    private static final Color ROCK_COLOR  = new Color(0.55f, 0.50f, 0.42f, 1f);
    //private static final Color ROCK_SHADOW = new Color(0.35f, 0.30f, 0.25f, 1f);

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
        Color shadowColor    = new Color(0.25f, 0.22f, 0.18f, 1f);
        Color midColor       = new Color(0.45f, 0.40f, 0.33f, 1f);
        Color highlightColor = new Color(0.70f, 0.65f, 0.55f, 1f);
        Color specularColor  = new Color(0.85f, 0.80f, 0.70f, 1f);

        // ground shadow underneath
        sr.setColor(new Color(0f, 0f, 0f, 0.3f));
        sr.ellipse(position.x + size * 0.1f, position.y - size * 0.05f, size * 0.9f, size * 0.2f);

        // dark shadow layer - offset right and down
        sr.setColor(shadowColor);
        sr.ellipse(position.x + size * 0.12f, position.y, size * 0.88f, size * 0.55f);

        // mid base layer
        sr.setColor(midColor);
        sr.ellipse(position.x, position.y + size * 0.05f, size, size * 0.6f);

        // main rock body
        sr.setColor(ROCK_COLOR);
        sr.ellipse(position.x, position.y + size * 0.08f, size * 0.92f, size * 0.55f);

        // highlight layer - offset left and up
        sr.setColor(highlightColor);
        sr.ellipse(position.x + size * 0.05f, position.y + size * 0.2f, size * 0.55f, size * 0.32f);

        // specular spot - top left bright spot
        sr.setColor(specularColor);
        sr.ellipse(position.x + size * 0.1f, position.y + size * 0.28f, size * 0.28f, size * 0.18f);
    }

    @Override public int getZIndex() { return 3; }
}
