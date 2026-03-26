package io.github.inf1009_p10_9.game.ui;
import io.github.inf1009_p10_9.engine.core.UIElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009_p10_9.game.interfaces.IUILives;

public class LivesElement extends UIElement implements IUILives {
    private BitmapFont font;
    private Texture sheet;
    private float scale;
    private int livesCounter;

    public LivesElement(float x, float y, BitmapFont sharedFont){
        super(x,y);
        this.zIndex = 2;
        this.font = sharedFont;

        this.sheet = new Texture(Gdx.files.internal("gameScene/heart.png"));
        this.scale = 0.025f;
        this.livesCounter = 1;
    }

    public int getLivesCounter(){return livesCounter;}

    public void setLivesCounter(int lives){
        this.livesCounter = lives;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!visible) return;

        batch.draw(sheet, x-40, y-25, sheet.getWidth() * scale, sheet.getHeight() * scale);
        font.setColor(Color.WHITE);
        font.draw(batch, String.format("x %d", livesCounter), x, y);
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer){
        //
    }
}
