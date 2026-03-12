package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// a car sprite cut from the shared spritesheet that drives across the screen and wraps around
public class CarElement extends UIElement {

    private TextureRegion carRegion;
    private Texture sheet;
    private float speed;
    private float screenWidth;
    private float carWidth;
    private float carHeight;

    // sheetX/Y and regionWidth/Height define the crop from the spritesheet,
    // drawWidth/Height define how large it appears on screen
    public CarElement(float x, float y, float speed,
                      int sheetX, int sheetY, int regionWidth, int regionHeight,
                      float drawWidth, float drawHeight) {
        super(x, y);
        this.speed = speed;
        this.screenWidth = Gdx.graphics.getWidth();
        this.carWidth = drawWidth;
        this.carHeight = drawHeight;
        this.zIndex = -30;

        this.sheet = new Texture(Gdx.files.internal("cars/cars.png"));
        this.carRegion = new TextureRegion(sheet, sheetX, sheetY, regionWidth, regionHeight);
    }

    // moves the car forward and wraps it back to the left edge when it goes off screen
    public void update(float delta) {
        x += speed * delta;
        if (x > screenWidth + carWidth) {
            x = -carWidth;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!visible) return;
        batch.draw(carRegion, x, y, carWidth, carHeight);
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        // sprite only, no shapes
    }

    public void dispose() {
        if (sheet != null) {
            sheet.dispose();
        }
    }
}
