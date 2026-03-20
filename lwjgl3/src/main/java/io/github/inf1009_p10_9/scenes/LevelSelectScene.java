package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.managers.SettingsManager;
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

// screen where the player picks a scenery theme before moving on to difficulty selection
public class LevelSelectScene extends Scene {

    // -------------------------------------------------------------------------
    // scenery options
    // -------------------------------------------------------------------------

    // names shown on each card and stored when confirmed
    private static final String[] SCENERY_OPTIONS = { "City", "Desert" };

    // which card is currently selected (0 = City, 1 = Desert)
    private int selectedSceneryIndex = 0;

    // -------------------------------------------------------------------------
    // card layout constants
    // -------------------------------------------------------------------------

    // each card is a tall rectangle drawn by a MenuButtonElement
    private static final float CARD_WIDTH  = 300f;
    private static final float CARD_HEIGHT = 260f;

    // horizontal centres for the two cards on a 1280-wide screen
    private static final float CARD_LEFT_CENTER_X  = 320f;
    private static final float CARD_RIGHT_CENTER_X = 960f;

    // top of the card area (LibGDX y grows upward, so this is nearer the middle)
    private static final float CARD_TOP_Y = 460f;

    // -------------------------------------------------------------------------
    // colors
    // -------------------------------------------------------------------------

    private static final Color CARD_CITY_COLOR    = new Color(0.20f, 0.50f, 0.90f, 0.85f);
    private static final Color CARD_DESERT_COLOR  = new Color(0.85f, 0.55f, 0.15f, 0.85f);
    private static final Color CARD_SELECTED_BORDER = new Color(1f, 0.90f, 0.10f, 1f);
    private static final Color CARD_UNSELECTED_TINT = new Color(0f,  0f,   0f,   0.35f);
    private static final Color LABEL_SELECTED_COLOR  = new Color(0.10f, 0.10f, 0.10f, 1f);
    private static final Color LABEL_UNSELECTED_COLOR = Color.WHITE;

    // -------------------------------------------------------------------------
    // UI element references
    // -------------------------------------------------------------------------

    // animated header elements
    private TitleElement  titleElement;
    private TitleCarElement titleCar;
    private CloudElement[] clouds;
    private CarElement    grassCar;

    // scenery cards: each card has a background button, a dimming overlay when
    // unselected, a large name label, and a subtitle hint line
    private MenuButtonElement[] cardBg;       // base colored rectangle
    private MenuButtonElement[] cardOverlay;  // dark tint overlay on unselected card
    private TextLabel[]         cardNameLabels;
    private TextLabel[]         cardHintLabels;

    // selection checkmark shown below the selected card
    private TextLabel selectionIndicator;


    // instruction line
    private TextLabel instructionLabel;

    // -------------------------------------------------------------------------
    // animation / input state
    // -------------------------------------------------------------------------

    private float titleBounceTimer = 0f;
    private float sceneLoadTime    = 0f;

    private boolean leftRightPressed = false;
    private boolean enterPressed     = false;
    private boolean escPressed       = false;

    // -------------------------------------------------------------------------
    // dependencies
    // -------------------------------------------------------------------------

    private final IInputKeyCheckable inputKeyCheckable;
    private final ISceneSwitchable   sceneSwitchable;
    private final FontManager        fontManager;

    // -------------------------------------------------------------------------
    // constructor
    // -------------------------------------------------------------------------

    public LevelSelectScene(IEntityRegisterable entityRegisterable,
                            IUIDisplayable uiDisplayable,
                            ICollidableRegisterable collidableRegisterable,
                            IRenderRegisterable renderRegisterable,
                            IMusicPlayable musicPlayable,
                            IInputKeyCheckable inputKeyCheckable,
                            ISceneSwitchable sceneSwitchable,
                            FontManager fontManager) {
        super("LevelSelectScene",
              entityRegisterable,
              uiDisplayable,
              collidableRegisterable,
              renderRegisterable,
              musicPlayable);
        this.inputKeyCheckable = inputKeyCheckable;
        this.sceneSwitchable   = sceneSwitchable;
        this.fontManager       = fontManager;
    }

    // -------------------------------------------------------------------------
    // loadEntities — build all UI once
    // -------------------------------------------------------------------------

