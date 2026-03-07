package io.github.inf1009_p10_9.ui;

import io.github.inf1009_p10_9.managers.QuestionManager;
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
    public QuestionDisplay(float x, float y, QuestionManager questionManager) {
        super(x, y);
        this.font = new BitmapFont();
        this.font.getData().setScale(2.5f);
        this.font.setColor(Color.WHITE);
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
            font.draw(batch, q.getQuestion(), centeredX, y);
        }
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        // No shapes needed for text
    }

    public void dispose() {
        font.dispose();
    }
}
