package io.github.inf1009_p10_9.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

import io.github.inf1009_p10_9.managers.SettingsManager;
import io.github.inf1009_p10_9.managers.InputManager;
import io.github.inf1009_p10_9.managers.OutputManager;
import io.github.inf1009_p10_9.ui.TextLabel;
import io.github.inf1009_p10_9.interfaces.*;

public class SettingsScene extends Scene {
    private final IInputKeyCheckable inputKeyCheckable;
    private final ISceneSwitchable sceneSwitchable;

    private TextLabel titleLabel;
    private TextLabel[] optionLabels;
    private TextLabel[] valueLabels;

    private final String[] options = {
        "Music Volume",
        "SFX Volume",
        "Move Up",
        "Move Down",
        "Move Left",
        "Move Right",
        "Back"
    };

    private final String[] bindActions = {
        null,
        null,
        "MOVE_UP",
        "MOVE_DOWN",
        "MOVE_LEFT",
        "MOVE_RIGHT",
        null
    };

    private int selectedIndex = 0;
    private boolean upDownPressed = false;
    private boolean leftRightPressed = false;
    private boolean enterPressed = false;
    private boolean escPressed = false;
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

        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;
                handleEnter();
                refreshLabels();
            }
        } else {
            enterPressed = false;
        }

        if (inputKeyCheckable.isKeyPressed(Keys.ESCAPE)) {
            if (!escPressed) {
                escPressed = true;
                sceneSwitchable.switchScene("StartScene");
            }
        } else {
            escPressed = false;
        }
    }

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

    private void handleEnter() {
        if (selectedIndex >= 2 && selectedIndex <= 5) {
            waitingForRebind = true;
            rebindReady = false;
            InputManager.getInstance().consumeLastJustPressedKey();
        } else if (selectedIndex == 6) {
            sceneSwitchable.switchScene("StartScene");
        }
    }

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