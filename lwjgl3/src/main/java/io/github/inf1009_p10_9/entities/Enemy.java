package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.ISFXPlayable;

public class Enemy extends Entity {

    private Color color;
    private ISFXPlayable sfxPlayable;

    public Enemy(float x, float y, ISFXPlayable sfxPlayable) {
        super(x, y, 32, 32, 10);
        this.color = Color.RED; // Red enemy
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
            // Fallback: draw colored rectangle (red, or orange when hit)
            shapeRenderer.setColor(color);
            shapeRenderer.rect(position.x, position.y, width, height);
        }
    }

    @Override
    public void onCollision(ICollidable other) {
        System.out.println("ENEMY COLLIDED WITH: " + other.getClass().getSimpleName());
        sfxPlayable.playSound("sound/jump.mp3");
        // Flash orange when hit
        color = Color.ORANGE;

        // Return to red after a moment
        // In a real game, use timer
        new Thread(() -> {
            try {
                Thread.sleep(200); // 200ms flash
                color = Color.RED;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public int getCollisionLayer() {
        return 1; // Same layer as player so they can collide
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
