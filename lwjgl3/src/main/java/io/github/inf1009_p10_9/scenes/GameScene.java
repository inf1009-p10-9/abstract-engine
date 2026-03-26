package io.github.inf1009_p10_9.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import io.github.inf1009_p10_9.PlayerState;
import io.github.inf1009_p10_9.entities.*;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.questions.QuestionManager;
import io.github.inf1009_p10_9.ui.FontManager;
import io.github.inf1009_p10_9.ui.LivesElement;
import io.github.inf1009_p10_9.ui.QuestionDisplay;
import io.github.inf1009_p10_9.ui.TextLabel;
import io.github.inf1009_p10_9.ui.PauseOverlay;


// the main gameplay scene, sets up the road, player, gates, and question display + flags
public class GameScene extends Scene {
	private PauseOverlay pauseOverlay;
    private QuestionDisplay questionDisplay;
    private LivesElement livesElement;
    private float endDelay = 0f; // delay before going to endScene
    private boolean endStarted = false; // to check if going to endScene
    private boolean pauseState = false; // to check if game is set to pause
    private boolean escWasPressed = false; // to prevent spam Escape

    // dependencies
    private final IInputKeyCheckable inputKeyCheckable;
    private final ISFXPlayable sfxPlayable;
    private final ISceneSwitchable sceneSwitchable;
    private final QuestionManager questionManager;
    private final FontManager fontManager;
    private final IMovementCalculatable movementCalculatable;
    private final IScenerySelect scenerySelect;
    private final IMovementStrategyRetrievable movementStrategyRetrievable;

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
            IMovementStrategyRetrievable movementStrategyRetrievable,
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
            this.movementStrategyRetrievable = movementStrategyRetrievable;
            this.scenerySelect = scenerySelect;
			}

    @Override
    protected void loadEntities() {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float gateWidth = 350f;
        float gateHeight = 80f;
        float centerX = screenWidth / 2;
        float gap = 0f;

        float roadLeftEdge  = screenWidth * 0.3f - 100f;
        float roadRightEdge = screenWidth * 0.70f + 100f;

        // Map Loading
        Color roadsurroundingColor = new Color(0.2f, 0.5f, 0.2f, 1f); // default green
        String selectedScenery = scenerySelect.getSelectedScenery();

        if (selectedScenery.equals("City")) {
            roadsurroundingColor = new Color(0.2f, 0.5f, 0.2f, 1f);

            // dedicated X zones for left side
            float leftBuildingZone = roadLeftEdge * 0.1f;   // far left
            float leftTreeZone     = roadLeftEdge * 0.45f;  // middle left
            float leftLampX        = roadLeftEdge - 50f;    // right next to road

            // dedicated X zones for right side
            float rightBuildingZone = roadRightEdge + 180f;  // right next to road
            float rightTreeZone     = roadRightEdge + 120f; // further right
            float rightLampX        = roadRightEdge + 45f;  // right next to road

            // left buildings - fixed X zone, spaced Y
            float[] leftWidths  = {80f, 60f, 90f};
            float[] leftHeights = {200f, 150f, 180f};
            Color[] leftColors  = {
                new Color(0.55f, 0.55f, 0.60f, 1f),
                new Color(0.65f, 0.50f, 0.40f, 1f),

            };

            // right buildings - fixed X zone, spaced Y
            float[] rightWidths  = {80f, 70f, 85f};
            float[] rightHeights = {220f, 160f, 190f};
            Color[] rightColors  = {
                new Color(0.65f, 0.50f, 0.40f, 1f),
                new Color(0.45f, 0.50f, 0.60f, 1f)
            };

            float minBuildingGap = 300f;
            float minTreeGap = 250f;
            for (int i = 0; i < 2; i++) {
                float ly = i * minBuildingGap + (float)(Math.random() * minBuildingGap * 0.5f);
                Building buildingL = new Building(leftBuildingZone, ly, leftWidths[i], leftHeights[i], leftColors[i]);
                addEntity(buildingL);
                renderRegisterable.registerRenderable(buildingL);

                float ry = i * minBuildingGap + (float)(Math.random() * minBuildingGap * 0.5f);
                Building buildingR = new Building(rightBuildingZone, ry, rightWidths[i], rightHeights[i], rightColors[i]);
                addEntity(buildingR);
                renderRegisterable.registerRenderable(buildingR);
            }

            // left & right trees - fixed X zone, spaced Y
            for (int i = 0; i < 3; i++) {
                float ly = i * minTreeGap + (float)(Math.random() * 80f);
                Tree treeL = new Tree(leftTreeZone, ly);
                addEntity(treeL);
                renderRegisterable.registerRenderable(treeL);

                float ry = i * minTreeGap + (float)(Math.random() * 80f);
                Tree treeR = new Tree(rightTreeZone, ry);
                addEntity(treeR);
                renderRegisterable.registerRenderable(treeR);
            }

            // street lamps - fixed X, evenly spaced Y
            for (int i = 0; i < 3; i++) {
                float ly = 100f + i * 220f;
                StreetLamp lampL = new StreetLamp(leftLampX, ly, true);
                addEntity(lampL);
                renderRegisterable.registerRenderable(lampL);

                StreetLamp lampR = new StreetLamp(rightLampX, ly, false);
                addEntity(lampR);
                renderRegisterable.registerRenderable(lampR);
            }
        }

        if (selectedScenery.equals("Desert")) {
        	roadsurroundingColor = new Color(0.76f, 0.70f, 0.35f, 1f);
        	float minGap = 150f;

            for (int i = 0; i < 4; i++) {
                // left side
                float lx = (float)(Math.random() * (roadLeftEdge - 30f));
                float ly = i * minGap + (float)(Math.random() * minGap);
                Cactus cactusL = new Cactus(lx, ly);
                addEntity(cactusL);
                renderRegisterable.registerRenderable(cactusL);

                // right side
                float rx = roadRightEdge + 60f + (float)(Math.random() * (screenWidth - roadRightEdge - 60f));
                float ry = i * minGap + (float)(Math.random() * minGap);
                Cactus cactusR = (new Cactus(rx, ry));
                addEntity(cactusR);
                renderRegisterable.registerRenderable(cactusR);
            }

            for (int i = 0; i < 4; i++) {
                // left side
                float lx = (float)(Math.random() * (roadLeftEdge - 60f));
                float ly = (float)(Math.random() * screenHeight);
                float lsize = 20f + (float)(Math.random() * 25f); // random size between 20 and 45
                Rock rockL = new Rock(lx, ly, lsize);
                addEntity(rockL);
                renderRegisterable.registerRenderable(rockL);

                // right side
                float rx = roadRightEdge + (float)(Math.random() * (screenWidth - roadRightEdge - 60f));
                float ry = (float)(Math.random() * screenHeight);
                float rsize = 20f + (float)(Math.random() * 25f);
                Rock rockR = new Rock(rx, ry, rsize);
                addEntity(rockR);
                renderRegisterable.registerRenderable(rockR);
            }

            // bones - 3 on each side
            for (int i = 0; i < 3; i++) {
                float boneSize = 8f + (float)(Math.random() * 6f); // random size between 8 and 14

                // left side
                float lx = (float)(Math.random() * (roadLeftEdge - 60f));
                float ly = i * minGap + (float)(Math.random() * minGap);
                Bones boneL = new Bones(lx, ly, boneSize);
                addEntity(boneL);
                renderRegisterable.registerRenderable(boneL);

                // right side
                float rx = roadRightEdge + 60f + (float)(Math.random() * (screenWidth - roadRightEdge - 60f));
                float ry = i * minGap + (float)(Math.random() * minGap);
                Bones boneR = new Bones(rx, ry, boneSize);
                addEntity(boneR);
                renderRegisterable.registerRenderable(boneR);
            }
        }

        // Road loading

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

        //road dashes
        float dashCycle = 50f;
        for (float y = 0; y < screenHeight; y += dashCycle) {
            RoadDashes roaddashes = new RoadDashes(y);
            addEntity(roaddashes);
            renderRegisterable.registerRenderable(roaddashes);
        }

        // pause overlay
        pauseOverlay = new PauseOverlay(screenWidth, screenHeight, fontManager.getLargeFont());
        addUI(pauseOverlay);


        // question text shown at the top of the screen
        questionDisplay = new QuestionDisplay(0, 690, questionManager, fontManager.getLargeFont());
        addUI(questionDisplay);

        //Lives sprites
        livesElement = new LivesElement(90, 60, fontManager.getMediumFont());
        addUI(livesElement);
        int totalQuestions = questionManager.getTotalQuestions();
        float livesQuestionsRatio = 0.17f;
        livesElement.setLivesCounter((int)(livesQuestionsRatio * totalQuestions));


        GlyphLayout instrLayout = new GlyphLayout(fontManager.getSmallFont(), "ENTER: select    UP/DOWN: move    ESC: pause/resume");
        TextLabel instructionLabel = new TextLabel(
            "ENTER: select    UP/DOWN: move    ESC: pause/resume",
            centerX - instrLayout.width / 2f,
            Gdx.graphics.getHeight() * 0.05f,
            fontManager.getSmallFont()
        );
        instructionLabel.setColor(new Color(1f, 1f, 0.6f, 1f));
        addUI(instructionLabel);

        // player centered horizontally at the bottom
        PlayerState playerState = PlayerState.getInstance();
        Player player = new Player(screenWidth / 2 - 16, 80, playerState.getActivePlayerSkin().getTexturePath());
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

        // checks pause game input
        boolean EscKeyPressed = inputKeyCheckable.isKeyPressed(Keys.ESCAPE);
        if (EscKeyPressed && !escWasPressed) {
            pauseState = !pauseState;
        }

        if (pauseState) {
            boolean qPressed = inputKeyCheckable.isKeyPressed(Keys.Q);
            if (qPressed) {
                pauseState = false;
                sceneSwitchable.switchScene("StartScene");
            }
        }

        escWasPressed = EscKeyPressed; // prevent esc key spam, checks from previous game frame
        pauseOverlay.setVisible(pauseState);

        if (!pauseState) {
	        // move to the end screen once all questions have been answered
	        if (questionManager.isBankFinished()) {
	            System.out.println("All questions done, going to EndScene...");
	            sceneSwitchable.switchScene("EndScene");
	        }

	        // if no more lives -> delay -> end screen
	        if (livesElement.getLivesCounter() <= 0) {
	            if (!endStarted) {
	                endStarted = true;
                    // seconds
                    endDelay = 1.0f;
	            }

	            endDelay -= Gdx.graphics.getDeltaTime();
	            if (endDelay <= 0f) {
	                endStarted = false;
	                sceneSwitchable.switchScene("EndScene");
	            }
	        }

            // adjust scroll movement speed based on correct questions
            float currentScore = (float) questionManager.getScore();
            float targetSpeed = 90f + (currentScore * 10f);

            for (IMovementStrategy strategy : movementStrategyRetrievable.getAllStrategies()) {
                if (strategy instanceof IMovementScrollAdjustable) {
                    ((IMovementScrollAdjustable) strategy).adjustScrollSpeed(targetSpeed);
                }
            }

	        //enable movement for non-player entities
            Array<Entity> entities = entityRegisterable.getEntities();
	        for (Entity entity : entities) {
                if (!(entity instanceof Player)) {
                    movementCalculatable.move(entity, 0);
                }
	        }
        }
    }
}
