package io.github.inf1009_p10_9.game.scenes;

import io.github.inf1009_p10_9.engine.core.Scene;

import io.github.inf1009_p10_9.engine.interfaces.*;
import io.github.inf1009_p10_9.game.ui.FontLoader;
import io.github.inf1009_p10_9.game.ui.SceneBackdrop;
import io.github.inf1009_p10_9.game.ui.SettingsRow;
import io.github.inf1009_p10_9.game.ui.TextLabel;
import io.github.inf1009_p10_9.game.ui.TitleElement;
import io.github.inf1009_p10_9.game.interfaces.ISettingsKBRetrievable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

// the settings screen, showing a dashboard-style control panel for audio and keybind settings
public class SettingsScene extends Scene {

    // ui elements
    private TitleElement titleElement;
    private TextLabel instructionLabel;
    private SceneBackdrop backdrop;
    private SettingsRow[] rows;

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
    private final IKeyPressConsumable keyPressConsumable;
    private final ISceneSwitchable sceneSwitchable;
    private final ISettingsControllable settingsControllable;
    private final IMusicPlayable musicPlayable;
    private final ISFXPlayable sfxPlayable;
    private final FontLoader fontManager;
    private final ISettingsKBRetrievable settingsKBRetrievable;

    public SettingsScene(IEntityRegisterable entityRegisterable,
                         IUIDisplayable uiDisplayable,
                         ICollidableRegisterable collidableRegisterable,
                         IRenderRegisterable renderRegisterable,
                         IMusicPlayable musicPlayable,
                         ISFXPlayable sfxPlayable,
                         IInputKeyCheckable inputKeyCheckable,
                         IKeyPressConsumable keyPressConsumable,
                         ISceneSwitchable sceneSwitchable,
                         ISettingsControllable settingsControllable,
                         FontLoader fontManager,
                         ISettingsKBRetrievable settingsKBRetrievable) {
        super("SettingsScene",
              entityRegisterable,
              uiDisplayable,
              collidableRegisterable,
              renderRegisterable,
              musicPlayable);
        this.musicPlayable = musicPlayable;
        this.sfxPlayable = sfxPlayable;
        this.inputKeyCheckable = inputKeyCheckable;
        this.keyPressConsumable = keyPressConsumable;
        this.sceneSwitchable = sceneSwitchable;
        this.settingsControllable = settingsControllable;
        this.fontManager = fontManager;
        this.settingsKBRetrievable = settingsKBRetrievable;
    }

    // builds and registers all visual elements for the scene
    @Override
    protected void loadEntities() {
        float screenWidth = Gdx.graphics.getWidth();
        float centerX = screenWidth / 2f;

        // shared decorative background
        backdrop = new SceneBackdrop(false);
        backdrop.addToScene(this);

        // title for the settings dashboard
        titleElement = new TitleElement("SETTINGS", fontManager.getLargeFont(), Color.CYAN);
        addUI(titleElement);

        instructionLabel = new TextLabel(
            "UP / DOWN to move   |   LEFT / RIGHT to adjust   |   ENTER to rebind   |   ESC to return",
            centerX - 360, 500, fontManager.getSmallFont()
        );
        instructionLabel.setColor(Color.WHITE);
        addUI(instructionLabel);

        // create the dashboard rows as reusable row objects instead of parallel arrays
        rows = new SettingsRow[options.length];

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

            rows[i] = new SettingsRow(
                options[i],
                bindActions[i],
                rowX,
                rowY,
                ROW_WIDTH,
                ROW_HEIGHT,
                rowColor,
                ROW_HIGHLIGHT_COLOR,
                NORMAL_COLOR,
                VALUE_COLOR,
                ARROW_COLOR,
                fontManager
            );
            rows[i].addToScene(this);
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

        // animate shared decorative background
        backdrop.update(delta, 1f, 1f);

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
            animateArrow(delta);
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
                    System.out.println(selectedIndex);
                } else {
                    selectedIndex--;
                    System.out.println(selectedIndex);
                }

                // wrap around at the top and bottom
                if (selectedIndex < 0) {
                    selectedIndex = rows.length - 1;
                }
                if (selectedIndex >= rows.length) {
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
            rows[selectedIndex].getBindAction() != null) {

            settingsControllable.rebindKey(rows[selectedIndex].getBindAction(), pressedKey);
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
        sfxPlayable.setVolume(settingsControllable.getSfxVolume());
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
        float arrowBaseX = rowX - 45;

        for (int i = 0; i < rows.length; i++) {
            rows[i].setHighlighted(
                i == selectedIndex,
                NORMAL_COLOR,
                HIGHLIGHTED_COLOR,
                VALUE_COLOR,
                VALUE_HIGHLIGHTED_COLOR,
                arrowBaseX
            );
        }

        // update the row values
        rows[0].getValueLabel().setText(Math.round(settingsControllable.getMusicVolume() * 100) + "%");
        rows[1].getValueLabel().setText(Math.round(settingsControllable.getSfxVolume() * 100) + "%");

        rows[2].getValueLabel().setText(selectedIndex == 2 && waitingForRebind
            ? "Press key..."
            : Keys.toString(settingsKBRetrievable.getKeybind("MOVE_UP")));
        rows[3].getValueLabel().setText(selectedIndex == 3 && waitingForRebind
            ? "Press key..."
            : Keys.toString(settingsKBRetrievable.getKeybind("MOVE_DOWN")));
        rows[4].getValueLabel().setText(selectedIndex == 4 && waitingForRebind
            ? "Press key..."
            : Keys.toString(settingsKBRetrievable.getKeybind("MOVE_LEFT")));
        rows[5].getValueLabel().setText(selectedIndex == 5 && waitingForRebind
            ? "Press key..."
            : Keys.toString(settingsKBRetrievable.getKeybind("MOVE_RIGHT")));

        rows[6].getValueLabel().setText("Enter");

        // change the instruction line while waiting for a new keybind
        if (waitingForRebind) {
            instructionLabel.setText("Press a new key for " + rows[selectedIndex].getOptionText());
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

        rows[selectedIndex].getArrowLabel().setPosition(
            arrowBaseX + arrowOffset,
            rows[selectedIndex].getArrowLabel().getY()
        );
    }
}
