package io.github.inf1009_p10_9.game.ui;
import io.github.inf1009_p10_9.engine.core.UIElement;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;


// the main title text with a drop shadow, centered horizontally and bounced vertically by StartScene
public class TitleElement extends UIElement {

    private BitmapFont font;
    private String text;
    private Color mainColor;
    private Color shadowColor;
    private float baseY; // the resting y position before any bounce offset is applied

    public TitleElement(String text, BitmapFont font, Color mainColor) {
        super(0, 0);
        this.text = text;
        this.font = font;
        this.mainColor = mainColor;
        this.shadowColor = new Color(0f, 0.25f, 0f, 0.5f); // soft dark green, semi-transparent
        this.zIndex = 100;

        // center horizontally based on measured text width
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

        // shadow drawn first, slightly offset down and to the right
        font.setColor(shadowColor);
        font.draw(batch, text, x + 4, y - 4);

        // main text drawn on top
        font.setColor(mainColor);
        font.draw(batch, text, x, y);
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        // text only, no shapes
    }
}
