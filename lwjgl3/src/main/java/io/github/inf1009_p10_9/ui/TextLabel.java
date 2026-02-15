package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TextLabel extends UIElement {
    private String text;
    private BitmapFont font;
    private Color color;

    public TextLabel(String text) {
        super(0, 0);
        this.text = text;
        this.font = new BitmapFont();
        this.color = Color.WHITE;
    }

    public TextLabel(String text, float x, float y) {
        super(x, y);
        this.text = text;
        this.font = new BitmapFont();
        this.color = Color.WHITE;

        // Make font bigger
        font.getData().setScale(2f);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!visible) {
            return;
        }

        // Text draws itself using BitmapFont with SpriteBatch
        font.setColor(color);
        font.draw(batch, text, x, y);
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        // TextLabel doesn't need shape rendering
        // This method can be empty since text is drawn with SpriteBatch
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFont(BitmapFont font) {
        if (this.font != null && this.font != font) {
            this.font.dispose();
        }
        this.font = font;
    }

    public void dispose() {
        if (font != null) {
            font.dispose();
        }
    }
}