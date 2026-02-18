package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.interfaces.IMusicPlayable;
import io.github.inf1009_p10_9.interfaces.ICollidableRegisterable;
import io.github.inf1009_p10_9.interfaces.IRenderRegisterable;
import io.github.inf1009_p10_9.ui.TextLabel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class EndScene extends Scene {
    private TextLabel titleLabel;
    private TextLabel instructionLabel1;
    private TextLabel instructionLabel2;
    private boolean spacePressed = false;
    private float sceneLoadTime = 0;
    private IInputKeyCheckable inputKeyCheckable;

    public EndScene(IInputKeyCheckable inputKeyCheckable,
                    ICollidableRegisterable collidableRegisterable,
                    IRenderRegisterable renderRegisterable,
                    IMusicPlayable musicPlayable) {
        super("EndScene", collidableRegisterable, renderRegisterable, musicPlayable);
        this.inputKeyCheckable = inputKeyCheckable;
    }

    @Override
    protected void loadEntities() {
        // Create scene title
        titleLabel = new TextLabel("END SCENE", 330, 400);
        titleLabel.setColor(Color.MAGENTA);
        addUI(titleLabel);

        // Create instruction text
        instructionLabel1 = new TextLabel("Press SPACE to restart (Mid Scene)", 180, 250);
        instructionLabel1.setColor(Color.YELLOW);
        addUI(instructionLabel1);

        instructionLabel2 = new TextLabel("Press ESC for Start Scene", 240, 200);
        instructionLabel2.setColor(Color.WHITE);
        addUI(instructionLabel2);

        System.out.println("EndScene loaded");
    }

    @Override
    public void load() {
        super.load();
        sceneLoadTime = 0;
    }


    @Override
    public void update() {
        super.update();

        sceneLoadTime += Gdx.graphics.getDeltaTime();

        // Only accept input after 0.2 seconds
        if (sceneLoadTime < 0.2f) {
            return;
        }

        // Press SPACE to go to mid scene
        if (inputKeyCheckable.isKeyPressed(Keys.SPACE)) {
            if (!spacePressed) {
                spacePressed = true;
                System.out.println("Restarting - Going to MidScene...");
                GameContext.getSceneManager().switchScene("MidScene");
            }
        } else {
            spacePressed = false;
        }

        // Press ESC to go to start scene
        if (inputKeyCheckable.isKeyPressed(Keys.ESCAPE)) {
            System.out.println("Going back to StartScene...");
            GameContext.getSceneManager().switchScene("StartScene");
        }

        // Make instruction text blink
        instructionLabel1.setVisible((System.currentTimeMillis() / 500) % 2 == 0);
    }
}
