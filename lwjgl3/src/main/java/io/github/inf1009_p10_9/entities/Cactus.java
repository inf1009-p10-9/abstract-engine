package io.github.inf1009_p10_9.entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IRenderable;

public class Cactus extends Entity implements IRenderable {
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
        sr.setColor(CACTUS_GREEN);

        // trunk
        sr.rect(position.x, position.y, TRUNK_WIDTH, TRUNK_HEIGHT);

        // left arm
        sr.rect(position.x - ARM_WIDTH, position.y + TRUNK_HEIGHT * 0.4f, ARM_WIDTH, ARM_HEIGHT * 0.4f);
        sr.rect(position.x - ARM_WIDTH, position.y + TRUNK_HEIGHT * 0.4f, ARM_WIDTH * 0.8f, ARM_HEIGHT);

        // right arm
        sr.rect(position.x + TRUNK_WIDTH, position.y + TRUNK_HEIGHT * 0.55f, ARM_WIDTH, ARM_HEIGHT * 0.4f);
        sr.rect(position.x + TRUNK_WIDTH, position.y + TRUNK_HEIGHT * 0.55f, ARM_WIDTH * 0.8f, ARM_HEIGHT);
    }

    @Override public int getZIndex() { return 4; }
    @Override public void onCollision(ICollidable other) {}
    @Override public int getCollisionLayer() { return 0; }
}