package io.github.inf1009_p10_9.game.entities;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.inf1009_p10_9.engine.core.Entity;

// draws the green grass strips on either side of the road
public class RoadSurrounding extends Entity {
    private final Color color;

    private final float gameWidth;
    private final float gameHeight;
    private final float roadLeftEdge;
    private final float roadRightEdge;
    private final float laneWidth = 200f;

    public RoadSurrounding(String side, Color color) {
    	this.color = color;

        this.gameWidth    = Gdx.graphics.getWidth();
        this.gameHeight   = Gdx.graphics.getHeight();

        this.roadLeftEdge  = gameWidth * 0.3f  - laneWidth / 2;
        this.roadRightEdge = gameWidth * 0.70f + laneWidth / 2;

        if (side.equals("left")) {
        	super.position.set(0, 0);
        	super.bounds.set(0, 0, roadLeftEdge, gameHeight);
        }

        else if (side.equals("right")) {
        	super.position.set(roadRightEdge, 0);
        	super.bounds.set(roadRightEdge, 0, gameWidth - roadRightEdge, gameHeight);
        }
    }

    @Override public void update() {
        bounds.setPosition(position.x, position.y);
    }

    @Override public void render(SpriteBatch batch) {}

    @Override
    public void renderShapes(ShapeRenderer sr) {
        sr.setColor(color);
        sr.rect(0, 0, roadLeftEdge, gameHeight);
        sr.rect(roadRightEdge, 0, gameWidth - roadRightEdge, gameHeight);
    }

    @Override public int getZIndex() { return 2; }

    @Override public int getCollisionLayer() { return 1; }
}
