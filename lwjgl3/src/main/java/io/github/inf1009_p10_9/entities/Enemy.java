package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IRenderable;

public class Enemy extends Entity implements IRenderable, ICollidable {

    private Color color;

    public Enemy(float x, float y) {
        super(x, y);
        this.bounds = new Rectangle(x, y, 32, 32);
        this.zIndex = 10;
        this.color = Color.RED; // Red enemy

        // Optionally load texture here
        // this.texture = new Texture("enemy.png");
    }

    @Override
    public void update() {
        // Update bounds to match position
        bounds.setPosition(position.x, position.y);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (texture != null) {
            // Draw texture if available
            batch.draw(texture, position.x, position.y, 32, 32);
        }
        // If no texture, shape will be drawn in renderShapes() instead
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        if (texture == null) {
            // Fallback: draw colored rectangle (red, or orange when hit)
            shapeRenderer.setColor(color);
            shapeRenderer.rect(position.x, position.y, 32, 32);
        }
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void onCollision(ICollidable other) {
        System.out.println("ENEMY COLLIDED WITH: " + other.getClass().getSimpleName());
        GameContext.getOutputManager().getSFXManager().playSound("sound/jump.mp3");
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
