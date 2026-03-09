package io.github.inf1009_p10_9.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;


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
        
        loadTexture();
    }

    
    private void loadTexture() {
        try {
            // ✅ Load your player image
            this.texture = new Texture(Gdx.files.internal("cars/car1_stripe_racer.png"));
            // Or try: "images/player.png" depending on your folder structure
        } catch (Exception e) {
            System.err.println("Failed to load player texture: " + e.getMessage());
            this.texture = null;  // Will fall back to blue square
        }
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
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
    
    
}
