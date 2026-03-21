package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// a card that renders a preview image with a colored border when selected
public class SceneryCardElement extends UIElement {

    private Texture previewTexture;
    private final float width;
    private final float height;
    private boolean selected;

    // border drawn by renderShapes() — one element can use BOTH passes
    private static final float BORDER_THICKNESS = 4f;
    private static final Color SELECTED_BORDER  = new Color(1f, 0.90f, 0.10f, 1f);
    private static final Color UNSELECTED_TINT  = new Color(0f, 0f, 0f, 0.4f);

    public SceneryCardElement(float x, float y, float width, float height,
                              String texturePath) {
        super(x, y);
        this.width  = width;
        this.height = height;
        this.selected = false;
        this.zIndex = 50;

        // loads the preview image from assets, same pattern as CarElement
        this.previewTexture = new Texture(Gdx.files.internal(texturePath));

        // linear filtering keeps the preview smooth when scaled up
        this.previewTexture.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // render() is called during the SpriteBatch pass — this is where images go
    @Override
    public void render(SpriteBatch batch) {
        if (!visible) return;

        // draw the preview image scaled to card size
        batch.draw(previewTexture, x, y, width, height);

        // dim unselected cards with a semi-transparent overlay
        if (!selected) {
            Color prev = batch.getColor().cpy();
            batch.setColor(0f, 0f, 0f, 0.45f);
            batch.draw(previewTexture, x, y, width, height);
            batch.setColor(prev);
        }
    }

    // renderShapes() is called during the ShapeRenderer pass — draw the border here
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        if (!visible || !selected) return;

        // draw a gold rectangle border around the selected card
        shapeRenderer.setColor(SELECTED_BORDER);
        float t = BORDER_THICKNESS;
        shapeRenderer.rectLine(x,         y,          x + width, y,          t); // bottom
        shapeRenderer.rectLine(x + width, y,          x + width, y + height, t); // right
        shapeRenderer.rectLine(x + width, y + height, x,         y + height, t); // top
        shapeRenderer.rectLine(x,         y + height, x,         y,          t); // left
    }

    // call this from LevelSelectScene.unload() to avoid leaking GPU memory
    public void dispose() {
        if (previewTexture != null) {
            previewTexture.dispose();
        }
    }
}