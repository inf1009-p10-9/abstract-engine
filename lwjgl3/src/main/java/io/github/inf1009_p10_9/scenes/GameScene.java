package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.entities.Gate;
import io.github.inf1009_p10_9.entities.Road;
import io.github.inf1009_p10_9.entities.Player;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.questions.QuestionManager;
import io.github.inf1009_p10_9.ui.FontManager;
import io.github.inf1009_p10_9.ui.QuestionDisplay;
import com.badlogic.gdx.Gdx;

public class GameScene extends Scene {
    private QuestionDisplay questionDisplay;

    private final IInputKeyCheckable inputKeyCheckable;
    private final ISFXPlayable sfxPlayable;
    private final ISceneSwitchable sceneSwitchable;
    private final QuestionManager questionManager;
    private final FontManager fontManager;

    public GameScene(IEntityRegisterable entityRegisterable,
            IUIDisplayable uiDisplayable,
            ICollidableRegisterable collidableRegisterable,
            IRenderRegisterable renderRegisterable,
            IMusicPlayable musicPlayable,
            IInputKeyCheckable inputKeyCheckable,
            ISFXPlayable sfxPlayable,
            ISceneSwitchable sceneSwitchable,
            QuestionManager questionManager,
            FontManager fontManager) {
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
			}

    @Override
    protected void loadEntities() {
    	
    	Road road = new Road();
        addEntity(road);
        renderRegisterable.registerRenderable(road);
        
        questionDisplay = new QuestionDisplay(0, 690, questionManager, fontManager.getLargeFont());
        addUI(questionDisplay);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float laneLeft = screenWidth * 0.25f;
        float laneRight = screenWidth * 0.70f;
        float gateWidth = 150f;
        float gateHeight = 80f;
        float centerX = screenWidth / 2;
        float gap = 100f; 
        // player centered horizontally at bottom
        Player player = new Player(screenWidth / 2 - 16, 80, sfxPlayable);
        addEntity(player);
        renderRegisterable.registerRenderable(player);
        collidableRegisterable.registerCollidable(player);

        // gate A on left lane, centered on lane
        Gate gateA = new Gate(centerX - gateWidth - gap/2, screenHeight * 0.65f, gateWidth, gateHeight, "A", questionManager);
        addEntity(gateA);
        renderRegisterable.registerRenderable(gateA);
        collidableRegisterable.registerCollidable(gateA);

        // gate B on right lane, centered on lane
        Gate gateB = new Gate(centerX + gap/2, screenHeight * 0.65f, gateWidth, gateHeight, "B", questionManager);
        addEntity(gateB);
        renderRegisterable.registerRenderable(gateB);
        collidableRegisterable.registerCollidable(gateB);

        // link gates so both reset together on collision
        gateA.setPartner(gateB);
        gateB.setPartner(gateA);

        System.out.println("GameScene loaded");
    }

    @Override
    public void update() {
        super.update();

        // check if all questions in the bank are done
        if (questionManager.isBankFinished()) {
            System.out.println("All questions done, going to EndScene...");
            sceneSwitchable.switchScene("EndScene");
        }
    }
}
