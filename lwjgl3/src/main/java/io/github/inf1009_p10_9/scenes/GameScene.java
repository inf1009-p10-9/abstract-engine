package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.entities.Gate;
import io.github.inf1009_p10_9.entities.Player;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.QuestionDisplay;
import com.badlogic.gdx.Gdx;

public class GameScene extends Scene {

    private QuestionDisplay questionDisplay;
    private ISFXPlayable sfxPlayable;
    private ISceneSwitchable sceneSwitchable;

    public GameScene(IEntityRegisterable entityRegisterable,
                     IUIDisplayable uiDisplayable,
                     ICollidableRegisterable collidableRegisterable,
                     IRenderRegisterable renderRegisterable,
                     IMusicPlayable musicPlayable,
                     ISFXPlayable sfxPlayable,
                     ISceneSwitchable sceneSwitchable) {
        super("GameScene",
              entityRegisterable,
              uiDisplayable,
              collidableRegisterable,
              renderRegisterable,
              musicPlayable);
        this.sfxPlayable = sfxPlayable;
        this.sceneSwitchable = sceneSwitchable;
    }

    @Override
    protected void loadEntities() {
        questionDisplay = new QuestionDisplay(0, 690);
        addUI(questionDisplay);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float laneLeft = screenWidth * 0.25f;
        float laneRight = screenWidth * 0.70f;
        float gateWidth = 150f;
        float gateHeight = 80f;

        // player centered horizontally at bottom
        Player player = new Player(screenWidth / 2 - 16, 80, sfxPlayable);
        addEntity(player);
        renderRegisterable.registerRenderable(player);
        collidableRegisterable.registerCollidable(player);

        // gate A on left lane, centered on lane
        Gate gateA = new Gate(laneLeft - gateWidth / 2, screenHeight * 0.65f, gateWidth, gateHeight, "A");
        addEntity(gateA);
        renderRegisterable.registerRenderable(gateA);
        collidableRegisterable.registerCollidable(gateA);

        // gate B on right lane, centered on lane
        Gate gateB = new Gate(laneRight - gateWidth / 2, screenHeight * 0.65f, gateWidth, gateHeight, "B");
        addEntity(gateB);
        renderRegisterable.registerRenderable(gateB);
        collidableRegisterable.registerCollidable(gateB);

        gateA.setPartner(gateB);
        gateB.setPartner(gateA);

        System.out.println("GameScene loaded");
    }

    @Override
    public void update() {
        super.update();

        if (GameContext.getQuestionManager().isBankFinished()) {
            System.out.println("All questions done, going to EndScene...");
            sceneSwitchable.switchScene("EndScene");
        }
    }
}