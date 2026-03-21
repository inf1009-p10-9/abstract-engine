package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IRenderable;

public class StreetLamp extends Entity implements IRenderable {
    private static final Color POLE_COLOR  = new Color(0.55f, 0.55f, 0.55f, 1f);
    private static final Color LIGHT_COLOR = new Color(1.0f,  0.95f, 0.60f, 1f);
    private static final Color GLOW_COLOR  = new Color(1.0f,  0.95f, 0.60f, 0.3f);

    private static final float POLE_WIDTH   = 6f;
    private static final float POLE_HEIGHT  = 70f;
    private static final float ARM_LENGTH   = 20f;
    private static final float LIGHT_RADIUS = 8f;

    // which side the arm extends toward the road
    private final boolean facingRight;

    public StreetLamp(float x, float y, boolean facingRight) {
        this.facingRight = facingRight;
        super.position.set(x, y);
        super.bounds.set(x, y, POLE_WIDTH, POLE_HEIGHT);
    }

    @Override
    public void update() {
        bounds.setPosition(position.x, position.y);
    }

    @Override public void render(SpriteBatch batch) {}

    @Override
    public void renderShapes(ShapeRenderer sr) {
    	float poleX = position.x;
        float poleY = position.y;

        // pole shadow
        sr.setColor(new Color(0.25f, 0.25f, 0.25f, 1f));
        sr.rect(poleX + 3f, poleY, POLE_WIDTH, POLE_HEIGHT);

        // pole main
        sr.setColor(POLE_COLOR);
        sr.rect(poleX, poleY, POLE_WIDTH, POLE_HEIGHT);

        // pole highlight strip
        sr.setColor(new Color(0.75f, 0.75f, 0.75f, 1f));
        sr.rect(poleX + 1f, poleY, POLE_WIDTH * 0.3f, POLE_HEIGHT);

        // arm shadow
        float armX = facingRight ? poleX + POLE_WIDTH : poleX - ARM_LENGTH;
        sr.setColor(new Color(0.25f, 0.25f, 0.25f, 1f));
        sr.rect(armX + 2f, poleY + POLE_HEIGHT - POLE_WIDTH - 2f, ARM_LENGTH, POLE_WIDTH);

        // arm main
        sr.setColor(POLE_COLOR);
        sr.rect(armX, poleY + POLE_HEIGHT - POLE_WIDTH, ARM_LENGTH, POLE_WIDTH);

        
        
        // light position
        float lightX = facingRight ? poleX + POLE_WIDTH + ARM_LENGTH : poleX - ARM_LENGTH;
        float lightY  = poleY + POLE_HEIGHT;

        // outer glow
        sr.setColor(new Color(1.0f, 0.95f, 0.60f, 0.15f));
        sr.circle(lightX, lightY, LIGHT_RADIUS * 3f, 16);

        // mid glow
        sr.setColor(new Color(1.0f, 0.95f, 0.60f, 0.25f));
        sr.circle(lightX, lightY, LIGHT_RADIUS * 2f, 16);

        // bulb shadow
        sr.setColor(new Color(0.70f, 0.65f, 0.30f, 1f));
        sr.circle(lightX + 2f, lightY - 2f, LIGHT_RADIUS, 16);

        // bulb main
        sr.setColor(LIGHT_COLOR);
        sr.circle(lightX, lightY, LIGHT_RADIUS, 16);

        // bulb highlight
        sr.setColor(new Color(1.0f, 1.0f, 0.90f, 1f));
        sr.circle(lightX - 2f, lightY + 2f, LIGHT_RADIUS * 0.4f, 12);
    }

    @Override public int getZIndex() { return 4; }
    @Override public void onCollision(ICollidable other) {}
    @Override public int getCollisionLayer() { return 0; }
}