package io.github.inf1009_p10_9.game.scenes;

import io.github.inf1009_p10_9.engine.core.Scene;

import io.github.inf1009_p10_9.engine.interfaces.ICollidableRegisterable;
import io.github.inf1009_p10_9.engine.interfaces.IEntityRegisterable;
import io.github.inf1009_p10_9.engine.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.engine.interfaces.IMusicPlayable;
import io.github.inf1009_p10_9.engine.interfaces.IRenderRegisterable;
import io.github.inf1009_p10_9.engine.interfaces.ISceneSwitchable;
import io.github.inf1009_p10_9.engine.interfaces.IUIDisplayable;
import io.github.inf1009_p10_9.game.managers.QuestionManager;
import io.github.inf1009_p10_9.game.ui.BackgroundElement;
import io.github.inf1009_p10_9.game.ui.CarElement;
import io.github.inf1009_p10_9.game.ui.CloudElement;
import io.github.inf1009_p10_9.game.ui.FontLoader;
import io.github.inf1009_p10_9.game.ui.MenuButtonElement;
import io.github.inf1009_p10_9.game.ui.TextLabel;
import io.github.inf1009_p10_9.game.ui.TitleCarElement;
import io.github.inf1009_p10_9.game.ui.TitleElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

// screen where the player picks a subject and difficulty before starting the game
public class SubjectSelectScene extends Scene {

    // dependencies
    private final IInputKeyCheckable inputKeyCheckable;
    private final ISceneSwitchable sceneSwitchable;
    private final QuestionManager questionManager;
    private final FontLoader fontManager;

    // background and decoration elements
    private TitleElement titleElement;
    private TitleCarElement titleCar;
    private CloudElement[] clouds;
    private CarElement grassCar;

    // subject selector row
    private TextLabel subjectTitleLabel;
    private TextLabel subjectLeftArrow;
    private TextLabel subjectValueLabel;
    private TextLabel subjectRightArrow;
    private MenuButtonElement subjectButton;

    // difficulty selector row
    private TextLabel difficultyTitleLabel;
    private TextLabel difficultyLeftArrow;
    private TextLabel difficultyValueLabel;
    private TextLabel difficultyRightArrow;
    private MenuButtonElement difficultyButton;

    private TextLabel instructionLabel;

    // available options for each row
    private String[] subjectOptions = { "Math", "English" };
    private String[] difficultyOptions = { "Easy", "Hard" };

    private int selectedSubjectIndex = 0;
    private int selectedDifficultyIndex = 0;

    // tracks which row the cursor is on: 0 = subject, 1 = difficulty
    private int activeRow = 0;

    // animation and input state
    private float titleBounceTimer = 0f;
    private float sceneLoadTime = 0f;

    private boolean upDownPressed = false;
    private boolean leftRightPressed = false;
    private boolean enterPressed = false;
    private boolean escPressed = false;

    // colors
    private static final Color ACTIVE_COLOR     = new Color(0.1f, 0.1f, 0.1f, 1f);
    private static final Color INACTIVE_COLOR   = new Color(0.75f, 0.75f, 0.75f, 1f);
    private static final Color SECTION_COLOR    = Color.CYAN;
    private static final Color ARROW_ACTIVE   = Color.WHITE;
    private static final Color ARROW_INACTIVE = new Color(1f, 1f, 1f, 0.4f); // faded white for inactive row

    private static final Color SUBJECT_COLOR    = new Color(0.2f, 0.6f, 0.9f, 0.88f);
    private static final Color DIFFICULTY_COLOR = new Color(0.6f, 0.3f, 0.9f, 0.88f);
    private static final Color HIGHLIGHT_COLOR  = new Color(1f, 0.85f, 0.1f, 0.95f);

    private static final float BUTTON_WIDTH  = 320f;
    private static final float BUTTON_HEIGHT = 50f;

