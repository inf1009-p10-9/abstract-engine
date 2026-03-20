package io.github.inf1009_p10_9.entities;
import com.badlogic.gdx.graphics.Color;
import io.github.inf1009_p10_9.interfaces.IRenderable;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RoadSurrounding extends Entity implements IRenderable {
    private static final Color GRASS_GREEN = new Color(0.2f, 0.5f, 0.2f, 1f);
    
    private final float gameWidth;
    private final float gameHeight;
    private final float roadLeftEdge;
    private final float roadRightEdge;
    private final float laneWidth = 200f;

    public RoadSurrounding() {
        this.gameWidth    = Gdx.graphics.getWidth();
        this.gameHeight   = Gdx.graphics.getHeight();
        


        this.roadLeftEdge  = gameWidth * 0.3f  - laneWidth / 2;
        this.roadRightEdge = gameWidth * 0.70f + laneWidth / 2;
        

        super.position.set(0, 0);
        super.bounds.set(0, 0, gameWidth, gameHeight);
    }

    @Override public void update() {
        bounds.setPosition(position.x, position.y);
    }

    @Override public void render(SpriteBatch batch) {}

    @Override
    public void renderShapes(ShapeRenderer sr) {
        sr.setColor(GRASS_GREEN);
        sr.rect(0, 0, roadLeftEdge, gameHeight);
        sr.rect(roadRightEdge, 0, gameWidth - roadRightEdge, gameHeight);
    }

    @Override public int getZIndex() { return 2; }
    @Override public void onCollision(ICollidable other) {}
    @Override public int getCollisionLayer() { return 0; }
}