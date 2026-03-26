package io.github.inf1009_p10_9.game.ui;
import io.github.inf1009_p10_9.engine.core.UIElement;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PauseOverlay extends UIElement {
    private final BitmapFont font;
    private final float screenWidth;
    private final float screenHeight;

    // panel dimensions
    private final float panelWidth  = 400f;
    private final float panelHeight = 250f;

    // colors
    private static final Color OVERLAY_COLOR  = new Color(0f,    0f,    0f,    0.75f);
    private static final Color PANEL_COLOR     = new Color(0.12f, 0.12f, 0.15f, 1f);
    private static final Color PANEL_BORDER    = new Color(1.0f,  0.55f, 0.0f,  1f);  // vibrant orange
    private static final Color TITLE_COLOR     = new Color(1.0f,  0.55f, 0.0f,  1f);  // vibrant orange
    private static final Color HINT_COLOR      = new Color(0.85f, 0.85f, 0.85f, 1f);
    private static final Color SEPARATOR_COLOR = new Color(1.0f,  0.55f, 0.0f,  0.5f);

    public PauseOverlay(float screenWidth, float screenHeight, BitmapFont font) {
        super(0, 0);
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;
        this.font         = font;
        this.zIndex       = 200;
        this.visible      = false;
    }

    @Override
    public void renderShapes(ShapeRenderer sr) {
        if (!visible) return;

        float panelX = (screenWidth  - panelWidth)  / 2f;
        float panelY = (screenHeight - panelHeight) / 2f;

        // full screen dark overlay
        sr.setColor(OVERLAY_COLOR);
        sr.rect(0, 0, screenWidth, screenHeight);

        // panel background
        sr.setColor(PANEL_COLOR);
        sr.rect(panelX, panelY, panelWidth, panelHeight);

        // orange border around panel
        float borderThickness = 3f;
        sr.setColor(PANEL_BORDER);
        sr.rectLine(panelX, panelY, panelX + panelWidth, panelY, borderThickness);                               // bottom
        sr.rectLine(panelX, panelY + panelHeight, panelX + panelWidth, panelY + panelHeight, borderThickness);   // top
        sr.rectLine(panelX, panelY, panelX, panelY + panelHeight, borderThickness);                              // left
        sr.rectLine(panelX + panelWidth, panelY, panelX + panelWidth, panelY + panelHeight, borderThickness);    // right

        // separator line below title
        sr.setColor(SEPARATOR_COLOR);
        sr.rectLine(panelX + 20f, panelY + panelHeight - 80f, panelX + panelWidth - 20f, panelY + panelHeight - 80f, 1.5f);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!visible) return;

        float panelX = (screenWidth  - panelWidth)  / 2f;
        float panelY = (screenHeight - panelHeight) / 2f;
        float centerX = screenWidth / 2f;

        // PAUSED title
        font.setColor(TITLE_COLOR);
        GlyphLayout pausedLayout = new GlyphLayout(font, "PAUSED");
        font.draw(batch, "PAUSED",
            centerX - pausedLayout.width / 2f,
            panelY + panelHeight - 30f);

        // separator gap then hints
        font.setColor(HINT_COLOR);

        GlyphLayout resumeLayout = new GlyphLayout(font, "ESC:  Resume");
        font.draw(batch, "ESC:  Resume",
            centerX - resumeLayout.width / 2f,
            panelY + panelHeight - 110f);

        GlyphLayout quitLayout = new GlyphLayout(font, "Q:  Quit to menu");
        font.draw(batch, "Q:  Quit to menu",
            centerX - quitLayout.width / 2f,
            panelY + panelHeight - 160f);
    }
}