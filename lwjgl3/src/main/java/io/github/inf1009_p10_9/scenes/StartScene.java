package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.BackgroundElement;
import io.github.inf1009_p10_9.ui.CarElement;
import io.github.inf1009_p10_9.ui.CloudElement;
import io.github.inf1009_p10_9.ui.FontManager;
import io.github.inf1009_p10_9.ui.MenuButtonElement;
import io.github.inf1009_p10_9.ui.TextLabel;
import io.github.inf1009_p10_9.ui.TitleCarElement;
import io.github.inf1009_p10_9.ui.TitleElement;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

// the main menu screen, showing the title, animated background elements, and navigation buttons
public class StartScene extends Scene {

    // ui elements
    private TitleElement titleElement;
    private TitleCarElement titleCar;
    private TextLabel[] menuOptionLabels;
    private TextLabel[] arrowIndicators;
    private MenuButtonElement[] menuButtons;
    private CloudElement[] clouds;
    private CarElement grassCar;
    private String[] menuOptions = { "Level Selection", "Settings", "Quit Game" };

    // menu navigation state
    private int highlightedIndex = 0;
    private boolean upDownPressed = false;
    private boolean enterPressed = false;
    private float sceneLoadTime = 0;

    // animation timers
    private float titleBounceTimer = 0;
    private float arrowBounceTimer = 0;

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
    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color HIGHLIGHTED_COLOR = new Color(0.1f, 0.1f, 0.1f, 1f);
    private static final Color ARROW_COLOR = Color.WHITE;

    // external dependencies injected via constructor
    private IInputKeyCheckable inputKeyCheckable;
    private ISceneSwitchable sceneSwitchable;
    private final FontManager fontManager;

    public StartScene(IEntityRegisterable entityRegisterable,
                      IUIDisplayable uiDisplayable,
                      ICollidableRegisterable collidableRegisterable,
                      IRenderRegisterable renderRegisterable,
                      IMusicPlayable musicPlayable,
                      IInputKeyCheckable inputKeyCheckable,
                      ISceneSwitchable sceneSwitchable,
                      FontManager fontManager) {
        super("StartScene",
              entityRegisterable,
              uiDisplayable,
              collidableRegisterable,
              renderRegisterable,
              musicPlayable);
        this.inputKeyCheckable = inputKeyCheckable;
        this.sceneSwitchable = sceneSwitchable;
        this.fontManager = fontManager;
    }

    // builds and registers all visual elements for the scene
    @Override
    protected void loadEntities() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float centerX = screenWidth / 2;

        // background at z-index -100
        BackgroundElement background = new BackgroundElement();
        addUI(background);

        // drifting clouds at z-index -50
        clouds = new CloudElement[3];
        clouds[0] = new CloudElement(100, screenHeight * 0.78f, 160, 50, 30f);
        clouds[1] = new CloudElement(500, screenHeight * 0.84f, 200, 60, 20f);
        clouds[2] = new CloudElement(950, screenHeight * 0.72f, 140, 45, 35f);
        for (CloudElement cloud : clouds) {
            addUI(cloud);
        }

        // car driving across the grass strip
        float grassY = screenHeight * 0.15f;
        grassCar = new CarElement(
            100, grassY - 26, 110f,
            0, 133, 64, 26,    // exact coords from analysis
            128, 52            // scaled up 2x on screen
        );
        addUI(grassCar);

        // title with drop shadow and outline at z-index 100
        titleElement = new TitleElement("DRIVE AND LEARN", fontManager.getLargeFont(), Color.GREEN);
        addUI(titleElement);

        // small car next to title, positioned just to the right of it
        GlyphLayout layout = new GlyphLayout(fontManager.getLargeFont(), "DRIVE AND LEARN");
        float titleRightEdge = (screenWidth + layout.width) / 2;
        titleCar = new TitleCarElement(
        	    titleRightEdge + 12, titleElement.getBaseY() - 35,
        	    0, 296, 64, 24,    // exact sheet coords
        	    96, 36             // small draw size for title decoration
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
                buttonX, buttonY, BUTTON_WIDTH-30, BUTTON_HEIGHT,
                BUTTON_COLORS[i], BUTTON_HIGHLIGHT_COLOR);
            addUI(menuButtons[i]);

            // arrow sits to the left of the button, only visible on the highlighted option
            float buttonLeftEdge = centerX - BUTTON_WIDTH / 2;
            float arrowX = buttonLeftEdge - 50;

            arrowIndicators[i] = new TextLabel(">>", arrowX, startY - (i * spacingY),
                                               fontManager.getMediumFont());
            arrowIndicators[i].setColor(new Color(1f, 0.9f, 0.1f, 1f));
            arrowIndicators[i].setZIndex(200);
            addUI(arrowIndicators[i]);

