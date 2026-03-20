package io.github.inf1009_p10_9.entities;
import com.badlogic.gdx.graphics.Color;
import io.github.inf1009_p10_9.interfaces.IRenderable;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class RoadDashes extends Entity implements IRenderable {
    private static final float SCROLL_SPEED = 60f;
    private static final float DASH_LENGTH  = 30f;
    private static final float DASH_GAP     = 20f;
    private static final float DASH_WIDTH   = 4f;
    private static final Color LINE_WHITE   = new Color(0.9f, 0.9f, 0.9f, 1f);

    private float scrollOffset = 0;
    private final float gameWidth;
    private final float gameHeight;
    private final float centerX;
    
    private final float laneLeft;
    private final float laneRight;
    
    private float position_y;
    
    

    public RoadDashes(float startY) {
    	this.gameWidth = Gdx.graphics.getWidth();
        this.gameHeight = Gdx.graphics.getHeight();
        this.laneLeft  = gameWidth * 0.3f;
        this.laneRight = gameWidth * 0.70f;
        this.centerX = (laneLeft + laneRight) / 2;
        super.position.set(centerX, startY);
        super.bounds.set(centerX, startY , DASH_WIDTH, gameHeight);
        
    }


    @Override
    public void update() {
        bounds.setPosition(position.x, position.y);
    }
    
    @Override public void render(SpriteBatch batch) {}

    @Override
    public void renderShapes(ShapeRenderer sr) {
        sr.setColor(LINE_WHITE);
        sr.rectLine(centerX, position.y, centerX, position.y + DASH_LENGTH, DASH_WIDTH);
        
    }

    @Override public int getZIndex() { return 2; }
    @Override public void onCollision(ICollidable other) {}
    @Override public int getCollisionLayer() { return 0; }
}