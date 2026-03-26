package io.github.inf1009_p10_9.game.ui;
import io.github.inf1009_p10_9.engine.core.UIElement;

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

    private static final float BORDER_THICKNESS = 4f;
    private static final Color SELECTED_BORDER  = new Color(1f, 0.90f, 0.10f, 1f);

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

    @Override
    public void render(SpriteBatch batch) {
        if (!visible) return;

        // draw the full card image
        batch.draw(previewTexture, x, y, width, height);

        // dim the whole card more heavily when not selected
        if (!selected) {
            Color prev = batch.getColor().cpy();
            batch.setColor(0f, 0f, 0f, 0.55f);
            batch.draw(previewTexture, x, y, width, height);
            batch.setColor(prev);
        }

     // scrim drawn only behind the text area in the middle of the card, not the full bottom
        float textAreaY  = y + height * 0.35f; // where the hint text sits
        float scrimHeight = height * 0.10f;    // tall enough to cover hint + name label
        Color prev = batch.getColor().cpy();
        batch.setColor(0f, 0f, 0f, 0.65f);
        batch.draw(previewTexture, x, textAreaY, width, scrimHeight);
        batch.setColor(prev);
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        if (!visible) return;

        // gold border for selected, dim gray for unselected
        if (selected) {
            shapeRenderer.setColor(SELECTED_BORDER);
        } else {
            shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0.6f);
        }

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
