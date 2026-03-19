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

// a gate that scrolls down the screen displaying one answer option (A or B).
// when the player collides with it, it checks the answer, flashes a result color, then resets.
// collisions are ignored briefly after spawning to prevent instant triggers when gates reappear at the top.
// #TODO: refactor QuestionManager reference to use an interface
public class Gate extends Entity implements IRenderable, ICollidable {
    private final QuestionManager questionManager;
    private String option; // "A" or "B"
    private BitmapFont font;

    // flash state after a collision
    private float flashTimer = 0;
    private boolean needsReset = false;
    private static final float FLASH_DURATION = 1.0f;

    // immunity period after spawning at the top — prevents instant collision before gate scrolls into view
    private float spawnImmunityTimer = 0;
    private static final float SPAWN_IMMUNITY_DURATION = 1.5f;

    private static final float SPEED = 60f;

    private final Color defaultColor = Color.TAN;
    private Color color = defaultColor;

    // the other gate, so both can be repositioned together after a collision
    private Gate partner;

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
        // scroll downward
        position.y -= SPEED * Gdx.graphics.getDeltaTime();

        // wrap back to the top when fully off screen
        if (position.y + bounds.height < 0) {
            position.y = Gdx.graphics.getHeight();
        }

        bounds.setPosition(position.x, position.y);

        // count down spawn immunity so collision activates once the gate is visible on screen
        if (spawnImmunityTimer > 0) {
            spawnImmunityTimer -= Gdx.graphics.getDeltaTime();
        }

        // count down flash timer, then advance the question and reposition
        if (flashTimer > 0) {
            flashTimer -= Gdx.graphics.getDeltaTime();

            if (flashTimer <= 0) {
                color = defaultColor;

                if (needsReset) {
                    needsReset = false;
                    questionManager.nextQuestion();
                    reset();
                }
            }
        }
    }

    // draws the answer option text centered inside the gate
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
            // ignore collision while gate is still spawning in from the top
            if (spawnImmunityTimer > 0) {
                return;
            }

            // ignore repeated collisions while already flashing a result
            if (needsReset || partner.isNeedsReset()) {
                return;
            }

            // ignore if there is no active question
            if (questionManager.getCurrentQuestion() == null) {
                return;
            }

            boolean answeredCorrectly = questionManager.checkAnswer(option);

            if (answeredCorrectly) {
                color = Color.LIME;
                // #TODO: refactor to use constructor injection from GameApplication
                OutputManager.getInstance().getSFXManager().playCorrectAnswerSound();
            } else {
                color = Color.RED;
                // #TODO: refactor to use constructor injection from GameApplication
                OutputManager.getInstance().getSFXManager().playWrongAnswerSound();
            }

            // hold the result color briefly before resetting
            flashTimer = FLASH_DURATION;
            needsReset = true;
        }
    }

    // picks a random side for this gate and sends the partner to the opposite side
    // resets spawn immunity so the gate cannot be triggered immediately after reappearing
    public void reset() {
        boolean goLeft = Math.random() < 0.5;

        if (goLeft) {
            position.x = (float) Gdx.graphics.getWidth() / 2 - 150f - 100f / 2;
        } else {
            position.x = (float) Gdx.graphics.getWidth() / 2 + 100f / 2;
        }

        position.y = Gdx.graphics.getHeight();
        needsReset = false;
        spawnImmunityTimer = SPAWN_IMMUNITY_DURATION;

        if (partner != null) {
            partner.resetToSide(!goLeft);
        }
    }

    // positions this gate on the given side without triggering a partner reset
    // resets spawn immunity so the gate cannot be triggered immediately after reappearing
    public void resetToSide(boolean goLeft) {
        if (goLeft) {
            position.x = (float) Gdx.graphics.getWidth() / 2 - 150f - 100f / 2;
        } else {
            position.x = (float) Gdx.graphics.getWidth() / 2 + 100f / 2;
        }

        position.y = Gdx.graphics.getHeight();
        needsReset = false;
        spawnImmunityTimer = SPAWN_IMMUNITY_DURATION;
    }

    public void setPartner(Gate partner) {
        this.partner = partner;
    }

    public boolean isNeedsReset() {
        return needsReset;
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
