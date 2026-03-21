package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IRenderable;

public class Building extends Entity implements IRenderable {

    private final float buildingWidth;
    private final float buildingHeight;
    private final Color wallColor;
    private final Color shadowColor;
    private final Color windowColor;
    private final Color roofColor;

    public Building(float x, float y, float width, float height, Color wallColor) {
        this.buildingWidth  = width;
        this.buildingHeight = height;
        this.wallColor      = wallColor;
        this.shadowColor    = new Color(wallColor.r * 0.6f, wallColor.g * 0.6f, wallColor.b * 0.6f, 1f);
        this.windowColor    = new Color(0.85f, 0.90f, 1.0f, 1f);
        this.roofColor      = new Color(wallColor.r * 0.75f, wallColor.g * 0.75f, wallColor.b * 0.75f, 1f);
        super.position.set(x, y);
        super.bounds.set(x, y, width, height);
    }

    @Override
    public void update() {
        bounds.setPosition(position.x, position.y);
    }

    @Override public void render(SpriteBatch batch) {}

    @Override
    public void renderShapes(ShapeRenderer sr) {
        float x = position.x;
        float y = position.y;

        // shadow on the right side for depth
        sr.setColor(shadowColor);
        sr.rect(x + buildingWidth * 0.85f, y, buildingWidth * 0.15f, buildingHeight);

        // main wall
        sr.setColor(wallColor);
        sr.rect(x, y, buildingWidth * 0.85f, buildingHeight);

        // roof
        sr.setColor(roofColor);
        sr.rect(x, y + buildingHeight, buildingWidth, buildingHeight * 0.08f);

        // highlight strip on left edge
        sr.setColor(new Color(wallColor.r * 1.2f, wallColor.g * 1.2f, wallColor.b * 1.2f, 1f));
        sr.rect(x, y, buildingWidth * 0.05f, buildingHeight);

        // windows - 2 columns, multiple rows
        sr.setColor(windowColor);
        float winWidth  = buildingWidth * 0.18f;
        float winHeight = buildingHeight * 0.08f;
        float colA = x + buildingWidth * 0.15f;
        float colB = x + buildingWidth * 0.55f;
        float rowGap = buildingHeight * 0.13f;

        for (int row = 0; row < 5; row++) {
            float wy = y + buildingHeight * 0.1f + row * rowGap;
            sr.rect(colA, wy, winWidth, winHeight);
            sr.rect(colB, wy, winWidth, winHeight);
        }
    }

    @Override public int getZIndex() { return 3; }
    @Override public void onCollision(ICollidable other) {}
    @Override public int getCollisionLayer() { return 0; }
}