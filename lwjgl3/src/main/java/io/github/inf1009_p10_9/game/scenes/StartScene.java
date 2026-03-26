package io.github.inf1009_p10_9.game.scenes;

import io.github.inf1009_p10_9.engine.core.Scene;

import io.github.inf1009_p10_9.engine.interfaces.*;
import io.github.inf1009_p10_9.game.ui.FontLoader;
import io.github.inf1009_p10_9.game.ui.MenuButtonElement;
import io.github.inf1009_p10_9.game.ui.SceneBackdrop;
import io.github.inf1009_p10_9.game.ui.TextLabel;
import io.github.inf1009_p10_9.game.ui.TitleCarElement;
import io.github.inf1009_p10_9.game.ui.TitleElement;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import io.github.inf1009_p10_9.game.interfaces.ISettingsKBRetrievable;

// the main menu screen, showing the title, animated background elements, and navigation buttons
public class StartScene extends MenuScene {

    // ui elements
    private TitleElement titleElement;
    private TitleCarElement titleCar;
    private SceneBackdrop backdrop;
    private final String[] menuOptions = { "Play", "Settings", "Quit Game" };

    // animation timers
    private float titleBounceTimer = 0;

    // button sizing
    private static final float BUTTON_WIDTH = 320f;
    private static final float BUTTON_HEIGHT = 50f;

    // button colors, one per menu option, plus highlight and text states
    private static final Color[] BUTTON_COLORS = {
        new Color(0.2f, 0.6f, 0.9f, 0.85f),
        new Color(0.6f, 0.3f, 0.9f, 0.85f),
        new Color(0.9f, 0.3f, 0.3f, 0.85f)
    };
    private static final Color BUTTON_HIGHLIGHT_COLOR = new Color(1f, 0.85f, 0.1f, 0.95f);

    // external dependencies injected via constructor
    private final FontLoader fontManager;

    public StartScene(IEntityRegisterable entityRegisterable,
                      IUIDisplayable uiDisplayable,
                      ICollidableRegisterable collidableRegisterable,
                      IRenderRegisterable renderRegisterable,
                      IMusicPlayable musicPlayable,
                      IInputKeyCheckable inputKeyCheckable,
                      ISceneSwitchable sceneSwitchable,
                      FontLoader fontManager,
                      ISettingsKBRetrievable settingsKBRetrievable) {
        super("StartScene",
              entityRegisterable,
              uiDisplayable,
              collidableRegisterable,
              renderRegisterable,
              musicPlayable,
              inputKeyCheckable,
              sceneSwitchable,
              settingsKBRetrievable);
        this.fontManager = fontManager;
    }

    // builds and registers all visual elements for the scene
    @Override
    protected void loadEntities() {
        float screenWidth = Gdx.graphics.getWidth();
        float centerX = screenWidth / 2;

        // shared decorative background
        backdrop = new SceneBackdrop(true);
        backdrop.addToScene(this);

        // title with drop shadow and outline at z-index 100
        titleElement = new TitleElement("DRIVE AND LEARN", fontManager.getLargeFont(), Color.GREEN);
        addUI(titleElement);

        // small car next to title, positioned just to the right of it
        GlyphLayout layout = new GlyphLayout(fontManager.getLargeFont(), "DRIVE AND LEARN");
        float titleRightEdge = (screenWidth + layout.width) / 2;
        titleCar = new TitleCarElement(
            titleRightEdge + 12, titleElement.getBaseY() - 35,
            0, 296, 64, 24,
            96, 36
        );
        addUI(titleCar);

        // menu buttons and labels, spaced evenly downward from startY
        menuOptionLabels = new TextLabel[menuOptions.length];
        arrowIndicators = new TextLabel[menuOptions.length];
        menuButtons = new MenuButtonElement[menuOptions.length];

        float startY = 450;
        float spacingY = 75;

        for (int i = 0; i < menuOptions.length; i++) {
            layout.setText(fontManager.getMediumFont(), menuOptions[i]);
            float optionX = centerX - layout.width / 2;
            float buttonX = centerX - BUTTON_WIDTH / 2 + 30;
            float buttonY = startY - (i * spacingY) - BUTTON_HEIGHT + 10;

            menuButtons[i] = new MenuButtonElement(
                buttonX, buttonY, BUTTON_WIDTH - 30, BUTTON_HEIGHT,
                BUTTON_COLORS[i], BUTTON_HIGHLIGHT_COLOR
            );
            addUI(menuButtons[i]);

            // arrow sits to the left of the button, only visible on the highlighted option
            float arrowX = getArrowBaseX();
            arrowIndicators[i] = new TextLabel(">>", arrowX, startY - (i * spacingY),
                                               fontManager.getMediumFont());
            arrowIndicators[i].setColor(ARROW_COLOR);
            arrowIndicators[i].setZIndex(200);
            addUI(arrowIndicators[i]);

            menuOptionLabels[i] = new TextLabel(menuOptions[i], optionX, startY - (i * spacingY),
                                                fontManager.getMediumFont());
            menuOptionLabels[i].setColor(NORMAL_COLOR);
            addUI(menuOptionLabels[i]);
        }
     // instruction hint in the grass strip at the bottom
        GlyphLayout instrLayout = new GlyphLayout(fontManager.getSmallFont(), "ENTER: select    UP/DOWN: move");
        TextLabel instructionLabel = new TextLabel(
            "ENTER: select    UP/DOWN: move",
            centerX - instrLayout.width / 2f,
            Gdx.graphics.getHeight() * 0.10f,
            fontManager.getSmallFont()
        );
        instructionLabel.setColor(new Color(1f, 1f, 0.6f, 1f));
        addUI(instructionLabel);

        updateHighlight();
        System.out.println("StartScene loaded");
    }

    // resets scene state and starts background music on each visit
    @Override
    public void load() {
        super.load();
        titleBounceTimer = 0;
        musicPlayable.playMusic("music/Super Mario Bros. medley.mp3");
    }

    @Override
    public void update() {
        super.update();

        float delta = Gdx.graphics.getDeltaTime();

        // animate shared decorative background
        backdrop.update(delta, 1f, 1f);

        // bounce title vertically and keep the title car glued to it
        titleBounceTimer += delta;
        float bounceOffset = (float) Math.sin(titleBounceTimer * 2.5f) * 10f;
        titleElement.setY(titleElement.getBaseY() + bounceOffset);
        titleCar.setY(titleElement.getBaseY() + bounceOffset - 35);

        // shared menu navigation
        updateMenuNavigation(menuOptions.length);

        // animate the arrow next to the currently highlighted option
        animateArrow(delta);
    }

    @Override
    protected float getArrowBaseX() {
        float centerX = Gdx.graphics.getWidth() / 2f;
        float buttonLeftEdge = centerX - BUTTON_WIDTH / 2f;
        return buttonLeftEdge - 50f;
    }

    // routes the confirmed selection to the appropriate scene or action
    @Override
    protected void handleMenuSelection() {
        String selectedOption = menuOptions[highlightedIndex];
        switch (selectedOption) {
            case "Play":
                System.out.println("Going to LevelSelectScene...");
                sceneSwitchable.switchScene("LevelSelectScene");

                break;
            case "Settings":
                sceneSwitchable.switchScene("SettingsScene");

                break;
            case "Quit Game":
                System.out.println("Quitting game...");
                Gdx.app.exit();
                break;
        }
    }
}
