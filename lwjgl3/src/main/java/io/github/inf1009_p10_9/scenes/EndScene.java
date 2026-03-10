package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.interfaces.IMusicPlayable;
import io.github.inf1009_p10_9.interfaces.ICollidableRegisterable;
import io.github.inf1009_p10_9.interfaces.IRenderRegisterable;
import io.github.inf1009_p10_9.interfaces.ISFXPlayable;
import io.github.inf1009_p10_9.interfaces.ISceneSwitchable;
import io.github.inf1009_p10_9.interfaces.IEntityRegisterable;
import io.github.inf1009_p10_9.interfaces.IUIDisplayable;
import io.github.inf1009_p10_9.questions.QuestionManager;
import io.github.inf1009_p10_9.ui.FontManager;
import io.github.inf1009_p10_9.ui.TextLabel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;


public class EndScene extends Scene {
    private TextLabel titleLabel;
    private TextLabel subjectLabel;
    private TextLabel scoreLabel;

    private TextLabel[] menuOptionLabels;
    private TextLabel[] arrowIndicators;
    private String[] menuOptions = { "Restart", "Main Menu", "Quit Game" };

    private int highlightedIndex = 0;

    private boolean upDownPressed = false;
    private boolean enterPressed = false;
    private float sceneLoadTime = 0;


    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color HIGHLIGHTED_COLOR = Color.YELLOW;

    private IInputKeyCheckable inputKeyCheckable;
    private ISceneSwitchable sceneSwitchable;
    private QuestionManager questionManager;
    private FontManager fontManager;

    public EndScene(IEntityRegisterable entityRegisterable,
            IUIDisplayable uiDisplayable,
            ICollidableRegisterable collidableRegisterable,
            IRenderRegisterable renderRegisterable,
            IMusicPlayable musicPlayable,
            IInputKeyCheckable inputKeyCheckable,
            ISceneSwitchable sceneSwitchable,
            QuestionManager questionManager,
            FontManager fontManager) {
			super("EndScene",
			      entityRegisterable,
			      uiDisplayable,
			      collidableRegisterable,
			      renderRegisterable,
			      musicPlayable);
			this.inputKeyCheckable = inputKeyCheckable;
			this.sceneSwitchable = sceneSwitchable;
			this.questionManager = questionManager;
			this.fontManager = fontManager;
			}


    @Override
    protected void loadEntities() {
        float screenWidth = Gdx.graphics.getWidth();
        float centerX = screenWidth / 2;

        titleLabel = new TextLabel("WELL DONE!", centerX - 80, 500);
        titleLabel.setColor(Color.YELLOW);
        addUI(titleLabel);

        subjectLabel = new TextLabel("", centerX - 100, 430);
        subjectLabel.setColor(Color.CYAN);
        addUI(subjectLabel);

        scoreLabel = new TextLabel("", centerX - 100, 370);
        scoreLabel.setColor(Color.WHITE);
        addUI(scoreLabel);

        menuOptionLabels = new TextLabel[menuOptions.length];
        arrowIndicators = new TextLabel[menuOptions.length];

        float startY = 280;
        float spacingY = 60;

        for (int i = 0; i < menuOptions.length; i++) {
            arrowIndicators[i] = new TextLabel("->", centerX - 130, startY - (i * spacingY));
            arrowIndicators[i].setColor(HIGHLIGHTED_COLOR);
            addUI(arrowIndicators[i]);

            menuOptionLabels[i] = new TextLabel(menuOptions[i], centerX - 90, startY - (i * spacingY));
            menuOptionLabels[i].setColor(NORMAL_COLOR);
            addUI(menuOptionLabels[i]);
        }

        updateHighlight();

        System.out.println("EndScene loaded");
    }

    @Override
    public void load() {
        super.load();
        sceneLoadTime = 0;
        highlightedIndex = 0;
        enterPressed = false;
        upDownPressed = false;

        // update score and subject labels with current game results
        String subject = questionManager.getActiveSubject();
        String difficulty = questionManager.getActiveDifficulty();
        int score = questionManager.getScore();
        int total = questionManager.getTotalQuestions();

        subjectLabel.setText(subject + " - " + difficulty);
        scoreLabel.setText("Score: " + score + " / " + total);
    }

    @Override
    public void update() {
        super.update();

        sceneLoadTime += Gdx.graphics.getDeltaTime();

        if (sceneLoadTime < 0.2f) {
            return;
        }

        // navigate with UP/DOWN or W/S
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

        // confirm with ENTER
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
                menuOptionLabels[i].setColor(HIGHLIGHTED_COLOR);
                arrowIndicators[i].setVisible(true);
            } else {
                menuOptionLabels[i].setColor(NORMAL_COLOR);
                arrowIndicators[i].setVisible(false);
            }
        }
    }

    private void handleMenuSelection() {
        String selectedOption = menuOptions[highlightedIndex];

        if (selectedOption.equals("Restart")) {
            System.out.println("Replaying same ...");
            questionManager.replayCurrentBank();
            sceneSwitchable.switchScene("GameScene");

        } else if (selectedOption.equals("Main Menu")) {
            System.out.println("Returning to StartScene...");
            sceneSwitchable.switchScene("StartScene");

        } else if (selectedOption.equals("Quit Game")) {
            System.out.println("Quitting game...");
            Gdx.app.exit();
        }
    }
}
