package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.interfaces.IMusicPlayable;
import io.github.inf1009_p10_9.interfaces.ICollidableRegisterable;
import io.github.inf1009_p10_9.interfaces.IRenderRegisterable;
import io.github.inf1009_p10_9.interfaces.ISceneSwitchable;
import io.github.inf1009_p10_9.interfaces.IEntityRegisterable;
import io.github.inf1009_p10_9.interfaces.IUIDisplayable;
import io.github.inf1009_p10_9.questions.QuestionManager;
import io.github.inf1009_p10_9.ui.BackgroundElement;
import io.github.inf1009_p10_9.ui.CarElement;
import io.github.inf1009_p10_9.ui.CloudElement;
import io.github.inf1009_p10_9.ui.FontManager;
import io.github.inf1009_p10_9.ui.MenuButtonElement;
import io.github.inf1009_p10_9.ui.TextLabel;
import io.github.inf1009_p10_9.ui.TitleElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

// the results screen, showing performance feedback, animated background effects, and navigation buttons
public class EndScene extends Scene {

    // result bands used to drive different animation intensity
    private enum ResultTier {
        EXCELLENT,
        GOOD,
        TRY_AGAIN
    }

    // ui elements
    private TitleElement titleElement;
    private TextLabel resultLabel;
    private TextLabel subjectLabel;
    private TextLabel scoreLabel;
    private TextLabel feedbackLabel;
    private TextLabel instructionLabel;
    private TextLabel statusLabel;
    private TextLabel[] menuOptionLabels;
    private TextLabel[] arrowIndicators;
    private MenuButtonElement[] menuButtons;
    private CloudElement[] clouds;
    private CarElement resultCar;

    // menu options
    private final String[] menuOptions = { "Restart", "Main Menu", "Quit Game" };

    // result state
    private ResultTier resultTier = ResultTier.GOOD;

    // navigation state
    private int highlightedIndex = 0;
    private boolean upDownPressed = false;
    private boolean enterPressed = false;
    private boolean escPressed = false;
    private float sceneLoadTime = 0f;

    // animation timers
    private float titleBounceTimer = 0f;
    private float arrowPulseTimer = 0f;
    private float resultAnimTimer = 0f;

    // button sizing
    private static final float BUTTON_WIDTH = 340f;
    private static final float BUTTON_HEIGHT = 52f;

    // button colors
    private static final Color[] BUTTON_COLORS = {
        new Color(0.18f, 0.55f, 0.82f, 0.88f),
        new Color(0.48f, 0.28f, 0.78f, 0.88f),
        new Color(0.82f, 0.28f, 0.28f, 0.88f)
    };
    private static final Color BUTTON_HIGHLIGHT_COLOR = new Color(1f, 0.85f, 0.12f, 0.96f);

    // text colors
    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color HIGHLIGHTED_COLOR = new Color(0.08f, 0.08f, 0.08f, 1f);
    private static final Color ARROW_COLOR = new Color(1f, 0.9f, 0.1f, 1f);

    // external dependencies injected via constructor
    private final IInputKeyCheckable inputKeyCheckable;
    private final ISceneSwitchable sceneSwitchable;
    private final QuestionManager questionManager;
    private final FontManager fontManager;

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

    // builds and registers all visual elements for the scene
    @Override
    protected void loadEntities() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float centerX = screenWidth / 2f;

        // background at z-index -100
        BackgroundElement background = new BackgroundElement();
        addUI(background);

        // drifting clouds at z-index -50
        clouds = new CloudElement[3];
        clouds[0] = new CloudElement(100, screenHeight * 0.88f, 160, 50, 24f);
        clouds[1] = new CloudElement(500, screenHeight * 0.93f, 210, 62, 18f);
        clouds[2] = new CloudElement(960, screenHeight * 0.87f, 150, 46, 28f);
        for (CloudElement cloud : clouds) {
            addUI(cloud);
        }

        // car driving across the grass strip, reused as the result animation element
        float grassY = screenHeight * 0.16f;
        resultCar = new CarElement(
            100, grassY - 26, 110f,
            0, 133, 64, 26,
            128, 52
        );
        addUI(resultCar);

        // title and result section
        titleElement = new TitleElement("RESULTS", fontManager.getLargeFont(), Color.GREEN);
        addUI(titleElement);

