package io.github.inf1009_p10_9.scenes;

import com.badlogic.gdx.utils.Array;
import io.github.inf1009_p10_9.entities.*;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.questions.QuestionManager;
import io.github.inf1009_p10_9.ui.FontManager;
import io.github.inf1009_p10_9.ui.LivesElement;
import io.github.inf1009_p10_9.ui.QuestionDisplay;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

// the main gameplay scene, sets up the road, player, gates, and question display
public class GameScene extends Scene {
    private QuestionDisplay questionDisplay;
    private LivesElement livesElement;
    private float endDelay = 0f;
    private final float END_DELAY_DURATION = 1.0f; // seconds
    private boolean endStarted = false;

    // dependencies
    private final IInputKeyCheckable inputKeyCheckable;
    private final ISFXPlayable sfxPlayable;
    private final ISceneSwitchable sceneSwitchable;
    private final QuestionManager questionManager;
    private final FontManager fontManager;
    private final IMovementCalculatable movementCalculatable;
    private final IScenerySelect scenerySelect;

    public GameScene(IEntityRegisterable entityRegisterable,
            IUIDisplayable uiDisplayable,
            ICollidableRegisterable collidableRegisterable,
            IRenderRegisterable renderRegisterable,
            IMusicPlayable musicPlayable,
            IInputKeyCheckable inputKeyCheckable,
            ISFXPlayable sfxPlayable,
            ISceneSwitchable sceneSwitchable,
            QuestionManager questionManager,
            FontManager fontManager,
            IMovementCalculatable movementCalculatable,
            IScenerySelect scenerySelect) {
			super("GameScene",
			     entityRegisterable,
			     uiDisplayable,
			     collidableRegisterable,
			     renderRegisterable,
			     musicPlayable);
			this.inputKeyCheckable = inputKeyCheckable;
			this.sfxPlayable = sfxPlayable;
			this.sceneSwitchable = sceneSwitchable;
			this.questionManager = questionManager;
			this.fontManager = fontManager;
            this.movementCalculatable = movementCalculatable;
            this.scenerySelect = scenerySelect;
			}

    @Override
    protected void loadEntities() {


        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float gateWidth = 150f;
        float gateHeight = 80f;
        float centerX = screenWidth / 2;
        float gap = 100f;
        Color roadsurroundingColor = new Color(0.2f, 0.5f, 0.2f, 1f); // default green

        String selectedScenery = scenerySelect.getSelectedScenery();

        if (selectedScenery.equals("City")) {
        	roadsurroundingColor = new Color(0.2f, 0.5f, 0.2f, 1f); 
        }
        if (selectedScenery.equals("Desert")) {
        	roadsurroundingColor = new Color(0.76f, 0.70f, 0.35f, 1f);
        }
       
    	RoadSurrounding roadsurroundingLeft= new RoadSurrounding("left",roadsurroundingColor );
        addEntity(roadsurroundingLeft);
        renderRegisterable.registerRenderable(roadsurroundingLeft);
        collidableRegisterable.registerCollidable(roadsurroundingLeft);

    	RoadSurrounding roadsurroundingRight = new RoadSurrounding("right",roadsurroundingColor);
        addEntity(roadsurroundingRight);

        renderRegisterable.registerRenderable(roadsurroundingRight);
        collidableRegisterable.registerCollidable(roadsurroundingRight);

        // scrolling road background
        RoadAsphalt roadasphalt = new RoadAsphalt();
        addEntity(roadasphalt);
        renderRegisterable.registerRenderable(roadasphalt);


        float dashCycle = 50f;

        for (float y = 0; y < screenHeight; y += dashCycle) {
            RoadDashes roaddashes = new RoadDashes(y);
            addEntity(roaddashes);
            renderRegisterable.registerRenderable(roaddashes);
        }

        // question text shown at the top of the screen
        questionDisplay = new QuestionDisplay(0, 690, questionManager, fontManager.getLargeFont());
        addUI(questionDisplay);

        //Lives sprites
        livesElement = new LivesElement(90, 60, fontManager.getMediumFont());
        addUI(livesElement);
        int totalQuestions = questionManager.getTotalQuestions();
        float livesQuestionsRatio = 0.65f;
        livesElement.setLivesCounter((int)(livesQuestionsRatio * totalQuestions));

        // player centered horizontally at the bottom
        Player player = new Player(screenWidth / 2 - 16, 80, sfxPlayable);
        addEntity(player);
        renderRegisterable.registerRenderable(player);
        collidableRegisterable.registerCollidable(player);

        // gate A on the left, gate B on the right, both positioned above center
        Gate gateA = new Gate(centerX - gateWidth - gap/2, screenHeight * 0.65f, gateWidth, gateHeight, "A", questionManager, sfxPlayable, livesElement);
        addEntity(gateA);
        renderRegisterable.registerRenderable(gateA);
        collidableRegisterable.registerCollidable(gateA);

        Gate gateB = new Gate(centerX + gap/2, screenHeight * 0.65f, gateWidth, gateHeight, "B", questionManager, sfxPlayable, livesElement);
        addEntity(gateB);
        renderRegisterable.registerRenderable(gateB);
        collidableRegisterable.registerCollidable(gateB);

        // link gates so both reset together when either is hit
        gateA.setPartner(gateB);
        gateB.setPartner(gateA);

        System.out.println("GameScene loaded");
    }

    @Override
    public void update() {
        super.update();

        // move to the end screen once all questions have been answered
        if (questionManager.isBankFinished()) {
            System.out.println("All questions done, going to EndScene...");
            sceneSwitchable.switchScene("EndScene");
        }

        // if no more lives -> delay -> end screen
        if (livesElement.getLivesCounter() <= 0) {
            if (!endStarted) {
                endStarted = true;
                endDelay = END_DELAY_DURATION;
            }

            endDelay -= Gdx.graphics.getDeltaTime();
            if (endDelay <= 0f) {
                endStarted = false;
                sceneSwitchable.switchScene("EndScene");
            }
        }

        Array<Entity> entities = entityRegisterable.getEntities();
        //enable movement
        for (Entity entity : entities) {
            if (entity instanceof Gate) {
                movementCalculatable.move(entity, 0);
            }

            if (entity instanceof RoadDashes) {
            	movementCalculatable.move(entity, 0);
            }
        }
    }
}
