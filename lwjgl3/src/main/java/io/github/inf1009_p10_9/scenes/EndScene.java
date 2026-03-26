package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.PlayerState;
import io.github.inf1009_p10_9.economy.concrete.CoinsWallet;
import io.github.inf1009_p10_9.questions.QuestionManager;
import io.github.inf1009_p10_9.ui.FontManager;
import io.github.inf1009_p10_9.ui.MenuButtonElement;
import io.github.inf1009_p10_9.ui.SceneBackdrop;
import io.github.inf1009_p10_9.ui.TextLabel;
import io.github.inf1009_p10_9.ui.TitleElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

// the results screen, showing performance feedback, animated background effects, and navigation buttons
public class EndScene extends MenuScene {
    private final PlayerState playerState;

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
    private SceneBackdrop backdrop;

    // menu options
    private final String[] menuOptions = { "Restart", "Main Menu", "Quit Game" };

    // result state
    private ResultTier resultTier = ResultTier.GOOD;

    // animation timers
    private float titleBounceTimer = 0f;
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

    // external dependencies injected via constructor
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
                    FontManager fontManager,
                    PlayerState playerState,
                    ISettingsKBRetrievable settingsKBRetrievable) {
        super("EndScene",
              entityRegisterable,
              uiDisplayable,
              collidableRegisterable,
              renderRegisterable,
              musicPlayable,
              inputKeyCheckable,
              sceneSwitchable,
              settingsKBRetrievable);
        this.questionManager = questionManager;
        this.fontManager = fontManager;
        this.playerState = playerState;
    }

    // builds and registers all visual elements for the scene
    @Override
    protected void loadEntities() {
        float screenWidth = Gdx.graphics.getWidth();
        float centerX = screenWidth / 2f;

        // shared decorative background
        backdrop = new SceneBackdrop(false);
        backdrop.addToScene(this);

        // title and result section
        titleElement = new TitleElement("RESULTS", fontManager.getLargeFont(), Color.GREEN);
        addUI(titleElement);

        resultLabel = new TextLabel("", centerX - 65, 510, fontManager.getMediumFont());
        resultLabel.setColor(new Color(1f, 0.9f, 0.1f, 1f));
        addUI(resultLabel);

        subjectLabel = new TextLabel("", centerX - 95, 470, fontManager.getMediumFont());
        subjectLabel.setColor(Color.CYAN);
        addUI(subjectLabel);

        scoreLabel = new TextLabel("", centerX - 85, 420, fontManager.getMediumFont());
        scoreLabel.setColor(Color.WHITE);
        addUI(scoreLabel);

        feedbackLabel = new TextLabel("", centerX - 190, 372, fontManager.getSmallFont());
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

            float arrowX = getArrowBaseX();
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
        titleBounceTimer = 0f;
        resultAnimTimer = 0f;
        highlightedIndex = 0;

        String subject = questionManager.getActiveSubject();
        String difficulty = questionManager.getActiveDifficulty();
        int score = questionManager.getScore();
        int total = questionManager.getTotalQuestions();

        playerState.getWalletBag().getWallets(CoinsWallet.class).get(0).creditBalance(score);

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
        resultAnimTimer += delta;

        // animate shared decorative background at different intensity based on performance
        if (resultTier == ResultTier.EXCELLENT) {
            backdrop.update(delta, 1.45f, 1.55f);
        } else if (resultTier == ResultTier.GOOD) {
            backdrop.update(delta, 1f, 1f);
        } else {
            backdrop.update(delta, 1.1f, 0.65f);
        }

        // vary title and feedback motion by result tier
        animateResultState();

        // shared menu navigation
        updateMenuNavigation(menuOptions.length);

        // allow quick return to the main menu with escape
        if (inputKeyCheckable.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
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
        float bounceStrength;
        float feedbackOffset;

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

        titleBounceTimer += Gdx.graphics.getDeltaTime();
        float bounceOffset = (float) Math.sin(titleBounceTimer * 2.2f) * bounceStrength;

        titleElement.setY(titleElement.getBaseY() + bounceOffset);
        feedbackLabel.setY(372 + feedbackOffset);
    }

    @Override
    protected float getArrowBaseX() {
        float buttonX = Gdx.graphics.getWidth() / 2f - BUTTON_WIDTH / 2f;
        return buttonX - 45f;
    }

    // routes the confirmed selection to the appropriate scene or action
    @Override
    protected void handleMenuSelection() {
        String selectedOption = menuOptions[highlightedIndex];

        switch (selectedOption) {
            case "Restart":
                System.out.println("Replaying same question bank...");
                questionManager.replayCurrentBank();
                sceneSwitchable.switchScene("GameScene");

                break;
            case "Main Menu":
                System.out.println("Returning to StartScene...");
                sceneSwitchable.switchScene("StartScene");

                break;
            case "Quit Game":
                System.out.println("Quitting game...");
                Gdx.app.exit();
                break;
        }
    }
}
