package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;

import java.util.UUID;

public abstract class Entity {
    protected String id;
    protected Vector2 position;
    protected boolean active;
    
    protected Texture texture;
    protected Rectangle bounds;
    protected int zIndex;
    
    public Entity() {
        this.id = UUID.randomUUID().toString();
        this.position = new Vector2(0, 0);
        this.active = true;
        this.texture = null;  
        this.bounds = new Rectangle(0, 0, 0, 0); 
        this.zIndex = 0; 
    }
    
    public Entity(float x, float y) {
        this.id = UUID.randomUUID().toString();
        this.position = new Vector2(x, y);
        this.active = true;
        this.texture = null;  
        this.bounds = new Rectangle(0, 0, 0, 0);  
        this.zIndex = 0; 
    }
    
    public Entity(float x, float y, float width, float height) {
        this.id = UUID.randomUUID().toString();
        this.position = new Vector2(x, y);
        this.active = true;
        this.texture = null;
        this.bounds = new Rectangle(x, y, width, height);
        this.zIndex = 0;
    }
    
    public abstract void update();
    
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