        resultLabel = new TextLabel("", centerX - 70, 500, fontManager.getMediumFont());
        resultLabel.setColor(new Color(1f, 0.9f, 0.1f, 1f));
        addUI(resultLabel);

        subjectLabel = new TextLabel("", centerX - 95, 460, fontManager.getMediumFont());
        subjectLabel.setColor(Color.CYAN);
        addUI(subjectLabel);

        scoreLabel = new TextLabel("", centerX - 110, 410, fontManager.getMediumFont());
        scoreLabel.setColor(Color.WHITE);
        addUI(scoreLabel);

        feedbackLabel = new TextLabel("", centerX - 180, 372, fontManager.getSmallFont());
        feedbackLabel.setColor(new Color(0.95f, 0.95f, 0.95f, 1f));
        addUI(feedbackLabel);

        statusLabel = new TextLabel("", centerX - 60, 345, fontManager.getMediumFont());
        statusLabel.setColor(new Color(1f, 0.4f, 0.2f, 1f));
        addUI(statusLabel);

        instructionLabel = new TextLabel(
            "UP / DOWN to move   |   ENTER to confirm   |   ESC to return",
            centerX - 245, 315, fontManager.getSmallFont()
        );
        instructionLabel.setColor(Color.WHITE);
        addUI(instructionLabel);

        // menu buttons and labels
        menuOptionLabels = new TextLabel[menuOptions.length];
        arrowIndicators = new TextLabel[menuOptions.length];
        menuButtons = new MenuButtonElement[menuOptions.length];

        float startY = 240f;
        float spacingY = 72f;
        float buttonX = centerX - BUTTON_WIDTH / 2f;

        for (int i = 0; i < menuOptions.length; i++) {
            float rowY = startY - (i * spacingY);

            menuButtons[i] = new MenuButtonElement(
                buttonX, rowY - BUTTON_HEIGHT + 10,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                BUTTON_COLORS[i], BUTTON_HIGHLIGHT_COLOR
            );
            addUI(menuButtons[i]);

            float arrowX = buttonX - 45;
            arrowIndicators[i] = new TextLabel(">>", arrowX, rowY, fontManager.getMediumFont());
            arrowIndicators[i].setColor(ARROW_COLOR);
            arrowIndicators[i].setZIndex(200);
            addUI(arrowIndicators[i]);

            GlyphLayout layout = new GlyphLayout(fontManager.getMediumFont(), menuOptions[i]);
            menuOptionLabels[i] = new TextLabel(
                menuOptions[i],
                centerX - layout.width / 2f,
                rowY,
                fontManager.getMediumFont()
            );
            menuOptionLabels[i].setColor(NORMAL_COLOR);
            addUI(menuOptionLabels[i]);
        }

