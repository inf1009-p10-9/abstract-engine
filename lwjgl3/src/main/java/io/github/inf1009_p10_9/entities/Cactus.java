package io.github.inf1009_p10_9.entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Cactus extends Entity {
    private static final Color CACTUS_GREEN = new Color(0.18f, 0.45f, 0.18f, 1f);
    private static final float TRUNK_WIDTH  = 14f;
    private static final float TRUNK_HEIGHT = 60f;
    private static final float ARM_WIDTH    = 12f;
    private static final float ARM_HEIGHT   = 24f;

    public Cactus(float x, float y) {
        super.position.set(x, y);
        super.bounds.set(x, y, TRUNK_WIDTH, TRUNK_HEIGHT);
    }

    @Override
    public void update() {
        bounds.setPosition(position.x, position.y);
    }

    @Override
    public void render(SpriteBatch batch) {}

    @Override
    public void renderShapes(ShapeRenderer sr) {
        // shadow color
        Color shadowGreen    = new Color(0.08f, 0.25f, 0.08f, 1f);
        Color highlightGreen = new Color(0.28f, 0.60f, 0.28f, 1f);

        // trunk shadow
        sr.setColor(shadowGreen);
        sr.rect(position.x + TRUNK_WIDTH * 0.6f, position.y, TRUNK_WIDTH * 0.4f, TRUNK_HEIGHT);

        // trunk main
        sr.setColor(CACTUS_GREEN);
        sr.rect(position.x, position.y, TRUNK_WIDTH, TRUNK_HEIGHT);

        // trunk highlight strip
        sr.setColor(highlightGreen);
        sr.rect(position.x + 1f, position.y, TRUNK_WIDTH * 0.25f, TRUNK_HEIGHT);

        // left arm shadow
        sr.setColor(shadowGreen);
        sr.rect(position.x - ARM_WIDTH + 2f, position.y + TRUNK_HEIGHT * 0.4f - 2f, ARM_WIDTH * 0.8f, ARM_HEIGHT * 0.4f);
        sr.rect(position.x - ARM_WIDTH + 2f, position.y + TRUNK_HEIGHT * 0.4f - 2f, ARM_WIDTH * 0.6f, ARM_HEIGHT);

        // left arm main
        sr.setColor(CACTUS_GREEN);
        sr.rect(position.x - ARM_WIDTH, position.y + TRUNK_HEIGHT * 0.4f, ARM_WIDTH, ARM_HEIGHT * 0.4f);
        sr.rect(position.x - ARM_WIDTH, position.y + TRUNK_HEIGHT * 0.4f, ARM_WIDTH * 0.8f, ARM_HEIGHT);

        // left arm highlight
        sr.setColor(highlightGreen);
        sr.rect(position.x - ARM_WIDTH + 1f, position.y + TRUNK_HEIGHT * 0.4f, ARM_WIDTH * 0.2f, ARM_HEIGHT);

        // right arm shadow
        sr.setColor(shadowGreen);
        sr.rect(position.x + TRUNK_WIDTH + ARM_WIDTH * 0.6f, position.y + TRUNK_HEIGHT * 0.55f - 2f, ARM_WIDTH * 0.4f, ARM_HEIGHT * 0.4f);
        sr.rect(position.x + TRUNK_WIDTH + ARM_WIDTH * 0.6f, position.y + TRUNK_HEIGHT * 0.55f - 2f, ARM_WIDTH * 0.4f, ARM_HEIGHT);

        // right arm main
        sr.setColor(CACTUS_GREEN);
        sr.rect(position.x + TRUNK_WIDTH, position.y + TRUNK_HEIGHT * 0.55f, ARM_WIDTH, ARM_HEIGHT * 0.4f);
        sr.rect(position.x + TRUNK_WIDTH, position.y + TRUNK_HEIGHT * 0.55f, ARM_WIDTH * 0.8f, ARM_HEIGHT);

        // right arm highlight
        sr.setColor(highlightGreen);
        sr.rect(position.x + TRUNK_WIDTH + 1f, position.y + TRUNK_HEIGHT * 0.55f, ARM_WIDTH * 0.2f, ARM_HEIGHT);
    }

    @Override public int getZIndex() { return 4; }
}
