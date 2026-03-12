package io.github.inf1009_p10_9.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

import io.github.inf1009_p10_9.managers.SettingsManager;
import io.github.inf1009_p10_9.managers.InputManager;
import io.github.inf1009_p10_9.managers.OutputManager;
import io.github.inf1009_p10_9.ui.TextLabel;
import io.github.inf1009_p10_9.interfaces.*;

// settings screen for adjusting volume and rebinding movement keys
public class SettingsScene extends Scene {
    private final IInputKeyCheckable inputKeyCheckable;
    private final ISceneSwitchable sceneSwitchable;

    // ui elements
    private TextLabel titleLabel;
    private TextLabel[] optionLabels;
    private TextLabel[] valueLabels;

    // the list of settings rows shown on screen
    private final String[] options = {
        "Music Volume",
        "SFX Volume",
        "Move Up",
        "Move Down",
        "Move Left",
        "Move Right",
        "Back"
    };

    // maps each row to its keybind action name, null means the row is not rebindable
    private final String[] bindActions = {
        null,
        null,
        "MOVE_UP",
        "MOVE_DOWN",
        "MOVE_LEFT",
        "MOVE_RIGHT",
        null
    };

    // navigation and input state
    private int selectedIndex = 0;
    private boolean upDownPressed = false;
    private boolean leftRightPressed = false;
    private boolean enterPressed = false;
    private boolean escPressed = false;

    // rebinding state, tracks whether we are waiting for a new key to be pressed
    private boolean waitingForRebind = false;
    private boolean rebindReady = false;

    public SettingsScene(
            IEntityRegisterable entityRegisterable,
            IUIDisplayable uiDisplayable,
            ICollidableRegisterable collidableRegisterable,
            IRenderRegisterable renderRegisterable,
            IMusicPlayable musicPlayable,
            IInputKeyCheckable inputKeyCheckable,
            ISceneSwitchable sceneSwitchable) {
        super("SettingsScene", entityRegisterable, uiDisplayable, collidableRegisterable, renderRegisterable, musicPlayable);
        this.inputKeyCheckable = inputKeyCheckable;
        this.sceneSwitchable = sceneSwitchable;
    }

    @Override
    protected void loadEntities() {
        float centerX = Gdx.graphics.getWidth() / 2f;

        titleLabel = new TextLabel("SETTINGS", centerX - 80, 560);
        titleLabel.setColor(Color.GREEN);
        addUI(titleLabel);

        // build a label pair for each option row
        optionLabels = new TextLabel[options.length];
        valueLabels = new TextLabel[options.length];

        float startY = 430;
        float gap = 55;

        for (int i = 0; i < options.length; i++) {
            optionLabels[i] = new TextLabel(options[i], centerX - 250, startY - i * gap);
            valueLabels[i] = new TextLabel("", centerX + 80, startY - i * gap);
            addUI(optionLabels[i]);
            addUI(valueLabels[i]);
        }

        refreshLabels();
    }

