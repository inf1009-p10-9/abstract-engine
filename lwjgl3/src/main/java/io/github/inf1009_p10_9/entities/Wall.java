package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IRenderable;

public class Wall extends Entity implements IRenderable, ICollidable {
    private float width;
    private float height;
    private Color currentColor;
    private Color normalColor = Color.GRAY;
    private Color collisionColor = Color.YELLOW;
    private float collisionTimer = 0;
    private static final float COLLISION_FLASH_DURATION = 0.3f;

    public Wall(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
        this.zIndex = 5; // Behind player
        this.currentColor = normalColor;

        // Optionally load texture here
        // this.texture = new Texture("wall.png");
    }

    @Override
    public void update() {
        // Walls don't move, but update bounds just in case
        bounds.setPosition(position.x, position.y);

        // Handle color flash timer
        if (collisionTimer > 0) {
            collisionTimer -= com.badlogic.gdx.Gdx.graphics.getDeltaTime();
            if (collisionTimer <= 0) {
                // Timer expired, return to normal color
                currentColor = normalColor;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (texture != null) {
            // Draw texture if available
            batch.draw(texture, position.x, position.y, width, height);
        }
        // If no texture, shape will be drawn in renderShapes() instead
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        if (texture == null) {
            // Fallback: draw colored rectangle (gray normally, yellow when hit)
            shapeRenderer.setColor(currentColor);
            shapeRenderer.rect(position.x, position.y, width, height);
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
        // Ignore wall-to-wall collisions
        if (other.getClass().getSimpleName().equals("Wall")) {
            return;
        }

        // Change color when hit by player
        if (other.getClass().getSimpleName().equals("Player")) {
            System.out.println("WALL COLLIDED WITH: Player");
            currentColor = collisionColor;
            collisionTimer = COLLISION_FLASH_DURATION;
        }
    }

    @Override
    public int getCollisionLayer() {
        return 1; // Same collision layer as Player
    }


    public void setNormalColor(Color color) {
        this.normalColor = color;
        if (collisionTimer <= 0) {
            this.currentColor = color;
        }
    }


    public void setCollisionColor(Color color) {
        this.collisionColor = color;
    }


    public Color getCurrentColor() {
        return currentColor;
    }
}
