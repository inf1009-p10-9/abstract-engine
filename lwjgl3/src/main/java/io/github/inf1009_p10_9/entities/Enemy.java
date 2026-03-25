package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.inf1009_p10_9.interfaces.ICollidableDetectable;
import io.github.inf1009_p10_9.interfaces.ISFXPlayable;

// a red enemy entity that briefly flashes orange when hit
public class Enemy extends Entity {

    private Color color;
    private ISFXPlayable sfxPlayable;

    public Enemy(float x, float y, ISFXPlayable sfxPlayable) {
        super(x, y, 32, 32, 10);
        this.color = Color.RED;
        this.sfxPlayable = sfxPlayable;
    }

    @Override
    public void update() {
        bounds.setPosition(position.x, position.y);
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        if (texture == null) {
            shapeRenderer.setColor(color);
            shapeRenderer.rect(position.x, position.y, width, height);
        }
    }

    @Override
    public int getCollisionLayer() {
        return 1; // same layer as player so they can collide
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
