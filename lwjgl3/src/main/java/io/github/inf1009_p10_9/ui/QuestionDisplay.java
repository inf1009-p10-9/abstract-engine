package io.github.inf1009_p10_9.ui;

import io.github.inf1009_p10_9.questions.QuestionManager;
import io.github.inf1009_p10_9.questions.Question;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;

public class QuestionDisplay extends UIElement {
    private final QuestionManager questionManager;
    private BitmapFont font;

    // #TODO: Refactor QuestionManager to interface
    public QuestionDisplay(float x, float y, QuestionManager questionManager, BitmapFont sharedFont) {
        super(x, y);
        this.font = sharedFont; // font owned by FontManager, not disposed here
        this.zIndex = 100;
        this.questionManager = questionManager;
    }

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