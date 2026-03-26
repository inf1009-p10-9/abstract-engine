package io.github.inf1009_p10_9.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.inf1009_p10_9.engine.core.Entity;

public class Bones extends Entity {
    private static final Color BONE_COLOR      = new Color(0.92f, 0.90f, 0.82f, 1f);
    private static final Color BONE_SHADOW     = new Color(0.60f, 0.58f, 0.50f, 1f);
    private static final Color BONE_HIGHLIGHT  = new Color(1.00f, 0.98f, 0.93f, 1f);
    private static final Color GROUND_SHADOW   = new Color(0f,   0f,   0f,   0.25f);

    private final float size;

    public Bones(float x, float y, float size) {
        this.size = size;
        super.position.set(x, y);
        super.bounds.set(x, y, size * 2f, size);
    }

    @Override
    public void update() {
        bounds.setPosition(position.x, position.y);
    }

    @Override public void render(SpriteBatch batch) {}

    @Override
    public void renderShapes(ShapeRenderer sr) {
        float cx = position.x;
        float cy = position.y;
        float shaft = size * 1.6f;
        float knob  = size * 0.38f;

        // ground shadow
        sr.setColor(GROUND_SHADOW);
        sr.ellipse(cx - size * 0.1f, cy - size * 0.12f, shaft * 1.1f, size * 0.22f);

        // === BONE 1 (horizontal) ===

        // shaft shadow
        sr.setColor(BONE_SHADOW);
        sr.rect(cx - shaft / 2f + size * 0.05f, cy - size * 0.08f, shaft, size * 0.28f);

        // shaft main
        sr.setColor(BONE_COLOR);
        sr.rect(cx - shaft / 2f, cy, shaft, size * 0.28f);

        // shaft highlight
        sr.setColor(BONE_HIGHLIGHT);
        sr.rect(cx - shaft / 2f + size * 0.1f, cy + size * 0.16f, shaft * 0.6f, size * 0.07f);

        // left knob shadow
        sr.setColor(BONE_SHADOW);
        sr.ellipse(cx - shaft / 2f - knob * 0.3f + size * 0.05f, cy - knob * 0.3f - size * 0.05f, knob * 1.1f, knob * 1.1f);

        // left knob main
        sr.setColor(BONE_COLOR);
        sr.ellipse(cx - shaft / 2f - knob * 0.3f, cy - knob * 0.3f, knob, knob);

        // left knob highlight
        sr.setColor(BONE_HIGHLIGHT);
        sr.ellipse(cx - shaft / 2f - knob * 0.15f, cy - knob * 0.05f, knob * 0.4f, knob * 0.35f);

        // right knob shadow
        sr.setColor(BONE_SHADOW);
        sr.ellipse(cx + shaft / 2f - knob * 0.7f + size * 0.05f, cy - knob * 0.3f - size * 0.05f, knob * 1.1f, knob * 1.1f);

        // right knob main
        sr.setColor(BONE_COLOR);
        sr.ellipse(cx + shaft / 2f - knob * 0.7f, cy - knob * 0.3f, knob, knob);

        // right knob highlight
        sr.setColor(BONE_HIGHLIGHT);
        sr.ellipse(cx + shaft / 2f - knob * 0.55f, cy - knob * 0.05f, knob * 0.4f, knob * 0.35f);

        // === BONE 2 (diagonal cross) ===

        float diagLen = shaft * 0.85f;
        float dx = diagLen * 0.6f;
        float dy = diagLen * 0.5f;

        // diagonal shaft shadow
        sr.setColor(BONE_SHADOW);
        sr.rectLine(cx - dx + size * 0.05f, cy - dy - size * 0.05f,
                    cx + dx + size * 0.05f, cy + dy - size * 0.05f, size * 0.22f);

        // diagonal shaft main
        sr.setColor(BONE_COLOR);
        sr.rectLine(cx - dx, cy - dy, cx + dx, cy + dy, size * 0.22f);

        // diagonal shaft highlight
        sr.setColor(BONE_HIGHLIGHT);
        sr.rectLine(cx - dx * 0.5f, cy - dy * 0.3f, cx + dx * 0.3f, cy + dy * 0.6f, size * 0.06f);

        // top right knob shadow
        sr.setColor(BONE_SHADOW);
        sr.ellipse(cx + dx - knob * 0.5f + size * 0.05f, cy + dy - knob * 0.5f - size * 0.05f, knob * 1.1f, knob * 1.1f);

        // top right knob main
        sr.setColor(BONE_COLOR);
        sr.ellipse(cx + dx - knob * 0.5f, cy + dy - knob * 0.5f, knob, knob);

        // top right knob highlight
        sr.setColor(BONE_HIGHLIGHT);
        sr.ellipse(cx + dx - knob * 0.35f, cy + dy - knob * 0.2f, knob * 0.4f, knob * 0.35f);

        // bottom left knob shadow
        sr.setColor(BONE_SHADOW);
        sr.ellipse(cx - dx - knob * 0.5f + size * 0.05f, cy - dy - knob * 0.5f - size * 0.05f, knob * 1.1f, knob * 1.1f);

        // bottom left knob main
        sr.setColor(BONE_COLOR);
        sr.ellipse(cx - dx - knob * 0.5f, cy - dy - knob * 0.5f, knob, knob);

        // bottom left knob highlight
        sr.setColor(BONE_HIGHLIGHT);
        sr.ellipse(cx - dx - knob * 0.35f, cy - dy - knob * 0.2f, knob * 0.4f, knob * 0.35f);
    }

    @Override public int getZIndex() { return 4; }
}
