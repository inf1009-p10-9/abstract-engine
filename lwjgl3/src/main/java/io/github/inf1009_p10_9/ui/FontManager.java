package io.github.inf1009_p10_9.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import io.github.inf1009_p10_9.interfaces.IManager;

public class FontManager implements IManager {

    private static FontManager instance;

    private BitmapFont smallFont;   // instructions and small text
    private BitmapFont mediumFont;  // menu options and gate answers
    private BitmapFont largeFont;   // titles and questions

    protected FontManager() {}

    public static FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }

    @Override
    public void initialize() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/Fredoka.ttf")
        );

        FreeTypeFontParameter smallParams = new FreeTypeFontParameter();
        smallParams.size = 18;
        smallFont = generator.generateFont(smallParams);

        FreeTypeFontParameter mediumParams = new FreeTypeFontParameter();
        mediumParams.size = 32;
        mediumFont = generator.generateFont(mediumParams);

        FreeTypeFontParameter largeParams = new FreeTypeFontParameter();
        largeParams.size = 52;
        largeFont = generator.generateFont(largeParams);

        // generator is no longer needed once fonts are generated
        generator.dispose();

        System.out.println("FontManager initialized");
    }

    @Override
    public void update() {}

    @Override
    public void clear() {
        if (smallFont != null) {
            smallFont.dispose();
        }
        if (mediumFont != null) {
            mediumFont.dispose();
        }
        if (largeFont != null) {
            largeFont.dispose();
        }
    }

    public BitmapFont getSmallFont() {
        return smallFont;
    }

    public BitmapFont getMediumFont() {
        return mediumFont;
    }

    public BitmapFont getLargeFont() {
        return largeFont;
    }
}