package io.github.inf1009_p10_9.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IRenderable;
import io.github.inf1009_p10_9.questions.Question;

public class Gate extends Entity implements IRenderable, ICollidable {
    private String option; // "A" or "B"
    private BitmapFont font;
    private float flashTimer = 0;
    private boolean needsReset = false;
    private static final float FLASH_DURATION = 1.0f; // increased for visibility
    private static final float SPEED = 60f;
    private final float LANE_LEFT;
    private final float LANE_RIGHT;
    private Color defaultColor = Color.TAN;
    private Color color = defaultColor;
    private Gate partner;

    public Gate(float x, float y, float width, float height, String option) {
        super(x, y, width, height, 5);
        this.option = option;
        this.LANE_LEFT = Gdx.graphics.getWidth() * 0.25f;
        this.LANE_RIGHT = Gdx.graphics.getWidth() * 0.70f;
        this.font = new BitmapFont();
        this.font.getData().setScale(2f);
        this.font.setColor(Color.WHITE);
    }

    @Override
    public void update() {
        // move gate downward each frame
        position.y -= SPEED * Gdx.graphics.getDeltaTime();

        // if gate goes off the bottom, wrap to top
        if (position.y + bounds.height < 0) {
            position.y = Gdx.graphics.getHeight();
        }

        // keep collision bounds in sync with position
        bounds.setPosition(position.x, position.y);

        // count down the flash timer
        if (flashTimer > 0) {
            flashTimer -= Gdx.graphics.getDeltaTime();

            // once flash is done, reset color and reposition gate
            if (flashTimer <= 0) {
                color = defaultColor;

                if (needsReset) {
                    needsReset = false;
                    GameContext.getQuestionManager().nextQuestion();
                    reset(); // reset() handles partner internally now
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Question q = GameContext.getQuestionManager().getCurrentQuestion();
        if (q != null) {
            String label = option.equals("A") ? q.getOptionA() : q.getOptionB();
            GlyphLayout layout = new GlyphLayout(font, label);
            float centeredX = position.x + (bounds.width - layout.width) / 2;
            float centeredY = position.y + (bounds.height + layout.height) / 2;
            font.draw(batch, label, centeredX, centeredY);
        }
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.rect(position.x, position.y, bounds.width, bounds.height);
    }

    @Override
    public void onCollision(ICollidable other) {
        if (other instanceof Player) {
            // prevent collision from firing if bank is already finished
            if (needsReset) {
                return;
            }

            // prevent collision from firing if no active question
            if (GameContext.getQuestionManager().getCurrentQuestion() == null) {
                return;
            }

            boolean answeredCorrectly = GameContext.getQuestionManager().checkAnswer(option);

            if (answeredCorrectly) {
                color = Color.LIME;
            } else {
                color = Color.RED;
            }

            flashTimer = FLASH_DURATION;
            needsReset = true;
        }
    }

    public void reset() {
        // this gate decides randomly
        boolean goLeft = Math.random() < 0.5;

        if (goLeft) {
            position.x = LANE_LEFT;
        } else {
            position.x = LANE_RIGHT;
        }

        position.y = Gdx.graphics.getHeight();
        needsReset = false;

        // tell partner to go to the opposite side
        if (partner != null) {
            partner.resetToSide(!goLeft);
        }
    }
    
    public void resetToSide(boolean goLeft) {
        if (goLeft) {
            position.x = LANE_LEFT;
        } else {
            position.x = LANE_RIGHT;
        }

        position.y = Gdx.graphics.getHeight();
        needsReset = false;
    }

    public void setPartner(Gate partner) {
        this.partner = partner;
    }

    public boolean isNeedsReset() {
        return needsReset;
    }

    public void setNeedsReset(boolean needsReset) {
        this.needsReset = needsReset;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public int getCollisionLayer() {
        return 1;
    }

    public void dispose() {
        font.dispose();
    }
}