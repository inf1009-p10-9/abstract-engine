package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.BackgroundElement;
import io.github.inf1009_p10_9.ui.CloudElement;
import io.github.inf1009_p10_9.ui.FontManager;
import io.github.inf1009_p10_9.ui.MenuButtonElement;
import io.github.inf1009_p10_9.ui.TextLabel;
import io.github.inf1009_p10_9.ui.TitleElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

// the settings screen, showing a dashboard-style control panel for audio and keybind settings
public class SettingsScene extends Scene {

    // ui elements
    private TitleElement titleElement;
    private TextLabel instructionLabel;
    private TextLabel[] optionLabels;
    private TextLabel[] valueLabels;
    private TextLabel[] arrowIndicators;
    private MenuButtonElement[] menuButtons;
    private CloudElement[] clouds;

    // settings menu content
    private final String[] options = {
        "Music Volume",
        "SFX Volume",
        "Move Up",
        "Move Down",
        "Move Left",
        "Move Right",
        "Back"
    };

    // used to identify which rows are keybind rows
    private final String[] bindActions = {
        null,
        null,
        "MOVE_UP",
        "MOVE_DOWN",
        "MOVE_LEFT",
        "MOVE_RIGHT",
        null
    };

    // menu navigation state
    private int selectedIndex = 0;
    private boolean upDownPressed = false;
    private boolean leftRightPressed = false;
    private boolean enterPressed = false;
    private boolean escPressed = false;
    private float sceneLoadTime = 0;

    // rebinding state
    private boolean waitingForRebind = false;
    private boolean rebindReady = false;

    // animation timers
    private float titleBounceTimer = 0;
    private float arrowPulseTimer = 0;

    // panel sizing
    private static final float ROW_WIDTH = 620f;
    private static final float ROW_HEIGHT = 48f;

    // row colors for a more distinct "settings dashboard" look
    private static final Color AUDIO_ROW_COLOR = new Color(0.18f, 0.45f, 0.75f, 0.88f);
    private static final Color KEY_ROW_COLOR = new Color(0.45f, 0.26f, 0.72f, 0.88f);
    private static final Color BACK_ROW_COLOR = new Color(0.82f, 0.28f, 0.28f, 0.88f);
    private static final Color ROW_HIGHLIGHT_COLOR = new Color(1f, 0.85f, 0.12f, 0.96f);

    // text colors
    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color HIGHLIGHTED_COLOR = new Color(0.08f, 0.08f, 0.08f, 1f);
    private static final Color VALUE_COLOR = new Color(0.95f, 0.95f, 1f, 1f);
    private static final Color VALUE_HIGHLIGHTED_COLOR = new Color(0.08f, 0.08f, 0.08f, 1f);
    private static final Color ARROW_COLOR = new Color(1f, 0.9f, 0.1f, 1f);

    // external dependencies injected via constructor
    private final IInputKeyCheckable inputKeyCheckable;
    private final ISceneSwitchable sceneSwitchable;
    private final ISettingsControllable settingsControllable;
    private final IKeyPressConsumable keyPressConsumable;
    private final IMusicPlayable musicPlayable;
    private final ISFXVolume sfxVolume;
    private final FontManager fontManager;

    public SettingsScene(IEntityRegisterable entityRegisterable,
                         IUIDisplayable uiDisplayable,
                         ICollidableRegisterable collidableRegisterable,
                         IRenderRegisterable renderRegisterable,
                         IMusicPlayable musicPlayable,
                         ISFXVolume sfxVolume,
                         IInputKeyCheckable inputKeyCheckable,
                         IKeyPressConsumable keyPressConsumable,
                         ISceneSwitchable sceneSwitchable,
                         ISettingsControllable settingsControllable,
                         FontManager fontManager) {

        super("SettingsScene",
              entityRegisterable,
              uiDisplayable,
              collidableRegisterable,
              renderRegisterable,
              musicPlayable);
        this.musicPlayable = musicPlayable;
        this.sfxVolume = sfxVolume;
        this.inputKeyCheckable = inputKeyCheckable;
        this.sceneSwitchable = sceneSwitchable;
        this.keyPressConsumable = keyPressConsumable;
        this.settingsControllable = settingsControllable;
        this.fontManager = fontManager;
    }

