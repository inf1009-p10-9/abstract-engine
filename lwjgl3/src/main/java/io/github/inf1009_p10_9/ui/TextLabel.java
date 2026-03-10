package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TextLabel extends UIElement {
    private String text;
    private BitmapFont font;
    private Color color;
    private boolean ownsFont;

    // default constructor - creates its own BitmapFont (fallback only)
    public TextLabel(String text) {
        super(0, 0);
        this.text = text;
        this.font = new BitmapFont();
        this.color = Color.WHITE;
        this.ownsFont = true;
    }

    // position constructor - creates its own BitmapFont (fallback only)
    public TextLabel(String text, float x, float y) {
        super(x, y);
        this.text = text;
        this.font = new BitmapFont();
        this.font.getData().setScale(2f);
        this.color = Color.WHITE;
        this.ownsFont = true;
    }

    // preferred constructor - uses a shared font from FontManager
    public TextLabel(String text, float x, float y, BitmapFont sharedFont) {
        super(x, y);
        this.text = text;
        this.font = sharedFont;
        this.color = Color.WHITE;
        this.ownsFont = false; // FontManager owns this font, don't dispose it
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!visible) {
            return;
        }
        font.setColor(color);
        font.draw(batch, text, x, y);
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        // no shapes needed for text
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFont(BitmapFont font) {
        if (this.ownsFont && this.font != null && this.font != font) {
            this.font.dispose();
        }
        this.font = font;
        this.ownsFont = false;
    }

    public void dispose() {
        if (ownsFont && font != null) {
            font.dispose();
        }
    }
}