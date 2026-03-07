package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.entities.Gate;
import io.github.inf1009_p10_9.entities.Player;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.QuestionDisplay;

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
        questionDisplay = new QuestionDisplay(200, 570);
        addUI(questionDisplay);

        Player player = new Player(384, 100, sfxPlayable);
        addEntity(player);
        renderRegisterable.registerRenderable(player);
        collidableRegisterable.registerCollidable(player);

        Gate gateA = new Gate(150, 400, 100, 60, "A");
        addEntity(gateA);
        renderRegisterable.registerRenderable(gateA);
        collidableRegisterable.registerCollidable(gateA);

        Gate gateB = new Gate(550, 400, 100, 60, "B");
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