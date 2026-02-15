package io.github.inf1009_p10_9;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.inf1009_p10_9.movement.*;
import io.github.inf1009_p10_9.input.*;
import io.github.inf1009_p10_9.scenes.*;
import io.github.inf1009_p10_9.collision.*;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new GameApplication(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("My LibGDX Game Engine");
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);
        configuration.setWindowedMode(800, 600);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
    
    static class GameApplication extends GameMaster {
        
        @Override
        public void create() {
            super.create();
            
            // Set up input handling
            Keyboard keyboard = new Keyboard();
            GameContext.getInputManager().registerPeripheral(keyboard);
            
            // Set up collision detection
            AABBCollisionStrategy collisionStrategy = new AABBCollisionStrategy();
            GameContext.getCollisionManager().setCollisionStrategy(collisionStrategy);
            
            // Register movement strategies that the game can use
            UserMovement userMovement = new UserMovement(250f);
            GameContext.getMovementManager().registerMovementStrategy("Player", userMovement);
            
            AIMovement enemyMovement = new AIMovement(200f, AIMovement.MovementPattern.FLEE); // AI has 4 movement pattern to choose from RANDOM,PATROL,CHASE,FLEE
            GameContext.getMovementManager().registerMovementStrategy("Enemy", enemyMovement);
            
            
            // Add all game scenes
            GameContext.getSceneManager().addScene(new StartScene());
            GameContext.getSceneManager().addScene(new MidScene()); // Gameplay scene, edit MidScene with the entities needed to your preference
            GameContext.getSceneManager().addScene(new EndScene());
            
            // Start with the first scene
            GameContext.getSceneManager().switchScene("StartScene");
            
            System.out.println("Game Engine started successfully!");
        }
    }
}