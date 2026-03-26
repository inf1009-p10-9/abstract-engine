package io.github.inf1009_p10_9.game.ui;
import io.github.inf1009_p10_9.engine.core.UIElement;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


// a positioned text element. prefer the shared font constructor wherever possible.
public class TextLabel extends UIElement {
    private String text;
    private BitmapFont font;
    private Color color;
    private boolean ownsFont; // tracks whether this label should dispose the font on cleanup

    // fallback constructor, creates its own BitmapFont at default size
    public TextLabel(String text) {
        super(0, 0);
        this.text = text;
        this.font = new BitmapFont();
        this.color = Color.WHITE;
        this.ownsFont = true;
    }

    // fallback constructor with position, creates its own BitmapFont scaled up 2x
    public TextLabel(String text, float x, float y) {
        super(x, y);
        this.text = text;
        this.font = new BitmapFont();
        this.font.getData().setScale(2f);
        this.color = Color.WHITE;
        this.ownsFont = true;
    }

    // preferred constructor, uses a shared font from FontManager
    public TextLabel(String text, float x, float y, BitmapFont sharedFont) {
        super(x, y);
        this.text = text;
        this.font = sharedFont;
        this.color = Color.WHITE;
        this.ownsFont = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!visible) return;
        font.setColor(color);
        font.draw(batch, text, x, y);
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) {
        // no shapes needed for text
    }

    // setters
    public void setText(String text) {
        this.text = text;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // replaces the font, disposing the old one only if this label owns it
    public void setFont(BitmapFont font) {
        if (this.ownsFont && this.font != null && this.font != font) {
            this.font.dispose();
        }
        this.font = font;
        this.ownsFont = false;
    }

    // only disposes the font if this label created it
    public void dispose() {
        if (ownsFont && font != null) {
            font.dispose();
        }
    }

	public void setX(float f) {
		// TODO Auto-generated method stub
		
	}

	public void setY(float f) {
		// TODO Auto-generated method stub
		
	}
}