    // builds and registers all visual elements for the scene
    @Override
    protected void loadEntities() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float centerX = screenWidth / 2f;

        // background at z-index -100
        BackgroundElement background = new BackgroundElement();
        addUI(background);

        // drifting clouds for a bit of motion in the otherwise cleaner settings page
        clouds = new CloudElement[3];
        clouds[0] = new CloudElement(80, screenHeight * 0.90f, 160, 50, 25f);
        clouds[1] = new CloudElement(480, screenHeight * 0.94f, 200, 60, 18f);
        clouds[2] = new CloudElement(980, screenHeight * 0.89f, 150, 46, 28f);
        for (CloudElement cloud : clouds) {
            addUI(cloud);
        }

        // title for the settings dashboard
        titleElement = new TitleElement("SETTINGS", fontManager.getLargeFont(), Color.CYAN);
        addUI(titleElement);

        instructionLabel = new TextLabel(
            "UP / DOWN to move   |   LEFT / RIGHT to adjust   |   ENTER to rebind   |   ESC to return",
            centerX - 360, 500, fontManager.getSmallFont()
        );
        instructionLabel.setColor(Color.WHITE);
        addUI(instructionLabel);

        // create the dashboard rows
        optionLabels = new TextLabel[options.length];
        valueLabels = new TextLabel[options.length];
        arrowIndicators = new TextLabel[options.length];
        menuButtons = new MenuButtonElement[options.length];

        float startY = 430;
        float spacingY = 58;
        float rowX = centerX - ROW_WIDTH / 2f;

        for (int i = 0; i < options.length; i++) {
            float rowY = startY - (i * spacingY);

            // color-code each type of setting row
            Color rowColor;
            if (i <= 1) {
                rowColor = AUDIO_ROW_COLOR;
            } else if (i <= 5) {
                rowColor = KEY_ROW_COLOR;
            } else {
                rowColor = BACK_ROW_COLOR;
            }

            // row panel
            menuButtons[i] = new MenuButtonElement(
                rowX, rowY - ROW_HEIGHT + 10,
                ROW_WIDTH, ROW_HEIGHT,
                rowColor, ROW_HIGHLIGHT_COLOR
            );
            addUI(menuButtons[i]);

            // selection arrow to the left of the active row
            float arrowX = rowX - 45;
            arrowIndicators[i] = new TextLabel(">>", arrowX, rowY, fontManager.getMediumFont());
            arrowIndicators[i].setColor(ARROW_COLOR);
            arrowIndicators[i].setZIndex(200);
            addUI(arrowIndicators[i]);

            // setting name on the left side of each row
            optionLabels[i] = new TextLabel(options[i], rowX + 22, rowY, fontManager.getMediumFont());
            optionLabels[i].setColor(NORMAL_COLOR);
            addUI(optionLabels[i]);

            // current value on the right side of each row
            valueLabels[i] = new TextLabel("", rowX + ROW_WIDTH - 150, rowY, fontManager.getMediumFont());
            valueLabels[i].setColor(VALUE_COLOR);
            addUI(valueLabels[i]);
        }

