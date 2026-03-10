package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;

public class CloudElement extends UIElement {

    private float speed;
    private float width;
    private float height;
    private float screenWidth;

    public CloudElement(float x, float y, float width, float height, float speed) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.screenWidth = Gdx.graphics.getWidth();
        this.zIndex = -50; // above background, below text
    }

    public void update(float delta) {
        // drift left across screen
        x -= speed * delta;

        // wrap back to right side when fully off screen
        if (x + width < 0) {
            x = screenWidth + width;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // clouds are shapes only
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        if (!visible) return;

        shapeRenderer.setColor(Color.WHITE);

        // draw cloud as three overlapping ovals
        float r1 = height * 0.6f;
        float r2 = height * 0.8f;
        float r3 = height * 0.5f;

        shapeRenderer.ellipse(x, y, r1 * 2, height);
        shapeRenderer.ellipse(x + r1 * 0.8f, y + height * 0.2f, r2 * 2, height * 1.2f);
        shapeRenderer.ellipse(x + r1 * 1.8f, y, r3 * 2, height * 0.9f);
    }
}