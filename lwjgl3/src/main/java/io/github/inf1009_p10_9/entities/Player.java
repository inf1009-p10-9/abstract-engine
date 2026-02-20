package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IRenderable;
import io.github.inf1009_p10_9.interfaces.ISFXPlayable;

public class Player extends Entity {
    private Color color;
    private ISFXPlayable sfxPlayable;

    public Player(float x, float y, ISFXPlayable sfxPlayable) {
        super(x, y, 32, 32, 10);
        this.color = Color.BLUE;
        this.sfxPlayable = sfxPlayable;
    }

    @Override
    public void update() {
        // Update bounds to match position
        bounds.setPosition(position.x, position.y);
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        if (texture == null) {
            // Fallback: draw colored rectangle (equivalent to renderer.drawRectangle)
            shapeRenderer.setColor(color);
            shapeRenderer.rect(position.x, position.y, width, height);
        }
    }

    @Override
    public void onCollision(ICollidable other) {
        System.out.println("Player collided with: " + other.getClass().getSimpleName());
        sfxPlayable.playSound("sound/jump.mp3");
    }

    @Override
    public int getCollisionLayer() {
        return 1; // Player collision layer
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
