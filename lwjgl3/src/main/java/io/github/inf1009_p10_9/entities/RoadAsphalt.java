package io.github.inf1009_p10_9.entities;
import com.badlogic.gdx.graphics.Color;
import io.github.inf1009_p10_9.interfaces.IRenderable;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RoadAsphalt extends Entity implements IRenderable {
    private static final Color ASPHALT_GRAY  = new Color(0.2f, 0.2f, 0.22f, 1f);
    private static final Color LINE_YELLOW   = new Color(1f, 0.9f, 0.1f, 1f);
    
    private final float gameWidth;
    private final float gameHeight;
    private final float roadLeftEdge;
    private final float roadRightEdge;
    private final float laneWidth = 200f;

    public RoadAsphalt() {
    	this.gameWidth    = Gdx.graphics.getWidth();
        this.gameHeight    = Gdx.graphics.getHeight();
        this.roadLeftEdge  = gameWidth * 0.3f  - laneWidth / 2;
        this.roadRightEdge = gameWidth * 0.70f + laneWidth / 2;
        super.position.set(roadLeftEdge, 0);
        super.bounds.set(roadLeftEdge, 0, roadRightEdge - roadLeftEdge, gameHeight);
    }

    @Override public void update() {
        bounds.setPosition(position.x, position.y);
    }

    @Override public void render(SpriteBatch batch) {}

    @Override
    public void renderShapes(ShapeRenderer sr) {
        // asphalt slab
        sr.setColor(ASPHALT_GRAY);
        sr.rect(roadLeftEdge, 0, roadRightEdge - roadLeftEdge, gameHeight);

        // outer yellow edge lines
        sr.setColor(LINE_YELLOW);
        sr.rectLine(roadLeftEdge,  0, roadLeftEdge,  gameHeight, 4);
        sr.rectLine(roadRightEdge, 0, roadRightEdge, gameHeight, 4);
    }

    @Override public int getZIndex() { return 0; }
    @Override public void onCollision(ICollidable other) {}
    @Override public int getCollisionLayer() { return 0; }
}