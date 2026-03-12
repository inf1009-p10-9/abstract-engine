package io.github.inf1009_p10_9.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// anything that can be drawn each frame, shapes first then textures and text on top
public interface IRenderable {
    void render(SpriteBatch batch);
    void renderShapes(ShapeRenderer shapeRenderer);
    int getZIndex(); // lower values render behind, higher values render in front
}
