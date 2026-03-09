package io.github.inf1009_p10_9.entities;

import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IRenderable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Road extends Entity implements IRenderable {
    private static final float SCROLL_SPEED = 60f;
    private static final float DASH_LENGTH = 30f;
    private static final float DASH_GAP = 20f;
    private static final float DASH_WIDTH = 4f;

    private float scrollOffset = 0;

    // Dynamic screen dimensions
    private final float gameWidth;
    private final float gameHeight;
    
    // Lane positions (matching your gate lanes)
    private final float laneLeft;      // Center of left lane
    private final float laneRight;     // Center of right lane
    private final float laneWidth = 200f;  // Width of each lane
    
    // Road boundaries
    private final float roadLeftEdge;   // Far left edge of road
    private final float roadRightEdge;  // Far right edge of road

    // Colors
    private static final Color ASPHALT_GRAY = new Color(0.2f, 0.2f, 0.22f, 1f);
    private static final Color LINE_WHITE = new Color(0.9f, 0.9f, 0.9f, 1f);
    private static final Color LINE_YELLOW = new Color(1f, 0.9f, 0.1f, 1f);
    private static final Color GRASS_GREEN = new Color(0.2f, 0.5f, 0.2f, 1f);

    public Road() {
        // Get actual screen dimensions
        this.gameWidth = Gdx.graphics.getWidth();
        this.gameHeight = Gdx.graphics.getHeight();
        
        // Match gate lane positions
        this.laneLeft = gameWidth * 0.3f;   // 200 (if width = 800)
        this.laneRight = gameWidth * 0.70f;  // 560 (if width = 800)
        
        // Calculate road edges (from leftmost to rightmost lane edge)
        this.roadLeftEdge = laneLeft - laneWidth / 2;      // Left edge of left lane
        this.roadRightEdge = laneRight + laneWidth / 2;    // Right edge of right lane

        // Update entity bounds to match screen
        super.position.set(0, 0);
        super.bounds.set(0, 0, gameWidth, gameHeight);
    }

    @Override
    public void update() {
        scrollOffset -= SCROLL_SPEED * Gdx.graphics.getDeltaTime();

        float dashCycle = DASH_LENGTH + DASH_GAP;
        if (scrollOffset <= -dashCycle) {
            scrollOffset += dashCycle;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // No texture rendering
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        //Draw GRASS on far left
        shapeRenderer.setColor(GRASS_GREEN);
        shapeRenderer.rect(0, 0, roadLeftEdge, gameHeight);

        //Draw CONTINUOUS ASPHALT ROAD (both lanes together)
        shapeRenderer.setColor(ASPHALT_GRAY);
        float roadWidth = roadRightEdge - roadLeftEdge;
        shapeRenderer.rect(roadLeftEdge, 0, roadWidth, gameHeight);

        //Draw GRASS on far right
        shapeRenderer.setColor(GRASS_GREEN);
        shapeRenderer.rect(roadRightEdge, 0, gameWidth - roadRightEdge, gameHeight);

        //Draw road edge lines (solid yellow) - ONLY on outer edges
        shapeRenderer.setColor(LINE_YELLOW);
        shapeRenderer.rectLine(roadLeftEdge, 0, roadLeftEdge, gameHeight, 4);     // Left edge
        
        shapeRenderer.rectLine(roadRightEdge, 0, roadRightEdge, gameHeight, 4);   // Right edge

        //Draw center divider between the two lanes (white dashed)
        shapeRenderer.setColor(LINE_WHITE);
        float centerX = (laneLeft + laneRight) / 2;  // Middle between the two lanes
        drawDashedLine(shapeRenderer, centerX, scrollOffset);


    }

    private void drawDashedLine(ShapeRenderer shapeRenderer, float x, float offset) {
        float y = offset;

        while (y < gameHeight) {
            float dashEnd = Math.min(y + DASH_LENGTH, gameHeight);

            if (y >= 0 && dashEnd > 0) {
                shapeRenderer.rectLine(x, Math.max(0, y), x, dashEnd, DASH_WIDTH);
            }

            y += DASH_LENGTH + DASH_GAP;
        }
    }

    @Override
    public int getZIndex() {
        return 0;  // Background layer
    }

	@Override
	public void onCollision(ICollidable other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCollisionLayer() {
		// TODO Auto-generated method stub
		return 0;
	}
}