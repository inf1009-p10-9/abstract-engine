package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.TextLabel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class SubjectSelectScene extends Scene {

    private TextLabel titleLabel;

    // subject row labels
    private TextLabel subjectTitleLabel;
    private TextLabel subjectLeftArrow;
    private TextLabel subjectValueLabel;
    private TextLabel subjectRightArrow;

    // difficulty row labels
    private TextLabel difficultyTitleLabel;
    private TextLabel difficultyLeftArrow;
    private TextLabel difficultyValueLabel;
    private TextLabel difficultyRightArrow;

    // instruction labels
    private TextLabel instructionLine1;
    private TextLabel instructionLine2;

    private String[] subjectOptions = { "Math", "English" };
    private String[] difficultyOptions = { "Easy", "Hard" };

    private int selectedSubjectIndex = 0;
    private int selectedDifficultyIndex = 0;
    private int activeRow = 0; // 0 = subject row, 1 = difficulty row

    private boolean upDownPressed = false;
    private boolean leftRightPressed = false;
    private boolean enterPressed = false;
    private boolean escPressed = false;
    private float sceneLoadTime = 0;

    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color ACTIVE_ROW_COLOR = Color.YELLOW;
    private static final Color INACTIVE_ROW_COLOR = Color.LIGHT_GRAY;
    private static final Color SECTION_TITLE_COLOR = Color.CYAN;

    private IInputKeyCheckable inputKeyCheckable;
    private ISceneSwitchable sceneSwitchable;

    public SubjectSelectScene(IEntityRegisterable entityRegisterable,
                               IUIDisplayable uiDisplayable,
                               ICollidableRegisterable collidableRegisterable,
                               IRenderRegisterable renderRegisterable,
                               IMusicPlayable musicPlayable,
                               IInputKeyCheckable inputKeyCheckable,
                               ISceneSwitchable sceneSwitchable) {
        super("SubjectSelectScene",
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
        float screenWidth = Gdx.graphics.getWidth();
        float centerX = screenWidth / 2;

        titleLabel = new TextLabel("SELECT LEVEL", centerX - 80, 550);
        titleLabel.setColor(Color.GREEN);
        addUI(titleLabel);

        // subject row
        subjectTitleLabel = new TextLabel("Subject:", centerX - 200, 420);
        subjectTitleLabel.setColor(SECTION_TITLE_COLOR);
        addUI(subjectTitleLabel);

        subjectLeftArrow = new TextLabel("<", centerX - 30, 420);
        subjectLeftArrow.setColor(ACTIVE_ROW_COLOR);
        addUI(subjectLeftArrow);

        subjectValueLabel = new TextLabel(subjectOptions[selectedSubjectIndex], centerX + 20, 420);
        subjectValueLabel.setColor(ACTIVE_ROW_COLOR);
        addUI(subjectValueLabel);

        subjectRightArrow = new TextLabel(">", centerX + 110, 420);
        subjectRightArrow.setColor(ACTIVE_ROW_COLOR);
        addUI(subjectRightArrow);

        // difficulty row
        difficultyTitleLabel = new TextLabel("Difficulty:", centerX - 200, 300);
        difficultyTitleLabel.setColor(SECTION_TITLE_COLOR);
        addUI(difficultyTitleLabel);

        difficultyLeftArrow = new TextLabel("<", centerX - 30, 300);
        difficultyLeftArrow.setColor(INACTIVE_ROW_COLOR);
        addUI(difficultyLeftArrow);

        difficultyValueLabel = new TextLabel(difficultyOptions[selectedDifficultyIndex], centerX + 20, 300);
        difficultyValueLabel.setColor(INACTIVE_ROW_COLOR);
        addUI(difficultyValueLabel);

        difficultyRightArrow = new TextLabel(">", centerX + 110, 300);
        difficultyRightArrow.setColor(INACTIVE_ROW_COLOR);
        addUI(difficultyRightArrow);

        // instructions split across two lines
        instructionLine1 = new TextLabel(
            "UP/DOWN: switch row          LEFT/RIGHT: change value",
            centerX - 240, 130);
        instructionLine1.setColor(Color.LIGHT_GRAY);
        addUI(instructionLine1);

        instructionLine2 = new TextLabel(
            "ENTER: confirm          ESC: go back",
            centerX - 165, 90);
        instructionLine2.setColor(Color.LIGHT_GRAY);
        addUI(instructionLine2);

        updateHighlight();
        System.out.println("SubjectSelectScene loaded");
    }

    @Override
    public void load() {
        super.load();
        sceneLoadTime = 0;
        selectedSubjectIndex = 0;
        selectedDifficultyIndex = 0;
        activeRow = 0;
        enterPressed = false;
        escPressed = false;
    }

    @Override
    public void update() {
        super.update();

        sceneLoadTime += Gdx.graphics.getDeltaTime();
        if (sceneLoadTime < 0.2f) {
            return;
        }

        // switch active row with UP/DOWN or W/S
        boolean upKeyPressed = inputKeyCheckable.isKeyPressed(Keys.UP) ||
                               inputKeyCheckable.isKeyPressed(Keys.W);
        boolean downKeyPressed = inputKeyCheckable.isKeyPressed(Keys.DOWN) ||
                                 inputKeyCheckable.isKeyPressed(Keys.S);

        if (upKeyPressed || downKeyPressed) {
            if (!upDownPressed) {
                upDownPressed = true;

                if (downKeyPressed && activeRow == 0) {
                    activeRow = 1;
                } else if (upKeyPressed && activeRow == 1) {
                    activeRow = 0;
                }

                updateHighlight();
            }
        } else {
            upDownPressed = false;
        }

        // cycle value in active row with LEFT/RIGHT or A/D
        boolean leftKeyPressed = inputKeyCheckable.isKeyPressed(Keys.LEFT) ||
                                 inputKeyCheckable.isKeyPressed(Keys.A);
        boolean rightKeyPressed = inputKeyCheckable.isKeyPressed(Keys.RIGHT) ||
                                  inputKeyCheckable.isKeyPressed(Keys.D);

        if (leftKeyPressed || rightKeyPressed) {
            if (!leftRightPressed) {
                leftRightPressed = true;

                if (activeRow == 0) {
                    if (rightKeyPressed) {
                        selectedSubjectIndex++;
                    } else {
                        selectedSubjectIndex--;
                    }
                    if (selectedSubjectIndex < 0) {
                        selectedSubjectIndex = subjectOptions.length - 1;
                    }
                    if (selectedSubjectIndex >= subjectOptions.length) {
                        selectedSubjectIndex = 0;
                    }
                } else {
                    if (rightKeyPressed) {
                        selectedDifficultyIndex++;
                    } else {
                        selectedDifficultyIndex--;
                    }
                    if (selectedDifficultyIndex < 0) {
                        selectedDifficultyIndex = difficultyOptions.length - 1;
                    }
                    if (selectedDifficultyIndex >= difficultyOptions.length) {
                        selectedDifficultyIndex = 0;
                    }
                }

                updateHighlight();
            }
        } else {
            leftRightPressed = false;
        }

        // confirm with ENTER
        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;

                String chosenSubject = subjectOptions[selectedSubjectIndex];
                String chosenDifficulty = difficultyOptions[selectedDifficultyIndex];

                GameContext.getQuestionManager().selectBank(chosenSubject, chosenDifficulty);

                System.out.println("Starting: " + chosenSubject + " - " + chosenDifficulty);
                sceneSwitchable.switchScene("GameScene");
            }
        } else {
            enterPressed = false;
        }

        // go back with ESC
        if (inputKeyCheckable.isKeyPressed(Keys.ESCAPE)) {
            if (!escPressed) {
                escPressed = true;
                sceneSwitchable.switchScene("StartScene");
            }
        } else {
            escPressed = false;
        }
    }

    private void updateHighlight() {
        if (activeRow == 0) {
            subjectTitleLabel.setColor(SECTION_TITLE_COLOR);
            subjectLeftArrow.setColor(ACTIVE_ROW_COLOR);
            subjectValueLabel.setColor(ACTIVE_ROW_COLOR);
            subjectRightArrow.setColor(ACTIVE_ROW_COLOR);

            difficultyTitleLabel.setColor(INACTIVE_ROW_COLOR);
            difficultyLeftArrow.setColor(INACTIVE_ROW_COLOR);
            difficultyValueLabel.setColor(INACTIVE_ROW_COLOR);
            difficultyRightArrow.setColor(INACTIVE_ROW_COLOR);
        } else {
            subjectTitleLabel.setColor(INACTIVE_ROW_COLOR);
            subjectLeftArrow.setColor(INACTIVE_ROW_COLOR);
            subjectValueLabel.setColor(INACTIVE_ROW_COLOR);
            subjectRightArrow.setColor(INACTIVE_ROW_COLOR);

            difficultyTitleLabel.setColor(SECTION_TITLE_COLOR);
            difficultyLeftArrow.setColor(ACTIVE_ROW_COLOR);
            difficultyValueLabel.setColor(ACTIVE_ROW_COLOR);
            difficultyRightArrow.setColor(ACTIVE_ROW_COLOR);
        }

        // update displayed values
        subjectValueLabel.setText(subjectOptions[selectedSubjectIndex]);
        difficultyValueLabel.setText(difficultyOptions[selectedDifficultyIndex]);
    }
}