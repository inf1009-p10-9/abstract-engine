package io.github.inf1009_p10_9.game.ui;
import io.github.inf1009_p10_9.engine.core.UIElement;
import io.github.inf1009_p10_9.game.managers.QuestionManager;
import io.github.inf1009_p10_9.game.questions.Question;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;

// draws the current question text centered at the top of the game screen
// #TODO: refactor QuestionManager to interface
public class QuestionDisplay extends UIElement {
    private final QuestionManager questionManager;
    private BitmapFont font;

    // font is shared from FontManager, this class must not dispose it
    public QuestionDisplay(float x, float y, QuestionManager questionManager, BitmapFont sharedFont) {
        super(x, y);
        this.font = sharedFont;
        this.zIndex = 20;
        this.questionManager = questionManager;
    }

    // fetches the current question each frame and draws it centered horizontally
    @Override
    public void render(SpriteBatch batch) {
        if (!visible) return;
        Question q = questionManager.getCurrentQuestion();
        if (q != null) {
            GlyphLayout layout = new GlyphLayout(font, q.getQuestion());
            float centeredX = (Gdx.graphics.getWidth() - layout.width) / 2;
            font.setColor(Color.WHITE);
            font.draw(batch, q.getQuestion(), centeredX, y);
        }
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        // no shapes needed
    }

    public void dispose() {
        // font is owned by FontManager, do not dispose here
    }
}