        updateHighlight();
        System.out.println("EndScene loaded");
    }

    // resets scene state and updates the displayed result each time the screen is shown
    @Override
    public void load() {
        super.load();

        sceneLoadTime = 0f;
        highlightedIndex = 0;
        upDownPressed = false;
        enterPressed = false;
        escPressed = false;

        titleBounceTimer = 0f;
        arrowPulseTimer = 0f;
        resultAnimTimer = 0f;

        String subject = questionManager.getActiveSubject();
        String difficulty = questionManager.getActiveDifficulty();
        int score = questionManager.getScore();
        int total = questionManager.getTotalQuestions();

        subjectLabel.setText(subject + "  -  " + difficulty);
        scoreLabel.setText("Score: " + score + " / " + total);
        statusLabel.setText("");

        float percentage = total == 0 ? 0f : ((float) score / total) * 100f;

        if (percentage >= 80f) {
            resultTier = ResultTier.EXCELLENT;
            resultLabel.setText("Excellent!");
            resultLabel.setColor(new Color(0.25f, 1f, 0.4f, 1f));
            feedbackLabel.setText("Brilliant run. You really locked in this round.");
        } else if (percentage >= 50f) {
            resultTier = ResultTier.GOOD;
            resultLabel.setText("Nice Job!");
            resultLabel.setColor(new Color(1f, 0.9f, 0.1f, 1f));
            feedbackLabel.setText("Solid effort. One more run can push this even higher.");
        } else {
            resultTier = ResultTier.TRY_AGAIN;
            resultLabel.setText("Keep Going!");
            resultLabel.setColor(new Color(1f, 0.65f, 0.2f, 1f));
            feedbackLabel.setText("You are getting there. Try again and beat your score.");
        }

        updateHighlight();
    }

    @Override
    public void update() {
        super.update();

        float delta = Gdx.graphics.getDeltaTime();
        sceneLoadTime += delta;
        resultAnimTimer += delta;

        // animate background clouds, faster for better results
        for (CloudElement cloud : clouds) {
            cloud.update(delta);

            if (resultTier == ResultTier.EXCELLENT) {
                cloud.update(delta * 0.45f);
            } else if (resultTier == ResultTier.TRY_AGAIN) {
                cloud.update(delta * 0.10f);
            }
        }

        // animate the result car using its built-in update logic
        resultCar.update(delta);
        if (resultTier == ResultTier.EXCELLENT) {
            resultCar.update(delta * 0.55f);
        } else if (resultTier == ResultTier.TRY_AGAIN) {
            resultCar.update(delta * -0.35f);
        }

        // vary title and feedback motion by result tier
        animateResultState();

        // ignore input briefly after loading to avoid accidental presses
        if (sceneLoadTime < 0.2f) {
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
                    highlightedIndex++;
                } else {
                    highlightedIndex--;
                }

                // wrap around at the top and bottom
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

        // confirm selection on enter
        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;
                handleMenuSelection();
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

        // animate the arrow next to the highlighted option
        animateArrow(delta);
    }

    // applies a different feeling to the results screen depending on performance
    private void animateResultState() {
        float bounceStrength = 6f;
        float feedbackOffset = 0f;

        if (resultTier == ResultTier.EXCELLENT) {
            bounceStrength = 10f;
            feedbackOffset = (float) Math.sin(resultAnimTimer * 4.5f) * 6f;
        } else if (resultTier == ResultTier.GOOD) {
            bounceStrength = 6f;
            feedbackOffset = (float) Math.sin(resultAnimTimer * 2.6f) * 3f;
  
        } else {
            bounceStrength = 4f;
            feedbackOffset = (float) Math.sin(resultAnimTimer * 12f) * 4f;
        }

        float bounceOffset = (float) Math.sin(titleBounceTimer * 2.2f) * bounceStrength;
        titleBounceTimer += Gdx.graphics.getDeltaTime();

        titleElement.setY(titleElement.getBaseY() + bounceOffset);

    }

    // refreshes colors, arrow visibility, and button highlight state to match the current selection
    private void updateHighlight() {
        float buttonX = Gdx.graphics.getWidth() / 2f - BUTTON_WIDTH / 2f;
        float arrowX = buttonX - 45;

        for (int i = 0; i < menuOptions.length; i++) {
            if (i == highlightedIndex) {
                menuButtons[i].setHighlighted(true);
                menuOptionLabels[i].setColor(HIGHLIGHTED_COLOR);
                arrowIndicators[i].setVisible(true);
                arrowPulseTimer = 0f;
            } else {
                menuButtons[i].setHighlighted(false);
                menuOptionLabels[i].setColor(NORMAL_COLOR);
                arrowIndicators[i].setVisible(false);
                arrowIndicators[i].setPosition(arrowX, arrowIndicators[i].getY());
            }
        }
    }

    // routes the confirmed selection to the appropriate scene or action
    private void handleMenuSelection() {
        String selectedOption = menuOptions[highlightedIndex];

        if (selectedOption.equals("Restart")) {
            System.out.println("Replaying same question bank...");
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

    // adds a small pulsing motion to the visible selection arrow
    private void animateArrow(float delta) {
        float buttonX = Gdx.graphics.getWidth() / 2f - BUTTON_WIDTH / 2f;
        float arrowBaseX = buttonX - 45;

        arrowPulseTimer += delta;
        float arrowOffset = (float) Math.sin(arrowPulseTimer * 6f) * 5f;

        for (int i = 0; i < arrowIndicators.length; i++) {
            if (i == highlightedIndex) {
                arrowIndicators[i].setPosition(arrowBaseX + arrowOffset, arrowIndicators[i].getY());
            }
        }
    }
}