package io.github.inf1009_p10_9.engine.core;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.inf1009_p10_9.engine.interfaces.ICollidableDetectable;
import io.github.inf1009_p10_9.engine.interfaces.IPositionable;
import io.github.inf1009_p10_9.engine.interfaces.IRenderable;

/**
   Base class for all game entities, handles position, bounds, texture, and
   z-ordering.
*/
public abstract class Entity implements IPositionable,
                                        IRenderable,
                                        ICollidableDetectable {
    protected Vector2 position;
    protected boolean active;

    protected Texture texture;
    protected Rectangle bounds;
    protected float width;
    protected float height;
    protected int zIndex;

    // constructors with increasing specificity, all delegate to the full version
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
        this.position = new Vector2(x, y);
        this.active = true;
        this.texture = null;
        this.bounds = new Rectangle(x, y, width, height);
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
    }

    public abstract void update();

    /**
       Draws the texture if one is set, otherwise subclasses handle drawing in
       {@link Entity#renderShapes(com.badlogic.gdx.graphics.glutils.ShapeRenderer)}.
    */
    @Override
    public void render(SpriteBatch spriteBatch) {
        if (texture != null) {
            spriteBatch.draw(texture, position.x, position.y, width, height);
        }
    }

    // getters and setters
    @Override
    public int getZIndex() {
        return zIndex;
    }

    // getters and setters
    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public int getCollisionLayer() {
        return 0; //default collision layer 0
    }

    public boolean isActive() {
        return active;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