    @Override
    protected void loadEntities() {
        float screenWidth  = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // --- background & decorations ----------------------------------------

        addUI(new BackgroundElement());

        clouds    = new CloudElement[3];
        clouds[0] = new CloudElement(100,  screenHeight * 0.78f, 160, 50, 30f);
        clouds[1] = new CloudElement(500,  screenHeight * 0.84f, 200, 60, 20f);
        clouds[2] = new CloudElement(950,  screenHeight * 0.72f, 140, 45, 35f);
        for (CloudElement cloud : clouds) addUI(cloud);

        float grassY = screenHeight * 0.15f;
        grassCar = new CarElement(
            100, grassY - 26, 110f,
            0, 133, 64, 26,
            128, 52
        );
        addUI(grassCar);

        // --- title -----------------------------------------------------------

        titleElement = new TitleElement("DRIVE AND LEARN", fontManager.getLargeFont(), Color.GREEN);
        addUI(titleElement);

        GlyphLayout layout = new GlyphLayout(fontManager.getLargeFont(), "DRIVE AND LEARN");
        float titleRightEdge = (screenWidth + layout.width) / 2;
        titleCar = new TitleCarElement(
            titleRightEdge + 12, titleElement.getBaseY() - 35,
            0, 296, 64, 24,
            96, 36
        );
        addUI(titleCar);


        // --- scenery cards ---------------------------------------------------

        cardBg          = new MenuButtonElement[2];
        cardOverlay     = new MenuButtonElement[2];
        cardNameLabels  = new TextLabel[2];
        cardHintLabels  = new TextLabel[2];

        float[] cardCenterXs = { CARD_LEFT_CENTER_X, CARD_RIGHT_CENTER_X };
        Color[] cardColors   = { CARD_CITY_COLOR, CARD_DESERT_COLOR };
        String[] hintTexts   = { "Urban streets & skyscrapers", "Sandy dunes & open roads" };

        for (int i = 0; i < 2; i++) {
            float cardX = cardCenterXs[i] - CARD_WIDTH / 2;
            float cardY = CARD_TOP_Y - CARD_HEIGHT;          // bottom of card

            // base colored rectangle (z = 50)
            cardBg[i] = new MenuButtonElement(
                cardX, cardY, CARD_WIDTH, CARD_HEIGHT,
                cardColors[i], CARD_SELECTED_BORDER);
            addUI(cardBg[i]);

            // dark overlay on top of the base color when this card is NOT selected (z = 60)
            cardOverlay[i] = new MenuButtonElement(
                cardX, cardY, CARD_WIDTH, CARD_HEIGHT,
                CARD_UNSELECTED_TINT, CARD_UNSELECTED_TINT);
            cardOverlay[i].setZIndex(60);
            addUI(cardOverlay[i]);

            // large name label centred in the card (z = 100)
            layout.setText(fontManager.getMediumFont(), SCENERY_OPTIONS[i]);
            float nameX = cardCenterXs[i] - layout.width / 2;
            float nameY = cardY + CARD_HEIGHT / 2 + layout.height / 2 + 20;
            cardNameLabels[i] = new TextLabel(
                SCENERY_OPTIONS[i], nameX, nameY, fontManager.getMediumFont());
            addUI(cardNameLabels[i]);

            // smaller hint text below the name
            layout.setText(fontManager.getSmallFont(), hintTexts[i]);
            float hintX = cardCenterXs[i] - layout.width / 2;
            float hintY = cardY + CARD_HEIGHT / 2 - 20;
            cardHintLabels[i] = new TextLabel(
                hintTexts[i], hintX, hintY, fontManager.getSmallFont());
            cardHintLabels[i].setColor(Color.LIGHT_GRAY);
            addUI(cardHintLabels[i]);
        }

        // checkmark / selection indicator centred under the selected card
        selectionIndicator = new TextLabel(
            "[ SELECTED ]",
            0, CARD_TOP_Y - CARD_HEIGHT - 28,
            fontManager.getSmallFont());
        selectionIndicator.setColor(CARD_SELECTED_BORDER);
        addUI(selectionIndicator);

        // --- instruction hint ------------------------------------------------

        instructionLabel = new TextLabel(
            "LEFT / RIGHT: choose scenery          ENTER: confirm          ESC: back",
            0, 105f,
            fontManager.getSmallFont());
        instructionLabel.setColor(Color.LIGHT_GRAY);
        layout.setText(fontManager.getSmallFont(),
            "LEFT / RIGHT: choose scenery          ENTER: confirm          ESC: back");
        instructionLabel.setPosition(screenWidth / 2 - layout.width / 2, 105f);
        addUI(instructionLabel);

        // apply initial highlight state
        updateCardVisuals();

        System.out.println("LevelSelectScene loaded");
    }