    @Override
    public void update() {
        super.update();

        // if waiting for a rebind, block until all navigation keys are released first,
        // then capture the next fresh key press as the new binding
        if (waitingForRebind) {
            if (!rebindReady) {
                boolean anyBlockedKeyHeld =
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

                if (!anyBlockedKeyHeld) {
                    rebindReady = true;
                    InputManager.getInstance().consumeLastJustPressedKey();
                }

                refreshLabels();
                return;
            }

            // capture the next valid key press and save it as the new binding
            int pressed = InputManager.getInstance().consumeLastJustPressedKey();

            if (pressed != -1
                    && pressed != Keys.ENTER
                    && pressed != Keys.ESCAPE
                    && bindActions[selectedIndex] != null) {
                SettingsManager.getInstance().rebindKey(bindActions[selectedIndex], pressed);
                waitingForRebind = false;
                rebindReady = false;
                refreshLabels();
            }

            return;
        }

        // up/down to move between rows, only triggers once per press
        boolean up = inputKeyCheckable.isKeyPressed(Keys.UP) || inputKeyCheckable.isKeyPressed(Keys.W);
        boolean down = inputKeyCheckable.isKeyPressed(Keys.DOWN) || inputKeyCheckable.isKeyPressed(Keys.S);

        if (up || down) {
            if (!upDownPressed) {
                upDownPressed = true;
                selectedIndex += down ? 1 : -1;
                if (selectedIndex < 0) selectedIndex = options.length - 1;
                if (selectedIndex >= options.length) selectedIndex = 0;
                refreshLabels();
            }
        } else {
            upDownPressed = false;
        }

        // left/right to adjust volume on the first two rows, only triggers once per press
        boolean left = inputKeyCheckable.isKeyPressed(Keys.LEFT) || inputKeyCheckable.isKeyPressed(Keys.A);
        boolean right = inputKeyCheckable.isKeyPressed(Keys.RIGHT) || inputKeyCheckable.isKeyPressed(Keys.D);

        if (left || right) {
            if (!leftRightPressed) {
                leftRightPressed = true;
                handleLeftRight(right ? 0.1f : -0.1f);
                refreshLabels();
            }
        } else {
            leftRightPressed = false;
        }

        // enter to confirm or start rebinding
        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;
                handleEnter();
                refreshLabels();
            }
        } else {
            enterPressed = false;
        }

        // escape to go back to the main menu
        if (inputKeyCheckable.isKeyPressed(Keys.ESCAPE)) {
            if (!escPressed) {
                escPressed = true;
                sceneSwitchable.switchScene("StartScene");
            }
        } else {
            escPressed = false;
        }
    }

    // adjusts music or sfx volume based on which row is selected
    private void handleLeftRight(float delta) {
        SettingsManager settings = SettingsManager.getInstance();
        OutputManager output = OutputManager.getInstance();

        if (selectedIndex == 0) {
            settings.setMusicVolume(settings.getMusicVolume() + delta);
            output.getBGManager().setVolume(settings.getMusicVolume());
        } else if (selectedIndex == 1) {
            settings.setSfxVolume(settings.getSfxVolume() + delta);
            output.getSFXManager().setVolume(settings.getSfxVolume());
        }
    }

    // starts key rebinding mode for the selected movement action, or goes back if on the last row
    private void handleEnter() {
        if (selectedIndex >= 2 && selectedIndex <= 5) {
            waitingForRebind = true;
            rebindReady = false;
            InputManager.getInstance().consumeLastJustPressedKey();
        } else if (selectedIndex == 6) {
            sceneSwitchable.switchScene("StartScene");
        }
    }

    // updates label colors and value text to reflect current settings and selection
    private void refreshLabels() {
        SettingsManager settings = SettingsManager.getInstance();

        for (int i = 0; i < options.length; i++) {
            optionLabels[i].setColor(i == selectedIndex ? Color.YELLOW : Color.WHITE);
            valueLabels[i].setColor(i == selectedIndex ? Color.YELLOW : Color.LIGHT_GRAY);
        }

        valueLabels[0].setText((int)(settings.getMusicVolume() * 100) + "%");
        valueLabels[1].setText((int)(settings.getSfxVolume() * 100) + "%");
        valueLabels[2].setText(selectedIndex == 2 && waitingForRebind ? "Press key..." : Keys.toString(settings.getKeybind("MOVE_UP")));
        valueLabels[3].setText(selectedIndex == 3 && waitingForRebind ? "Press key..." : Keys.toString(settings.getKeybind("MOVE_DOWN")));
        valueLabels[4].setText(selectedIndex == 4 && waitingForRebind ? "Press key..." : Keys.toString(settings.getKeybind("MOVE_LEFT")));
        valueLabels[5].setText(selectedIndex == 5 && waitingForRebind ? "Press key..." : Keys.toString(settings.getKeybind("MOVE_RIGHT")));
        valueLabels[6].setText(waitingForRebind ? "Press any key..." : "Enter");
    }
}
