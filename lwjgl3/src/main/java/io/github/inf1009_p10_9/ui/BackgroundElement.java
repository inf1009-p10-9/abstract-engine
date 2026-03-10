package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class BackgroundElement extends UIElement {

    public BackgroundElement() {
        super(0, 0);
        this.zIndex = -100; // renders before everything else
    }

    @Override
    public void render(SpriteBatch batch) {
        // background is drawn in renderShapes, nothing here
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float groundHeight = screenHeight * 0.15f;

        // sky gradient - horizontal strips fading from deep blue at top to light blue at horizon
        int skyStrips = 40;
        float stripHeight = (screenHeight - groundHeight) / skyStrips;
        for (int i = 0; i < skyStrips; i++) {
            float t = (float) i / skyStrips;
            float r = 0.4f + t * 0.4f;
            float g = 0.7f + t * 0.2f;
            float b = 1.0f;
            shapeRenderer.setColor(r, g, b, 1f);
            shapeRenderer.rect(0, groundHeight + i * stripHeight, screenWidth, stripHeight + 1);
        }

        // bright grass ground
        shapeRenderer.setColor(0.3f, 0.75f, 0.2f, 1f);
        shapeRenderer.rect(0, 0, screenWidth, groundHeight);

        // darker horizon strip for depth
        shapeRenderer.setColor(0.2f, 0.6f, 0.15f, 1f);
        shapeRenderer.rect(0, groundHeight - 10, screenWidth, 20);
    }
}