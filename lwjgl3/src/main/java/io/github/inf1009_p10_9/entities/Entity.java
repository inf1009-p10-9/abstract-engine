package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.UUID;

import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IPositionable;
import io.github.inf1009_p10_9.interfaces.IRenderable;

public abstract class Entity implements IPositionable,
                                        IRenderable,
                                        ICollidable  {

    protected String id;
    protected Vector2 position;
    protected boolean active;

    protected Texture texture;
    protected Rectangle bounds;
    protected float width;
    protected float height;
    protected int zIndex;

    public Entity() {
        this(0, 0);
    }

    public Entity(float x, float y) {
        this(x, y, 0, 0);
    }

    public Entity(float x, float y, float width, float height) {
        this(x, y, width, height, 0);
    }

    public Entity(float x, float y, float width, float height, int zIndex) {
        this.id = UUID.randomUUID().toString();
        this.position = new Vector2(x, y);
        this.active = true;
        this.texture = null;
        this.bounds = new Rectangle(x, y, width, height);
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
    }

    public abstract void update();

    @Override
    public void render(SpriteBatch spriteBatch) {
        // Draw texture if available
        // If no texture, shape will be drawn in renderShapes() instead
        if (texture != null) {
            spriteBatch.draw(texture,
                             position.x,
                             position.y,
                             width,
                             height);
        }
    }

    public String getId() {
        return id;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