            menuOptionLabels[i] = new TextLabel(menuOptions[i], optionX, startY - (i * spacingY),
                                                fontManager.getMediumFont());
            menuOptionLabels[i].setColor(NORMAL_COLOR);
            addUI(menuOptionLabels[i]);
        }

        updateHighlight();
        System.out.println("StartScene loaded");
    }

    // resets scene state and starts background music on each visit
    @Override
    public void load() {
        super.load();
        sceneLoadTime = 0;
        highlightedIndex = 0;
        titleBounceTimer = 0;
        musicPlayable.playMusic("music/Super Mario Bros. medley.mp3");
    }

    @Override
    public void update() {
        super.update();

        float delta = Gdx.graphics.getDeltaTime();
        sceneLoadTime += delta;

        // animate background elements
        for (CloudElement cloud : clouds) {
            cloud.update(delta);
        }
        grassCar.update(delta);

        // bounce title vertically and keep the title car glued to it
        titleBounceTimer += delta;
        float bounceOffset = (float) Math.sin(titleBounceTimer * 2.5f) * 10f;
        titleElement.setY(titleElement.getBaseY() + bounceOffset);
        titleCar.setY(titleElement.getBaseY() + bounceOffset - 35);

        // ignore input briefly after loading to avoid accidental presses
        if (sceneLoadTime < 0.2f) {
            return;
        }

        // move the selection up or down, only triggers once per press
        boolean upKeyPressed = inputKeyCheckable.isKeyPressed(Keys.UP) ||
                               inputKeyCheckable.isKeyPressed(Keys.W);
        boolean downKeyPressed = inputKeyCheckable.isKeyPressed(Keys.DOWN) ||
                                 inputKeyCheckable.isKeyPressed(Keys.S);

        if (upKeyPressed || downKeyPressed) {
            if (!upDownPressed) {
                upDownPressed = true;

                if (downKeyPressed) {
                    highlightedIndex++;
                } else {
                    highlightedIndex--;
                }

                // wrap around at the top and bottom
                if (highlightedIndex < 0) {
                    highlightedIndex = menuOptions.length - 1;
                }
                if (highlightedIndex >= menuOptions.length) {
                    highlightedIndex = 0;
                }

                updateHighlight();
            }
        } else {
            upDownPressed = false;
        }

        // confirm selection on enter
        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;
                handleMenuSelection();
            }
        } else {
            enterPressed = false;
        }

        // animate the arrow next to the currently highlighted option
        float buttonLeftEdge = Gdx.graphics.getWidth() / 2 - BUTTON_WIDTH / 2;
        float arrowBaseX = buttonLeftEdge - 50;
        arrowBounceTimer += delta;
        float arrowBounce = (float) Math.sin(arrowBounceTimer * 6f) * 6f; // fast horizontal bounce

        for (int i = 0; i < arrowIndicators.length; i++) {
            if (i == highlightedIndex) {
                GlyphLayout layout = new GlyphLayout(fontManager.getMediumFont(), menuOptions[i]);
                float optionX = Gdx.graphics.getWidth() / 2 - layout.width / 2;
                arrowIndicators[i].setPosition(arrowBaseX + arrowBounce, arrowIndicators[i].getY());
            }
        }
    }

    // refreshes colors, arrow visibility, and button highlight state to match the current selection
    private void updateHighlight() {
        GlyphLayout layout = new GlyphLayout();
        float centerX = Gdx.graphics.getWidth() / 2;
        float buttonLeftEdge = Gdx.graphics.getWidth() / 2 - BUTTON_WIDTH / 2;
        float arrowX = buttonLeftEdge - 50;

        for (int i = 0; i < menuOptions.length; i++) {
            layout.setText(fontManager.getMediumFont(), menuOptions[i]);
            float optionX = centerX - layout.width / 2;

            if (i == highlightedIndex) {
                menuOptionLabels[i].setColor(HIGHLIGHTED_COLOR);
                arrowIndicators[i].setVisible(true);
                arrowIndicators[i].setColor(new Color(1f, 0.9f, 0.1f, 1f));
                menuButtons[i].setHighlighted(true);
                arrowBounceTimer = 0; // reset bounce on each new selection
            } else {
                menuOptionLabels[i].setColor(NORMAL_COLOR);
                arrowIndicators[i].setVisible(false);
                arrowIndicators[i].setPosition(arrowX, arrowIndicators[i].getY());
                menuButtons[i].setHighlighted(false);
            }
        }
    }

    // routes the confirmed selection to the appropriate scene or action
    private void handleMenuSelection() {
        String selectedOption = menuOptions[highlightedIndex];

        if (selectedOption.equals("Level Selection")) {
            System.out.println("Going to SubjectSelectScene...");
            sceneSwitchable.switchScene("SubjectSelectScene");

        } else if (selectedOption.equals("Settings")) {
            sceneSwitchable.switchScene("SettingsScene");

        } else if (selectedOption.equals("Quit Game")) {
            System.out.println("Quitting game...");
            Gdx.app.exit();
        }
    }
}
