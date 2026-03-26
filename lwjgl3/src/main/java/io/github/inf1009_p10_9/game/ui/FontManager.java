package io.github.inf1009_p10_9.game.ui;
import io.github.inf1009_p10_9.engine.core.UIElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import io.github.inf1009_p10_9.engine.interfaces.IManagerMinimal;

// singleton that generates and owns the shared fonts used across all scenes
public class FontManager implements IManagerMinimal {

    private static FontManager instance;

    // three sizes covering the main use cases across the game
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

    // generates all three fonts from the same ttf file, then disposes the generator
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

        generator.dispose();

        System.out.println("FontManager initialized");
    }

    // disposes all fonts on shutdown, any ui elements using them must not call dispose themselves
    @Override
    public void clear() {
        if (smallFont != null)  smallFont.dispose();
        if (mediumFont != null) mediumFont.dispose();
        if (largeFont != null)  largeFont.dispose();
    }

    // getters
    public BitmapFont getSmallFont()  { return smallFont; }
    public BitmapFont getMediumFont() { return mediumFont; }
    public BitmapFont getLargeFont()  { return largeFont; }
}