    public SubjectSelectScene(IEntityRegisterable entityRegisterable,
            IUIDisplayable uiDisplayable,
            ICollidableRegisterable collidableRegisterable,
            IRenderRegisterable renderRegisterable,
            IMusicPlayable musicPlayable,
            IInputKeyCheckable inputKeyCheckable,
            ISceneSwitchable sceneSwitchable,
            QuestionManager questionManager,
            FontLoader fontManager) {
        super("SubjectSelectScene",
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

    // builds and registers all visual elements for the scene
    @Override
    protected void loadEntities() {
        float screenWidth  = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float centerX      = screenWidth / 2f;

        // background and decorations
        addUI(new BackgroundElement());

        clouds    = new CloudElement[3];
        clouds[0] = new CloudElement(100,  screenHeight * 0.78f, 160, 50, 30f);
        clouds[1] = new CloudElement(500,  screenHeight * 0.84f, 200, 60, 20f);
        clouds[2] = new CloudElement(950,  screenHeight * 0.72f, 140, 45, 35f);
        for (CloudElement cloud : clouds) addUI(cloud);

        float grassY = screenHeight * 0.15f;
        grassCar = new CarElement(100, grassY - 26, 110f, 0, 133, 64, 26, 128, 52);
        addUI(grassCar);

        // title with drop shadow and bouncing car
        titleElement = new TitleElement("SELECT LEVEL", fontManager.getLargeFont(), Color.GREEN);
        addUI(titleElement);

        GlyphLayout layout = new GlyphLayout(fontManager.getLargeFont(), "SELECT LEVEL");
        float titleRightEdge = (screenWidth + layout.width) / 2f;
        titleCar = new TitleCarElement(titleRightEdge + 12, titleElement.getBaseY() - 35,
                                       0, 296, 64, 24, 96, 36);
        addUI(titleCar);

        // selector rows — tighter vertical spacing, fixed column positions inside each button
        float subjectRowY    = 430f;
        float difficultyRowY = 350f;
        float buttonX        = centerX - BUTTON_WIDTH / 2f;

        // fixed x positions for each column within the button
        // left arrow and value pushed further right so they don't clip the row title
        float labelX      = buttonX + 14f;
        float leftArrowX  = buttonX + BUTTON_WIDTH * 0.58f;
        float valueX      = buttonX + BUTTON_WIDTH * 0.66f;
        float rightArrowX = buttonX + BUTTON_WIDTH * 0.90f;

        // subject button background
        subjectButton = new MenuButtonElement(
            buttonX, subjectRowY - BUTTON_HEIGHT + 10,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            SUBJECT_COLOR, HIGHLIGHT_COLOR);
        addUI(subjectButton);

        subjectTitleLabel = new TextLabel("Subject:", labelX, subjectRowY,
                                          fontManager.getMediumFont());
        subjectTitleLabel.setColor(SECTION_COLOR);
        addUI(subjectTitleLabel);

        subjectLeftArrow = new TextLabel("<", leftArrowX, subjectRowY, fontManager.getMediumFont());
        addUI(subjectLeftArrow);

        subjectValueLabel = new TextLabel(subjectOptions[selectedSubjectIndex],
                                          valueX, subjectRowY, fontManager.getMediumFont());
        addUI(subjectValueLabel);

        subjectRightArrow = new TextLabel(">", rightArrowX, subjectRowY, fontManager.getMediumFont());
        addUI(subjectRightArrow);

        // difficulty button background
        difficultyButton = new MenuButtonElement(
            buttonX, difficultyRowY - BUTTON_HEIGHT + 10,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            DIFFICULTY_COLOR, HIGHLIGHT_COLOR);
        addUI(difficultyButton);

        difficultyTitleLabel = new TextLabel("Difficulty:", labelX, difficultyRowY,
                                              fontManager.getMediumFont());
        difficultyTitleLabel.setColor(SECTION_COLOR);
        addUI(difficultyTitleLabel);

        difficultyLeftArrow = new TextLabel("<", leftArrowX, difficultyRowY,
                                            fontManager.getMediumFont());
        addUI(difficultyLeftArrow);

        difficultyValueLabel = new TextLabel(difficultyOptions[selectedDifficultyIndex],
                                             valueX, difficultyRowY,
                                             fontManager.getMediumFont());
        addUI(difficultyValueLabel);

        difficultyRightArrow = new TextLabel(">", rightArrowX, difficultyRowY,
                                             fontManager.getMediumFont());
        addUI(difficultyRightArrow);

        // instruction hint in the grass area
        instructionLabel = new TextLabel(
            "UP/DOWN: row    LEFT/RIGHT: change    ENTER: confirm    ESC: back",
            0, screenHeight * 0.10f, fontManager.getSmallFont());
        instructionLabel.setColor(new Color(1f, 1f, 0.6f, 1f));
        layout.setText(fontManager.getSmallFont(),
            "UP/DOWN: row    LEFT/RIGHT: change    ENTER: confirm    ESC: back");
        instructionLabel.setPosition(centerX - layout.width / 2f, screenHeight * 0.10f);
        addUI(instructionLabel);

        updateHighlight();
        System.out.println("SubjectSelectScene loaded");
    }

    // resets all selections back to defaults on each visit
    @Override
    public void load() {
        super.load();
        sceneLoadTime           = 0f;
        titleBounceTimer        = 0f;
        selectedSubjectIndex    = 0;
        selectedDifficultyIndex = 0;
        activeRow               = 0;
        upDownPressed           = false;
        leftRightPressed        = false;
        enterPressed            = false;
        escPressed              = false;
    }

    @Override
    public void update() {
        super.update();

        float delta = Gdx.graphics.getDeltaTime();
        sceneLoadTime    += delta;
        titleBounceTimer += delta;

        for (CloudElement cloud : clouds) cloud.update(delta);
        grassCar.update(delta);

        // bounce title and keep title car aligned
        float bounceOffset = (float) Math.sin(titleBounceTimer * 2.5f) * 10f;
        titleElement.setY(titleElement.getBaseY() + bounceOffset);
        titleCar.setY(titleElement.getBaseY() + bounceOffset - 35);

        // ignore input briefly after loading to avoid accidental presses
        if (sceneLoadTime < 0.2f) {
            return;
        }

        // up/down to switch between subject and difficulty rows
        boolean upKeyPressed   = inputKeyCheckable.isKeyPressed(Keys.UP)   ||
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

        // left/right to cycle the value in the active row
        boolean leftKeyPressed  = inputKeyCheckable.isKeyPressed(Keys.LEFT)  ||
                                  inputKeyCheckable.isKeyPressed(Keys.A);
        boolean rightKeyPressed = inputKeyCheckable.isKeyPressed(Keys.RIGHT) ||
                                  inputKeyCheckable.isKeyPressed(Keys.D);

        if (leftKeyPressed || rightKeyPressed) {
            if (!leftRightPressed) {
                leftRightPressed = true;

                if (activeRow == 0) {
                    // cycling through subjects
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
                    // cycling through difficulties
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

        // confirm selection and start the game
        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;

                String chosenSubject    = subjectOptions[selectedSubjectIndex];
                String chosenDifficulty = difficultyOptions[selectedDifficultyIndex];

                questionManager.selectBank(chosenSubject, chosenDifficulty);
                System.out.println("Starting: " + chosenSubject + " - " + chosenDifficulty);
                sceneSwitchable.switchScene("CustomisationScene");
            }
        } else {
            enterPressed = false;
        }

        // go back to the main menu
        if (inputKeyCheckable.isKeyPressed(Keys.ESCAPE)) {
            if (!escPressed) {
                escPressed = true;
                sceneSwitchable.switchScene("StartScene");
            }
        } else {
            escPressed = false;
        }
    }

    // refreshes button highlights, arrow colors, and displayed values to match the active row
    private void updateHighlight() {
        if (activeRow == 0) {
            subjectButton.setHighlighted(true);
            subjectTitleLabel.setColor(SECTION_COLOR);
            subjectLeftArrow.setColor(ARROW_ACTIVE);
            subjectValueLabel.setColor(ACTIVE_COLOR);
            subjectRightArrow.setColor(ARROW_ACTIVE);

            difficultyButton.setHighlighted(false);
            difficultyTitleLabel.setColor(INACTIVE_COLOR);
            difficultyLeftArrow.setColor(ARROW_INACTIVE);
            difficultyValueLabel.setColor(INACTIVE_COLOR);
            difficultyRightArrow.setColor(ARROW_INACTIVE);
        } else {
            subjectButton.setHighlighted(false);
            subjectTitleLabel.setColor(INACTIVE_COLOR);
            subjectLeftArrow.setColor(ARROW_INACTIVE);
            subjectValueLabel.setColor(INACTIVE_COLOR);
            subjectRightArrow.setColor(ARROW_INACTIVE);

            difficultyButton.setHighlighted(true);
            difficultyTitleLabel.setColor(SECTION_COLOR);
            difficultyLeftArrow.setColor(ARROW_ACTIVE);
            difficultyValueLabel.setColor(ACTIVE_COLOR);
            difficultyRightArrow.setColor(ARROW_ACTIVE);
        }

        // refresh the displayed text values
        subjectValueLabel.setText(subjectOptions[selectedSubjectIndex]);
        difficultyValueLabel.setText(difficultyOptions[selectedDifficultyIndex]);
    }
}