    // -------------------------------------------------------------------------
    // load — reset state on every visit
    // -------------------------------------------------------------------------

    @Override
    public void load() {
        super.load();
        sceneLoadTime     = 0f;
        titleBounceTimer  = 0f;
        selectedSceneryIndex = 0;
        leftRightPressed  = false;
        enterPressed      = false;
        escPressed        = false;
        musicPlayable.playMusic("music/Super Mario Bros. medley.mp3");
    }

    // -------------------------------------------------------------------------
    // update — animate + handle input
    // -------------------------------------------------------------------------

    @Override
    public void update() {
        super.update();

        float delta = Gdx.graphics.getDeltaTime();
        sceneLoadTime    += delta;
        titleBounceTimer += delta;

        // animate decorative elements
        for (CloudElement cloud : clouds) cloud.update(delta);
        grassCar.update(delta);

        // bounce the title and keep the car glued to it
        float bounceOffset = (float) Math.sin(titleBounceTimer * 2.5f) * 10f;
        titleElement.setY(titleElement.getBaseY() + bounceOffset);
        titleCar.setY(titleElement.getBaseY() + bounceOffset - 35);

        // ignore input briefly after loading to avoid accidental presses
        if (sceneLoadTime < 0.2f) return;

        // --- LEFT / RIGHT to switch scenery ----------------------------------

        boolean leftPressed  = inputKeyCheckable.isKeyPressed(Keys.LEFT)  ||
                               inputKeyCheckable.isKeyPressed(Keys.A);
        boolean rightPressed = inputKeyCheckable.isKeyPressed(Keys.RIGHT) ||
                               inputKeyCheckable.isKeyPressed(Keys.D);

        if (leftPressed || rightPressed) {
            if (!leftRightPressed) {
                leftRightPressed = true;
                if (rightPressed) {
                    selectedSceneryIndex = (selectedSceneryIndex + 1) % SCENERY_OPTIONS.length;
                } else {
                    selectedSceneryIndex =
                        (selectedSceneryIndex - 1 + SCENERY_OPTIONS.length) % SCENERY_OPTIONS.length;
                }
                updateCardVisuals();
            }
        } else {
            leftRightPressed = false;
        }

        // --- ENTER to confirm ------------------------------------------------

        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;
                confirmSelection();
            }
        } else {
            enterPressed = false;
        }

        // --- ESC to go back --------------------------------------------------

        if (inputKeyCheckable.isKeyPressed(Keys.ESCAPE)) {
            if (!escPressed) {
                escPressed = true;
                sceneSwitchable.switchScene("StartScene");
            }
        } else {
            escPressed = false;
        }
    }

    // -------------------------------------------------------------------------
    // updateCardVisuals — highlights the chosen card, dims the other
    // -------------------------------------------------------------------------

    private void updateCardVisuals() {
        float[] cardCenterXs = { CARD_LEFT_CENTER_X, CARD_RIGHT_CENTER_X };

        for (int i = 0; i < 2; i++) {
            boolean isSelected = (i == selectedSceneryIndex);

            // the base MenuButtonElement uses its highlight state to switch
            // between normal and highlight color (normal = themed, highlight = gold)
            cardBg[i].setHighlighted(isSelected);

            // dark overlay is visible only on the unselected card
            cardOverlay[i].setVisible(!isSelected);

            // name label text color
            cardNameLabels[i].setColor(isSelected ? LABEL_SELECTED_COLOR : LABEL_UNSELECTED_COLOR);
        }

        // move the selection indicator under the chosen card
        GlyphLayout layout = new GlyphLayout(fontManager.getSmallFont(), "[ SELECTED ]");
        float indicatorX = cardCenterXs[selectedSceneryIndex] - layout.width / 2;
        selectionIndicator.setPosition(indicatorX, CARD_TOP_Y - CARD_HEIGHT - 28);

    }

    // -------------------------------------------------------------------------
    // confirmSelection — store choice and advance to difficulty selection
    // -------------------------------------------------------------------------

    private void confirmSelection() {
        String chosen = SCENERY_OPTIONS[selectedSceneryIndex];
        System.out.println("Scenery chosen: " + chosen);

        // store the choice in SettingsManager so GameScene can read it
        SettingsManager.getInstance().setSelectedScenery(chosen);

        sceneSwitchable.switchScene("SubjectSelectScene");
    }
}