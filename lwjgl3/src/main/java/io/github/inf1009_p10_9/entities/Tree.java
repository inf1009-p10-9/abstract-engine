package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Tree extends Entity {
    private static final Color TRUNK_COLOR  = new Color(0.40f, 0.25f, 0.10f, 1f);
    //private static final Color LEAVES_COLOR = new Color(0.15f, 0.40f, 0.15f, 1f);
    private static final Color LEAVES_DARK  = new Color(0.10f, 0.30f, 0.10f, 1f);

    private static final float TRUNK_WIDTH  = 12f;
    private static final float TRUNK_HEIGHT = 35f;
    private static final float LEAVES_RADIUS = 28f;

    public Tree(float x, float y) {
        super.position.set(x, y);
        super.bounds.set(x, y, TRUNK_WIDTH, TRUNK_HEIGHT + LEAVES_RADIUS);
    }

    @Override
    public void update() {
        bounds.setPosition(position.x, position.y);
    }

    @Override public void render(SpriteBatch batch) {}

    @Override
    public void renderShapes(ShapeRenderer sr) {
        // trunk shadow
        sr.setColor(new Color(0.25f, 0.15f, 0.05f, 1f));
        sr.rect(position.x + 4f, position.y, TRUNK_WIDTH, TRUNK_HEIGHT);

        // trunk
        sr.setColor(TRUNK_COLOR);
        sr.rect(position.x, position.y, TRUNK_WIDTH, TRUNK_HEIGHT);

        // trunk highlight
        sr.setColor(new Color(0.55f, 0.35f, 0.15f, 1f));
        sr.rect(position.x + 2f, position.y, TRUNK_WIDTH * 0.3f, TRUNK_HEIGHT);

        // bottom shadow layer of leaves (widest)
        sr.setColor(new Color(0.08f, 0.25f, 0.08f, 1f));
        sr.circle(position.x + TRUNK_WIDTH / 2 + 3f, position.y + TRUNK_HEIGHT + LEAVES_RADIUS * 0.6f, LEAVES_RADIUS * 1.1f, 16);

        // mid layer of leaves
        sr.setColor(LEAVES_DARK);
        sr.circle(position.x + TRUNK_WIDTH / 2, position.y + TRUNK_HEIGHT + LEAVES_RADIUS * 0.75f, LEAVES_RADIUS, 16);

        // top highlight layer (brightest, smallest, offset slightly)
        sr.setColor(new Color(0.25f, 0.55f, 0.20f, 1f));
        sr.circle(position.x + TRUNK_WIDTH / 2 - 4f, position.y + TRUNK_HEIGHT + LEAVES_RADIUS * 0.9f, LEAVES_RADIUS * 0.65f, 16);

        // small bright spot for light reflection
        sr.setColor(new Color(0.35f, 0.65f, 0.25f, 1f));
        sr.circle(position.x + TRUNK_WIDTH / 2 - 6f, position.y + TRUNK_HEIGHT + LEAVES_RADIUS, LEAVES_RADIUS * 0.35f, 12);
    }

    @Override public int getZIndex() { return 4; }
}
