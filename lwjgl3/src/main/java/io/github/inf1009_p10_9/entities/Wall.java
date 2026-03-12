package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009_p10_9.interfaces.ICollidable;

// a static boundary wall that flashes yellow briefly when the player touches it
public class Wall extends Entity {
    private Color currentColor;
    private Color normalColor = Color.GRAY;
    private Color collisionColor = Color.YELLOW;

    // counts down after a collision, returns wall to its normal color when it hits zero
    private float collisionTimer = 0;
    private static final float COLLISION_FLASH_DURATION = 0.3f;

    public Wall(float x, float y, float width, float height) {
        super(x, y, width, height, 5);
        this.currentColor = normalColor;
    }

    @Override
    public void update() {
        bounds.setPosition(position.x, position.y);

        if (collisionTimer > 0) {
            collisionTimer -= Gdx.graphics.getDeltaTime();
            if (collisionTimer <= 0) {
                currentColor = normalColor;
            }
        }
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        if (texture == null) {
            shapeRenderer.setColor(currentColor);
            shapeRenderer.rect(position.x, position.y, width, height);
        }
    }

    @Override
    public void onCollision(ICollidable other) {
        if (other instanceof Player) {
            System.out.println("WALL COLLIDED WITH: Player");
            currentColor = collisionColor;
            collisionTimer = COLLISION_FLASH_DURATION;
        }
    }

    @Override
    public int getCollisionLayer() {
        return 1; // same layer as player
    }

    // allows the normal color to be changed at runtime, also updates current color if not flashing
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
