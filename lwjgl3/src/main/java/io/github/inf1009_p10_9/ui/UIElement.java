package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009_p10_9.interfaces.IRenderable;

public abstract class UIElement implements IRenderable {
    protected float x;
    protected float y;
    protected boolean visible;
    protected int zIndex;
    
    public UIElement(float x, float y) {
        this.x = x;
        this.y = y;
        this.visible = true;
        this.zIndex = 100; // UI always on top
    }
    
    @Override
    public abstract void render(SpriteBatch batch);
    
    @Override
    public abstract void renderShapes(ShapeRenderer shapeRenderer);
    
    @Override
    public int getZIndex() {
        return zIndex;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}