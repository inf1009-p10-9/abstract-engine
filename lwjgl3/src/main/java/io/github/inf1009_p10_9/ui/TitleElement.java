package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;

public class TitleElement extends UIElement {

    private BitmapFont font;
    private String text;
    private Color mainColor;
    private Color shadowColor;
    private float baseY;

    public TitleElement(String text, BitmapFont font, Color mainColor) {
        super(0, 0);
        this.text = text;
        this.font = font;
        this.mainColor = mainColor;
        this.shadowColor = new Color(0f, 0.25f, 0f, 0.5f); // soft dark green, semi-transparent
        this.zIndex = 100;

        GlyphLayout layout = new GlyphLayout(font, text);
        this.x = (Gdx.graphics.getWidth() - layout.width) / 2;
        this.baseY = 580;
        this.y = baseY;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getBaseY() {
        return baseY;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!visible) return;

        // single shadow offset down-right, drawn first
        font.setColor(shadowColor);
        font.draw(batch, text, x + 4, y - 4);

        // main title on top
        font.setColor(mainColor);
        font.draw(batch, text, x, y);
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {}
}