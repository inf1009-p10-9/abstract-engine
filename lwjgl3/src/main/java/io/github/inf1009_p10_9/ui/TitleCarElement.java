package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TitleCarElement extends UIElement {

    private Texture sheet;
    private TextureRegion carRegion;
    private float carWidth;
    private float carHeight;

    public TitleCarElement(float x, float y,
                           int sheetX, int sheetY, int regionWidth, int regionHeight,
                           float drawWidth, float drawHeight) {
        super(x, y);
        this.carWidth = drawWidth;
        this.carHeight = drawHeight;
        this.zIndex = 100;

        this.sheet = new Texture(Gdx.files.internal("cars/cars.png"));
        this.carRegion = new TextureRegion(sheet, sheetX, sheetY, regionWidth, regionHeight);
    }

    public void setY(float y) {
        this.y = y;
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