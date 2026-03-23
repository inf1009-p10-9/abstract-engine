package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class PauseOverlay extends UIElement {
    private final BitmapFont font;
    private final float screenWidth;
    private final float screenHeight;

    public PauseOverlay(float screenWidth, float screenHeight, BitmapFont font) {
        super(0, 0);
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;
        this.font         = font;
        this.zIndex       = 200; // renders on top of everything
        this.visible      = false; // hidden by default
    }

    @Override
    public void renderShapes(ShapeRenderer sr) {
        if (!visible) return;


        // enable blending for transparency

        // dark semi-transparent overlay - the 0.5f is the opacity
        sr.setColor(new Color(0f, 0f, 0f, 0.8f));
        sr.rect(0, 0, screenWidth, screenHeight);

        // disable blending after
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!visible) return;

        // PAUSED text centered on screen
        font.setColor(Color.WHITE);
        GlyphLayout layout = new GlyphLayout(font, "PAUSED");
        float textX = (screenWidth - layout.width) / 2;
        float textY = (screenHeight + layout.height) / 2;
        font.draw(batch, "PAUSED", textX, textY);

        // hint text
        font.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
        GlyphLayout hintLayout = new GlyphLayout(font, "Press ESC to resume");
        float hintX = (screenWidth - hintLayout.width) / 2;
        float hintY = textY - 50f;
        font.draw(batch, "Press ESC to resume", hintX, hintY);
    }
}