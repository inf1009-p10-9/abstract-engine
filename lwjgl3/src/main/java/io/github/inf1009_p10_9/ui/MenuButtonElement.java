package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MenuButtonElement extends UIElement {

    private float width;
    private float height;
    private Color color;
    private Color highlightColor;
    private boolean highlighted;

    public MenuButtonElement(float x, float y, float width, float height, Color color, Color highlightColor) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.color = color;
        this.highlightColor = highlightColor;
        this.highlighted = false;
        this.zIndex = 50; // behind text labels (zIndex 100) but above background
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public void render(SpriteBatch batch) {
        // buttons are shapes only
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        if (!visible) return;

        if (highlighted) {
            shapeRenderer.setColor(highlightColor);
        } else {
            shapeRenderer.setColor(color);
        }

        // draw filled rectangle as button background
        shapeRenderer.rect(x, y, width, height);

        // draw slightly darker border by drawing a smaller rect on top
        // this is done by the batch in render() since ShapeRenderer cant stack easily
    }
}