package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.ui.TextLabel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input.Keys;


public class StartScene extends Scene {
    private TextLabel titleLabel;
    private TextLabel instructionLabel;
    private boolean spacePressed = false;
    private float sceneLoadTime = 0;
    
    public StartScene() {
        super("StartScene");
    }
    
    @Override
    protected void loadEntities() {
        // Create scene title
        titleLabel = new TextLabel("START SCENE", 320, 400);
        titleLabel.setColor(Color.GREEN);
        addUI(titleLabel);
        
        // Create instruction text
        instructionLabel = new TextLabel("Press SPACE to go to Mid Scene", 220, 250);
        instructionLabel.setColor(Color.YELLOW);
        addUI(instructionLabel);
        
        System.out.println("StartScene loaded");
    }
    
    
    @Override
    public void load() {
        super.load();
        sceneLoadTime = 0;
        System.out.println("Attempting to play music...");
        GameContext.getOutputManager().getBGManager().playMusic("music/Super Mario Bros. medley.mp3");
        System.out.println("Music command sent!");


    }
    
    @Override
    public void update() {
        super.update();
        
        sceneLoadTime += com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        
        // Only accept input after 0.2 seconds
        if (sceneLoadTime < 0.2f) {
            return;
        }
        
        // Check if SPACE is pressed to go to mid scene
        if (GameContext.getInputManager().isKeyPressed(Keys.SPACE)) {
            if (!spacePressed) {
                spacePressed = true;
                System.out.println("Going to MidScene...");
                GameContext.getSceneManager().switchScene("MidScene");
            }
        } else {
            spacePressed = false;
        }
        
        // Make instruction text blink
        instructionLabel.setVisible((System.currentTimeMillis() / 500) % 2 == 0);
    }
}