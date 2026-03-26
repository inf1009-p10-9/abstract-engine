package io.github.inf1009_p10_9.game.ui;
import io.github.inf1009_p10_9.engine.core.UIElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// a small static car sprite shown next to the title, moves with it during the bounce animation
public class TitleCarElement extends UIElement {

    private Texture sheet;
    private TextureRegion carRegion;
    private float carWidth;
    private float carHeight;

    // sheetX/Y and regionWidth/Height define the crop from the spritesheet,
    // drawWidth/Height define how large it appears on screen
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
        // sprite only, no shapes
    }

    public void dispose() {
        if (sheet != null) {
            sheet.dispose();
        }
    }
}