        updateRows();
        System.out.println("SettingsScene loaded");
    }

    // resets scene state each time it is opened
    @Override
    public void load() {
        super.load();
        sceneLoadTime = 0;
        selectedIndex = 0;
        upDownPressed = false;
        leftRightPressed = false;
        enterPressed = false;
        escPressed = false;
        waitingForRebind = false;
        rebindReady = false;
        titleBounceTimer = 0;
        arrowPulseTimer = 0;
        updateRows();
    }

    @Override
    public void update() {
        super.update();

        float delta = Gdx.graphics.getDeltaTime();
        sceneLoadTime += delta;

        // animate background clouds
        for (CloudElement cloud : clouds) {
            cloud.update(delta);
        }

        // slight bounce for the title so the page still feels alive
        titleBounceTimer += delta;
        float bounceOffset = (float) Math.sin(titleBounceTimer * 2.2f) * 6f;
        titleElement.setY(titleElement.getBaseY() + bounceOffset);

        // ignore input briefly after loading to avoid accidental presses
        if (sceneLoadTime < 0.2f) {
            return;
        }

        // handle key-rebinding mode separately from normal navigation
        if (waitingForRebind) {
            handleRebindMode();
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
                    selectedIndex++;
                } else {
                    selectedIndex--;
                }

                // wrap around at the top and bottom
                if (selectedIndex < 0) {
                    selectedIndex = options.length - 1;
                }
                if (selectedIndex >= options.length) {
                    selectedIndex = 0;
                }

                updateRows();
            }
        } else {
            upDownPressed = false;
        }

        // adjust volume using left and right on the volume rows
        boolean leftKeyPressed = inputKeyCheckable.isKeyPressed(Keys.LEFT) ||
                                 inputKeyCheckable.isKeyPressed(Keys.A);
        boolean rightKeyPressed = inputKeyCheckable.isKeyPressed(Keys.RIGHT) ||
                                  inputKeyCheckable.isKeyPressed(Keys.D);

        if (leftKeyPressed || rightKeyPressed) {
            if (!leftRightPressed) {
                leftRightPressed = true;

                if (selectedIndex == 0) {
                    adjustMusicVolume(rightKeyPressed ? 0.1f : -0.1f);
                } else if (selectedIndex == 1) {
                    adjustSfxVolume(rightKeyPressed ? 0.1f : -0.1f);
                }

                updateRows();
            }
        } else {
            leftRightPressed = false;
        }

        // confirm selection on enter
        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;
                handleEnterSelection();
            }
        } else {
            enterPressed = false;
        }

        // allow quick return to the main menu with escape
        if (inputKeyCheckable.isKeyPressed(Keys.ESCAPE)) {
            if (!escPressed) {
                escPressed = true;
                sceneSwitchable.switchScene("StartScene");
            }
        } else {
            escPressed = false;
        }

        // animate the arrow next to the highlighted row
        animateArrow(delta);
    }

    // handles the special state where the player is choosing a new keybind
    private void handleRebindMode() {
        if (!rebindReady) {
            boolean blockedKeyStillHeld =
                inputKeyCheckable.isKeyPressed(Keys.ENTER) ||
                inputKeyCheckable.isKeyPressed(Keys.ESCAPE) ||
                inputKeyCheckable.isKeyPressed(Keys.UP) ||
                inputKeyCheckable.isKeyPressed(Keys.DOWN) ||
                inputKeyCheckable.isKeyPressed(Keys.LEFT) ||
                inputKeyCheckable.isKeyPressed(Keys.RIGHT) ||
                inputKeyCheckable.isKeyPressed(Keys.W) ||
                inputKeyCheckable.isKeyPressed(Keys.A) ||
                inputKeyCheckable.isKeyPressed(Keys.S) ||
                inputKeyCheckable.isKeyPressed(Keys.D);

            // wait until the original control keys are released before listening for the new key
            if (!blockedKeyStillHeld) {
                rebindReady = true;
                keyPressConsumable.consumeLastJustPressedKey();
            }

            updateRows();
            return;
        }

        int pressedKey = keyPressConsumable.consumeLastJustPressedKey();

        if (pressedKey != -1 &&
            pressedKey != Keys.ENTER &&
            pressedKey != Keys.ESCAPE &&
            bindActions[selectedIndex] != null) {

            settingsControllable.rebindKey(bindActions[selectedIndex], pressedKey);
            waitingForRebind = false;
            rebindReady = false;
            updateRows();
        }
    }

    // changes music volume and immediately applies it to the bgm manager
    private void adjustMusicVolume(float delta) {
        settingsControllable.setMusicVolume(settingsControllable.getMusicVolume() + delta);
        musicPlayable.setVolume(settingsControllable.getMusicVolume());
    }

    // changes sfx volume and immediately applies it to the sfx manager
    private void adjustSfxVolume(float delta) {
        settingsControllable.setSfxVolume(settingsControllable.getSfxVolume() + delta);
        sfxVolume.setVolume(settingsControllable.getSfxVolume());
    }

    // handles enter based on the currently selected row
    private void handleEnterSelection() {
        if (selectedIndex >= 2 && selectedIndex <= 5) {
            waitingForRebind = true;
            rebindReady = false;
            keyPressConsumable.consumeLastJustPressedKey();
            updateRows();
        } else if (selectedIndex == 6) {
            sceneSwitchable.switchScene("StartScene");
        }
    }

    // refreshes all row states, displayed values, and instruction text
    private void updateRows() {
        float rowX = Gdx.graphics.getWidth() / 2f - ROW_WIDTH / 2f;
        float arrowX = rowX - 45;

        for (int i = 0; i < options.length; i++) {
            if (i == selectedIndex) {
                menuButtons[i].setHighlighted(true);
                optionLabels[i].setColor(HIGHLIGHTED_COLOR);
                valueLabels[i].setColor(VALUE_HIGHLIGHTED_COLOR);
                arrowIndicators[i].setVisible(true);
                arrowPulseTimer = 0;
            } else {
                menuButtons[i].setHighlighted(false);
                optionLabels[i].setColor(NORMAL_COLOR);
                valueLabels[i].setColor(VALUE_COLOR);
                arrowIndicators[i].setVisible(false);
                arrowIndicators[i].setPosition(arrowX, arrowIndicators[i].getY());
            }
        }

        // update the row values
        valueLabels[0].setText((int) (settingsControllable.getMusicVolume() * 100) + "%");
        valueLabels[1].setText((int) (settingsControllable.getSfxVolume() * 100) + "%");

        valueLabels[2].setText(selectedIndex == 2 && waitingForRebind
            ? "Press key..."
            : Keys.toString(settingsControllable.getKeybind("MOVE_UP")));
        valueLabels[3].setText(selectedIndex == 3 && waitingForRebind
            ? "Press key..."
            : Keys.toString(settingsControllable.getKeybind("MOVE_DOWN")));
        valueLabels[4].setText(selectedIndex == 4 && waitingForRebind
            ? "Press key..."
            : Keys.toString(settingsControllable.getKeybind("MOVE_LEFT")));
        valueLabels[5].setText(selectedIndex == 5 && waitingForRebind
            ? "Press key..."
            : Keys.toString(settingsControllable.getKeybind("MOVE_RIGHT")));

        valueLabels[6].setText("Enter");

        // change the instruction line while waiting for a new keybind
        if (waitingForRebind) {
            instructionLabel.setText("Press a new key for " + options[selectedIndex]);
            instructionLabel.setColor(new Color(1f, 0.9f, 0.1f, 1f));
        } else {
            instructionLabel.setText("UP / DOWN to move   |   LEFT / RIGHT to adjust   |   ENTER to rebind   |   ESC to return");
            instructionLabel.setColor(Color.WHITE);
        }
    }

    // adds a small pulsing motion to the visible selection arrow
    private void animateArrow(float delta) {
        float rowX = Gdx.graphics.getWidth() / 2f - ROW_WIDTH / 2f;
        float arrowBaseX = rowX - 45;

        arrowPulseTimer += delta;
        float arrowOffset = (float) Math.sin(arrowPulseTimer * 6f) * 5f;

        for (int i = 0; i < arrowIndicators.length; i++) {
            if (i == selectedIndex) {
                arrowIndicators[i].setPosition(arrowBaseX + arrowOffset, arrowIndicators[i].getY());
            }
        }
    }
}
