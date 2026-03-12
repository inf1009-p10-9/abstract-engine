package io.github.inf1009_p10_9.entities;

import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IRenderable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// draws the scrolling road background with grass on the sides, solid lane edges, and a dashed center line
public class Road extends Entity implements IRenderable {
    private static final float SCROLL_SPEED = 60f;
    private static final float DASH_LENGTH = 30f;
    private static final float DASH_GAP = 20f;
    private static final float DASH_WIDTH = 4f;

    private float scrollOffset = 0;

    // screen dimensions cached at construction
    private final float gameWidth;
    private final float gameHeight;

    // lane centers and width, matched to the gate positions in GameScene
    private final float laneLeft;
    private final float laneRight;
    private final float laneWidth = 200f;

    // outer edges of the full road area
    private final float roadLeftEdge;
    private final float roadRightEdge;

    // colors
    private static final Color ASPHALT_GRAY = new Color(0.2f, 0.2f, 0.22f, 1f);
    private static final Color LINE_WHITE   = new Color(0.9f, 0.9f, 0.9f, 1f);
    private static final Color LINE_YELLOW  = new Color(1f, 0.9f, 0.1f, 1f);
    private static final Color GRASS_GREEN  = new Color(0.2f, 0.5f, 0.2f, 1f);

    public Road() {
        this.gameWidth  = Gdx.graphics.getWidth();
        this.gameHeight = Gdx.graphics.getHeight();

        this.laneLeft  = gameWidth * 0.3f;
        this.laneRight = gameWidth * 0.70f;

        this.roadLeftEdge  = laneLeft  - laneWidth / 2;
        this.roadRightEdge = laneRight + laneWidth / 2;

        super.position.set(0, 0);
        super.bounds.set(0, 0, gameWidth, gameHeight);
    }

    // advances the scroll offset and wraps it once a full dash cycle has passed
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
        // road is shapes only
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        // grass on the left
        shapeRenderer.setColor(GRASS_GREEN);
        shapeRenderer.rect(0, 0, roadLeftEdge, gameHeight);

        // asphalt road covering both lanes
        shapeRenderer.setColor(ASPHALT_GRAY);
        float roadWidth = roadRightEdge - roadLeftEdge;
        shapeRenderer.rect(roadLeftEdge, 0, roadWidth, gameHeight);

        // grass on the right
        shapeRenderer.setColor(GRASS_GREEN);
        shapeRenderer.rect(roadRightEdge, 0, gameWidth - roadRightEdge, gameHeight);

        // solid yellow lines on the outer road edges
        shapeRenderer.setColor(LINE_YELLOW);
        shapeRenderer.rectLine(roadLeftEdge,  0, roadLeftEdge,  gameHeight, 4);
        shapeRenderer.rectLine(roadRightEdge, 0, roadRightEdge, gameHeight, 4);

        // dashed white center divider between the two lanes
        shapeRenderer.setColor(LINE_WHITE);
        float centerX = (laneLeft + laneRight) / 2;
        drawDashedLine(shapeRenderer, centerX, scrollOffset);
    }

    // draws a vertical dashed line at x, offset by the current scroll position
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
        return 0; // background layer
    }

    @Override
    public void onCollision(ICollidable other) {
        // the road does not participate in collisions
    }

    @Override
    public int getCollisionLayer() {
        return 0; // separate layer so nothing collides with the road
    }
}
