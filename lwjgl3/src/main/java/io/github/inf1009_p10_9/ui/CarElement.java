package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CarElement extends UIElement {

    private TextureRegion carRegion;
    private Texture sheet;
    private float speed;
    private float screenWidth;
    private float carWidth;
    private float carHeight;

    public CarElement(float x, float y, float speed,
                      int sheetX, int sheetY, int regionWidth, int regionHeight,
                      float drawWidth, float drawHeight) {
        super(x, y);
        this.speed = speed;
        this.screenWidth = Gdx.graphics.getWidth();
        this.carWidth = drawWidth;
        this.carHeight = drawHeight;
        this.zIndex = -30;

        // load sheet and cut out the specific car
        this.sheet = new Texture(Gdx.files.internal("cars/cars.png"));
        this.carRegion = new TextureRegion(sheet, sheetX, sheetY, regionWidth, regionHeight);
    }

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
        // sprite only
    }

    public void dispose() {
        if (sheet != null) {
            sheet.dispose();
        }
    }
}