package io.github.inf1009_p10_9.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.inf1009_p10_9.engine.core.Entity;

import com.badlogic.gdx.graphics.Texture;


// the player entity, renders as a car sprite with a blue square fallback if the texture fails to load
public class Player extends Entity {
    private Color color;
    private final String texturePath;

    public Player(float x, float y) {
        this(x, y, null);
    }

    public Player(float x, float y, String texturePath) {
        super(x, y, 52, 95, 10);
        this.color = Color.BLUE;
        this.texturePath = texturePath;
        loadTexture();
    }

    // attempts to load the car sprite, falls back to a plain blue square if it fails
    private void loadTexture() {
        try {
            this.texture = new Texture(Gdx.files.internal(texturePath));
        } catch (Exception e) {
            System.err.println("Failed to load player texture: " + e.getMessage());
            this.texture = null;
        }
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

    @Override public int getCollisionLayer() {
        return 1;
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
