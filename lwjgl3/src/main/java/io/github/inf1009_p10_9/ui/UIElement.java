package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009_p10_9.interfaces.IRenderable;

// base class for all ui elements, handles position, visibility, and z-ordering
public abstract class UIElement implements IRenderable {
    protected float x;
    protected float y;
    protected boolean visible;
    protected int zIndex;

    public UIElement(float x, float y) {
        this.x = x;
        this.y = y;
        this.visible = true;
        this.zIndex = 100; // ui renders on top by default
    }

    @Override
    public abstract void render(SpriteBatch batch);

    @Override
    public abstract void renderShapes(ShapeRenderer shapeRenderer);

    // position
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }
    public float getY() { return y; }

    // visibility
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    // z-ordering
    @Override
    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
