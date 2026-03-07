package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.interfaces.ICollidableUnregisterable;
import io.github.inf1009_p10_9.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.interfaces.IMusicPlayable;
import io.github.inf1009_p10_9.interfaces.IRenderUnregisterable;
import io.github.inf1009_p10_9.ui.TextLabel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input.Keys;

public class SubjectSelectScene extends Scene {

    private TextLabel titleLabel;
    private TextLabel subjectTitleLabel;
    private TextLabel difficultyTitleLabel;
    private TextLabel backInstructionLabel;

    // subject options
    private TextLabel[] subjectLabels;
    private TextLabel[] subjectArrows;
    private String[] subjectOptions = { "Math", "English" };

    // difficulty options
    private TextLabel[] difficultyLabels;
    private TextLabel[] difficultyArrows;
    private String[] difficultyOptions = { "Easy", "Hard" };

    private int highlightedSubjectIndex = 0;
    private int highlightedDifficultyIndex = 0;

    // tracks which row the cursor is in: 0 = subject, 1 = difficulty
    private int activeRow = 0;

    // input state
    private boolean upDownPressed = false;
    private boolean leftRightPressed = false;
    private boolean enterPressed = false;
    private boolean escPressed = false;
    private float sceneLoadTime = 0;

    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color HIGHLIGHTED_COLOR = Color.YELLOW;
    private static final Color SECTION_TITLE_COLOR = Color.CYAN;
    private static final Color ACTIVE_ROW_COLOR = Color.YELLOW;
    private static final Color INACTIVE_ROW_COLOR = Color.LIGHT_GRAY;

    private IInputKeyCheckable inputKeyCheckable;

    public SubjectSelectScene(IInputKeyCheckable inputKeyCheckable,
                               ICollidableUnregisterable collidableUnregisterable,
                               IRenderUnregisterable renderUnregisterable,
                               IMusicPlayable musicPlayable) {
        super("SubjectSelectScene", collidableUnregisterable, renderUnregisterable, musicPlayable);
        this.inputKeyCheckable = inputKeyCheckable;
    }

    @Override
    protected void loadEntities() {
        titleLabel = new TextLabel("SELECT LEVEL", 260, 520);
        titleLabel.setColor(Color.GREEN);
        addUI(titleLabel);

        // subject row
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

        // difficulty row
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

        // back instruction
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

        sceneLoadTime += com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        if (sceneLoadTime < 0.2f) {
            return;
        }

        // switch active row with UP or DOWN (or W/S)
        boolean upKeyPressed = inputKeyCheckable.isKeyPressed(Keys.UP) ||
                               inputKeyCheckable.isKeyPressed(Keys.W);
        boolean downKeyPressed = inputKeyCheckable.isKeyPressed(Keys.DOWN) ||
                                 inputKeyCheckable.isKeyPressed(Keys.S);

        if (upKeyPressed || downKeyPressed) {
            if (!upDownPressed) {
                upDownPressed = true;

                if (downKeyPressed && activeRow == 0) {
                    activeRow = 1; // move to difficulty row
                } else if (upKeyPressed && activeRow == 1) {
                    activeRow = 0; // move back to subject row
                }

                updateHighlight();
            }
        } else {
            upDownPressed = false;
        }

        // cycle options within active row with LEFT or RIGHT (or A/D)
        boolean leftKeyPressed = inputKeyCheckable.isKeyPressed(Keys.LEFT) ||
                                 inputKeyCheckable.isKeyPressed(Keys.A);
        boolean rightKeyPressed = inputKeyCheckable.isKeyPressed(Keys.RIGHT) ||
                                  inputKeyCheckable.isKeyPressed(Keys.D);

        if (leftKeyPressed || rightKeyPressed) {
            if (!leftRightPressed) {
                leftRightPressed = true;

                if (activeRow == 0) {
                    // cycling through subjects
                    if (rightKeyPressed) {
                        highlightedSubjectIndex++;
                    } else {
                        highlightedSubjectIndex--;
                    }

                    // wrap around
                    if (highlightedSubjectIndex < 0) {
                        highlightedSubjectIndex = subjectOptions.length - 1;
                    }
                    if (highlightedSubjectIndex >= subjectOptions.length) {
                        highlightedSubjectIndex = 0;
                    }

                } else {
                    // cycling through difficulties
                    if (rightKeyPressed) {
                        highlightedDifficultyIndex++;
                    } else {
                        highlightedDifficultyIndex--;
                    }

                    // wrap around
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

        // confirm with ENTER
        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;

                String chosenSubject = subjectOptions[highlightedSubjectIndex];
                String chosenDifficulty = difficultyOptions[highlightedDifficultyIndex];

                GameContext.getQuestionManager().selectBank(chosenSubject, chosenDifficulty);

                System.out.println("Starting: " + chosenSubject + " - " + chosenDifficulty);
                GameContext.getSceneManager().switchScene("GameScene");
            }
        } else {
            enterPressed = false;
        }

        // go back to StartScene with ESC
        if (inputKeyCheckable.isKeyPressed(Keys.ESCAPE)) {
            if (!escPressed) {
                escPressed = true;
                GameContext.getSceneManager().switchScene("StartScene");
            }
        } else {
            escPressed = false;
        }
    }

    private void updateHighlight() {
        // update subject row
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

        // update difficulty row
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