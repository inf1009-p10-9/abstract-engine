package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.entities.Gate;
import io.github.inf1009_p10_9.entities.Player;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.QuestionDisplay;

public class GameScene extends Scene {
    private QuestionDisplay questionDisplay;

    private IInputKeyCheckable inputKeyCheckable;
    private ICollidableRegisterable collidableRegisterable;
    private ISFXPlayable sfxPlayable;
    private IRenderRegisterable renderRegisterable;

    public GameScene(IInputKeyCheckable inputKeyCheckable,
                     ICollidableUnregisterable collidableUnregisterable,
                     ICollidableRegisterable collidableRegisterable,
                     ISFXPlayable sfxPlayable,
                     IRenderUnregisterable renderUnregisterable,
                     IRenderRegisterable renderRegisterable,
                     IMusicPlayable musicPlayable) {
        super("GameScene", collidableUnregisterable, renderUnregisterable, musicPlayable);
        this.inputKeyCheckable = inputKeyCheckable;
        this.collidableRegisterable = collidableRegisterable;
        this.sfxPlayable = sfxPlayable;
        this.renderRegisterable = renderRegisterable;
    }

    @Override
    protected void loadEntities() {
        // question display at top of screen
        questionDisplay = new QuestionDisplay(200, 570);
        addUI(questionDisplay);

        // player in center bottom
        Player player = new Player(384, 100, sfxPlayable);
        addEntity(player);
        renderRegisterable.registerRenderable(player);
        collidableRegisterable.registerCollidable(player);

        // gate A on left lane
        Gate gateA = new Gate(150, 400, 100, 60, "A");
        addEntity(gateA);
        renderRegisterable.registerRenderable(gateA);
        collidableRegisterable.registerCollidable(gateA);

        // gate B on right lane
        Gate gateB = new Gate(550, 400, 100, 60, "B");
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
        if (GameContext.getQuestionManager().isBankFinished()) {
            System.out.println("All questions done, going to EndScene...");
            GameContext.getSceneManager().switchScene("EndScene");
        }
    }
}