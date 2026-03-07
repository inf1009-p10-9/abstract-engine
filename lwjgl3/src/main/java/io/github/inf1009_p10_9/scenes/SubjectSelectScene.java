package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.TextLabel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class SubjectSelectScene extends Scene {

    private TextLabel titleLabel;
    private TextLabel subjectTitleLabel;
    private TextLabel difficultyTitleLabel;
    private TextLabel backInstructionLabel;

    private TextLabel[] subjectLabels;
    private TextLabel[] subjectArrows;
    private String[] subjectOptions = { "Math", "English" };

    private TextLabel[] difficultyLabels;
    private TextLabel[] difficultyArrows;
    private String[] difficultyOptions = { "Easy", "Hard" };

    private int highlightedSubjectIndex = 0;
    private int highlightedDifficultyIndex = 0;
    private int activeRow = 0; // 0 = subject row, 1 = difficulty row

    private boolean upDownPressed = false;
    private boolean leftRightPressed = false;
    private boolean enterPressed = false;
    private boolean escPressed = false;
    private float sceneLoadTime = 0;

    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color HIGHLIGHTED_COLOR = Color.YELLOW;
    private static final Color SECTION_TITLE_COLOR = Color.CYAN;
    private static final Color INACTIVE_ROW_COLOR = Color.LIGHT_GRAY;

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
        titleLabel = new TextLabel("SELECT LEVEL", 260, 520);
        titleLabel.setColor(Color.GREEN);
        addUI(titleLabel);

        subjectTitleLabel = new TextLabel("Subject:", 100, 400);
        subjectTitleLabel.setColor(SECTION_TITLE_COLOR);
        addUI(subjectTitleLabel);

        subjectLabels = new TextLabel[subjectOptions.length];
        subjectArrows = new TextLabel[subjectOptions.length];

        float subjectStartX = 230;
        float subjectSpacingX = 150;

        for (int i = 0; i < subjectOptions.length; i++) {
            subjectArrows[i] = new TextLabel("->", subjectStartX + (i * subjectSpacingX) - 30, 400);
            subjectArrows[i].setColor(HIGHLIGHTED_COLOR);
            addUI(subjectArrows[i]);

            subjectLabels[i] = new TextLabel(subjectOptions[i], subjectStartX + (i * subjectSpacingX), 400);
            subjectLabels[i].setColor(NORMAL_COLOR);
            addUI(subjectLabels[i]);
        }

        difficultyTitleLabel = new TextLabel("Difficulty:", 100, 280);
        difficultyTitleLabel.setColor(SECTION_TITLE_COLOR);
        addUI(difficultyTitleLabel);

        difficultyLabels = new TextLabel[difficultyOptions.length];
        difficultyArrows = new TextLabel[difficultyOptions.length];

        float difficultyStartX = 230;
        float difficultySpacingX = 150;

        for (int i = 0; i < difficultyOptions.length; i++) {
            difficultyArrows[i] = new TextLabel("->", difficultyStartX + (i * difficultySpacingX) - 30, 280);
            difficultyArrows[i].setColor(HIGHLIGHTED_COLOR);
            addUI(difficultyArrows[i]);

            difficultyLabels[i] = new TextLabel(difficultyOptions[i], difficultyStartX + (i * difficultySpacingX), 280);
            difficultyLabels[i].setColor(NORMAL_COLOR);
            addUI(difficultyLabels[i]);
        }

        backInstructionLabel = new TextLabel("ESC to go back   |   ENTER to confirm", 160, 100);
        backInstructionLabel.setColor(Color.LIGHT_GRAY);
        addUI(backInstructionLabel);

        updateHighlight();
        System.out.println("SubjectSelectScene loaded");
    }

    @Override
    public void load() {
        super.load();
        sceneLoadTime = 0;
        highlightedSubjectIndex = 0;
        highlightedDifficultyIndex = 0;
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

        boolean leftKeyPressed = inputKeyCheckable.isKeyPressed(Keys.LEFT) ||
                                 inputKeyCheckable.isKeyPressed(Keys.A);
        boolean rightKeyPressed = inputKeyCheckable.isKeyPressed(Keys.RIGHT) ||
                                  inputKeyCheckable.isKeyPressed(Keys.D);

        if (leftKeyPressed || rightKeyPressed) {
            if (!leftRightPressed) {
                leftRightPressed = true;

                if (activeRow == 0) {
                    if (rightKeyPressed) {
                        highlightedSubjectIndex++;
                    } else {
                        highlightedSubjectIndex--;
                    }
                    if (highlightedSubjectIndex < 0) {
                        highlightedSubjectIndex = subjectOptions.length - 1;
                    }
                    if (highlightedSubjectIndex >= subjectOptions.length) {
                        highlightedSubjectIndex = 0;
                    }
                } else {
                    if (rightKeyPressed) {
                        highlightedDifficultyIndex++;
                    } else {
                        highlightedDifficultyIndex--;
                    }
                    if (highlightedDifficultyIndex < 0) {
                        highlightedDifficultyIndex = difficultyOptions.length - 1;
                    }
                    if (highlightedDifficultyIndex >= difficultyOptions.length) {
                        highlightedDifficultyIndex = 0;
                    }
                }

                updateHighlight();
            }
        } else {
            leftRightPressed = false;
        }

        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;

                String chosenSubject = subjectOptions[highlightedSubjectIndex];
                String chosenDifficulty = difficultyOptions[highlightedDifficultyIndex];

                GameContext.getQuestionManager().selectBank(chosenSubject, chosenDifficulty);

                System.out.println("Starting: " + chosenSubject + " - " + chosenDifficulty);
                sceneSwitchable.switchScene("GameScene");
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

    private void updateHighlight() {
        for (int i = 0; i < subjectOptions.length; i++) {
            if (i == highlightedSubjectIndex) {
                subjectArrows[i].setVisible(true);
                if (activeRow == 0) {
                    subjectLabels[i].setColor(HIGHLIGHTED_COLOR);
                    subjectArrows[i].setColor(HIGHLIGHTED_COLOR);
                } else {
                    subjectLabels[i].setColor(INACTIVE_ROW_COLOR);
                    subjectArrows[i].setColor(INACTIVE_ROW_COLOR);
                }
            } else {
                subjectArrows[i].setVisible(false);
                subjectLabels[i].setColor(NORMAL_COLOR);
            }
        }

        for (int i = 0; i < difficultyOptions.length; i++) {
            if (i == highlightedDifficultyIndex) {
                difficultyArrows[i].setVisible(true);
                if (activeRow == 1) {
                    difficultyLabels[i].setColor(HIGHLIGHTED_COLOR);
                    difficultyArrows[i].setColor(HIGHLIGHTED_COLOR);
                } else {
                    difficultyLabels[i].setColor(INACTIVE_ROW_COLOR);
                    difficultyArrows[i].setColor(INACTIVE_ROW_COLOR);
                }
            } else {
                difficultyArrows[i].setVisible(false);
                difficultyLabels[i].setColor(NORMAL_COLOR);
            }
        }
    }
}