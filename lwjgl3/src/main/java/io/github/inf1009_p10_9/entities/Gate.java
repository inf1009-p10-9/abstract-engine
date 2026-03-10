package io.github.inf1009_p10_9.entities;

import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.IRenderable;
import io.github.inf1009_p10_9.managers.OutputManager;
import io.github.inf1009_p10_9.questions.QuestionManager;
import io.github.inf1009_p10_9.questions.Question;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Gate extends Entity implements IRenderable, ICollidable {
    private final QuestionManager questionManager;
    private String option; // "A" or "B"
    private BitmapFont font;
    private float flashTimer = 0;
    private boolean needsReset = false;
    private static final float FLASH_DURATION = 1.0f; // increased for visibility
    private static final float SPEED = 60f;

    private Color defaultColor = Color.TAN;
    private Color color = defaultColor;
    private Gate partner;

    // #TODO: Refactor QuestionManager reference
    public Gate(float x, float y, float width, float height, String option, QuestionManager questionManager) {
        super(x, y, width, height, 5);
        this.option = option;
        this.font = new BitmapFont();
        this.font.getData().setScale(2f);
        this.font.setColor(Color.WHITE);
        this.questionManager = questionManager;
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
                    questionManager.nextQuestion();
                    reset(); // reset() handles partner internally now
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Question q = questionManager.getCurrentQuestion();
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
            // Ignore repeated collisions while the gate is waiting to reset
            if (needsReset) {
                return;
            }

            // Ignore collision if there is no active question
            if (questionManager.getCurrentQuestion() == null) {
                return;
            }

            // Check whether the player's chosen gate matches the correct answer
            boolean answeredCorrectly = questionManager.checkAnswer(option);

            if (answeredCorrectly) {
                color = Color.LIME;

                // Play sound for a correct answer
                // #TODO: Refactor this to use constructor injection from GameApplication.
                OutputManager.getInstance().getSFXManager().playCorrectAnswerSound();
            } else {
                color = Color.RED;

                // Play sound for a wrong answer
                // #TODO: Refactor this to use constructor injection from GameApplication.
                OutputManager.getInstance().getSFXManager().playWrongAnswerSound();
            }
            // Keep the result color on screen briefly before resetting
            flashTimer = FLASH_DURATION;
            needsReset = true;
        }
    }

    public void reset() {
        // this gate decides randomly
        boolean goLeft = Math.random() < 0.5;

        if (goLeft) {
            position.x = Gdx.graphics.getWidth()/ 2 - 150f - 100f/2 ;
        } else {
            position.x = Gdx.graphics.getWidth()/ 2 + 100f /2;
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
            position.x = Gdx.graphics.getWidth()/ 2 - 150f - 100f/2 ;
         } else {
             position.x = Gdx.graphics.getWidth()/ 2 + 100f /2;
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
