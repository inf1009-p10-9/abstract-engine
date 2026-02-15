package io.github.inf1009_p10_9.interfaces;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface IRenderable {
	void render(SpriteBatch batch);
	void renderShapes(ShapeRenderer shapeRenderer);
    int getZIndex(); // For rendering order (lower = background, higher = foreground)
}
