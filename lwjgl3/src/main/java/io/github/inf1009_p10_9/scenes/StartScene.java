package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.TextLabel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class StartScene extends Scene {

    private TextLabel titleLabel;

    // menu options as labels
    private TextLabel[] menuOptionLabels;
    private TextLabel[] arrowIndicators;
    private String[] menuOptions = { "Level Selection", "Settings", "Quit Game" };

    private int highlightedIndex = 0;

    // input state
    private boolean upDownPressed = false;
    private boolean enterPressed = false;
    private float sceneLoadTime = 0;

    // colors
    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color HIGHLIGHTED_COLOR = Color.YELLOW;
    private static final Color ARROW_COLOR = Color.YELLOW;

    private IInputKeyCheckable inputKeyCheckable;
    private ISceneSwitchable sceneSwitchable;

    public StartScene(IEntityRegisterable entityRegisterable,
                      IUIDisplayable uiDisplayable,
                      ICollidableRegisterable collidableRegisterable,
                      IRenderRegisterable renderRegisterable,
                      IMusicPlayable musicPlayable,

                      IInputKeyCheckable inputKeyCheckable,
                      ISceneSwitchable sceneSwitchable) {
        super("StartScene",
              entityRegisterable,
              uiDisplayable,
              collidableRegisterable,
              renderRegisterable,
              musicPlayable);

        this.inputKeyCheckable = inputKeyCheckable;
        this.sceneSwitchable = sceneSwitchable;
    }

    @Override
    protected void loadEntities() {
        titleLabel = new TextLabel("DRIVE AND LEARN", 230, 500);
        titleLabel.setColor(Color.GREEN);
        addUI(titleLabel);

        menuOptionLabels = new TextLabel[menuOptions.length];
        arrowIndicators = new TextLabel[menuOptions.length];

        // build menu options evenly spaced
        float startY = 360;
        float spacingY = 70;

        for (int i = 0; i < menuOptions.length; i++) {
            // arrow indicator to the left of each option
            arrowIndicators[i] = new TextLabel("->", 170, startY - (i * spacingY));
            arrowIndicators[i].setColor(ARROW_COLOR);
            addUI(arrowIndicators[i]);

            // menu option label
            menuOptionLabels[i] = new TextLabel(menuOptions[i], 210, startY - (i * spacingY));
            menuOptionLabels[i].setColor(NORMAL_COLOR);
            addUI(menuOptionLabels[i]);
        }

        // apply initial highlight
        updateHighlight();

        System.out.println("StartScene loaded");
    }

    @Override
    public void load() {
        super.load();
        sceneLoadTime = 0;
        highlightedIndex = 0;
        musicPlayable.playMusic("music/Super Mario Bros. medley.mp3");
    }

    @Override
    public void update() {
        super.update();

        sceneLoadTime += Gdx.graphics.getDeltaTime();

        if (sceneLoadTime < 0.2f) {
            return;
        }

        // navigate up with UP or W
        boolean upKeyPressed = inputKeyCheckable.isKeyPressed(Keys.UP) ||
                               inputKeyCheckable.isKeyPressed(Keys.W);

        // navigate down with DOWN or S
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

                // wrap around
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

        // confirm selection with ENTER
        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;
                handleMenuSelection();
            }
        } else {
            enterPressed = false;
        }
    }

    private void updateHighlight() {
        for (int i = 0; i < menuOptions.length; i++) {
            if (i == highlightedIndex) {
                // highlighted option gets yellow color and visible arrow
                menuOptionLabels[i].setColor(HIGHLIGHTED_COLOR);
                arrowIndicators[i].setVisible(true);
            } else {
                // other options are white with hidden arrow
                menuOptionLabels[i].setColor(NORMAL_COLOR);
                arrowIndicators[i].setVisible(false);
            }
        }
    }

    private void handleMenuSelection() {
        String selectedOption = menuOptions[highlightedIndex];

        if (selectedOption.equals("Level Selection")) {
            System.out.println("Going to SubjectSelectScene...");
            sceneSwitchable.switchScene("SubjectSelectScene");

        } else if (selectedOption.equals("Settings")) {
            System.out.println("Settings not yet implemented");
            // GameContext.getSceneManager().switchScene("SettingsScene");

        } else if (selectedOption.equals("Quit Game")) {
            System.out.println("Quitting game...");
            Gdx.app.exit();
        }
    }
}
