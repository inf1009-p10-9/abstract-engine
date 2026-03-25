package io.github.inf1009_p10_9.entities;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


// a single dashed white line in the centre of the road, scrolled downward each frame
public class RoadDashes extends Entity {
    private static final float DASH_LENGTH  = 30f;
    private static final float DASH_WIDTH   = 4f;
    private static final Color LINE_WHITE   = new Color(0.9f, 0.9f, 0.9f, 1f);

    private final float gameWidth;
    private final float gameHeight;
    private final float centerX;

    private final float laneLeft;
    private final float laneRight;




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

    @Override public int getZIndex() { return 1; }
}
