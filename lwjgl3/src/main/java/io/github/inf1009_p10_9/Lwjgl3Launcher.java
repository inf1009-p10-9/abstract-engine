package io.github.inf1009_p10_9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.inf1009_p10_9.interfaces.IMovementStrategyRegisterable;
import io.github.inf1009_p10_9.managers.CollisionManager;
import io.github.inf1009_p10_9.managers.InputManager;
import io.github.inf1009_p10_9.managers.MovementManager;
import io.github.inf1009_p10_9.managers.OutputManager;
import io.github.inf1009_p10_9.managers.SceneManager;
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

            MovementManager moveMgr = GameContext.getMovementManager();
            InputManager inputMgr = GameContext.getInputManager();
            CollisionManager collisionMgr = GameContext.getCollisionManager();
            OutputManager outMgr = GameContext.getOutputManager();
            SceneManager sceneManager = GameContext.getSceneManager();

            // Set up input handling
            inputMgr.registerPeripheral(new Keyboard(moveMgr, inputMgr));

            // Set up collision detection
            collisionMgr.setCollisionStrategy(new AABBCollisionStrategy());

            // Register movement strategies that the game can use
            moveMgr.registerMovementStrategy("Player",
                                             new UserMovement(250f));

            moveMgr.registerMovementStrategy("Enemy",
                                             // Demo
                                             new AIMovement(200f, AIMovement.AIMovementPattern.FLEE));

            // Add all game scenes
            Scene[] scenes = {
                new StartScene(inputMgr,
                               collisionMgr,
                               outMgr,
                               outMgr.getBGManager()),
                new MidScene(moveMgr,
                             moveMgr,
                             inputMgr,
                             collisionMgr,
                             outMgr.getSFXManager(),
                             outMgr,
                             outMgr.getBGManager()),
                new EndScene(inputMgr,
                             collisionMgr,
                             outMgr,
                             outMgr.getBGManager())
            };

            for (Scene scene : scenes) {
                sceneManager.addScene(scene);
            }

            // Start with first scene
            sceneManager.switchScene("StartScene");

            System.out.println("Game Engine started successfully!");
        }
    }
}
