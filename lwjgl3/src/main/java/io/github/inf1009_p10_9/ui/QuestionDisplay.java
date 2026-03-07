package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.questions.Question;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class QuestionDisplay extends UIElement {
    private BitmapFont font;

    public QuestionDisplay(float x, float y) {
        super(x, y);
        this.font = new BitmapFont(); 
        this.font.getData().setScale(2.5f);
        this.font.setColor(Color.WHITE);
        this.zIndex = 100;
    }

    @Override
    public void render(SpriteBatch batch) {

        if (!visible) return;
        Question q = GameContext.getQuestionManager().getCurrentQuestion();
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