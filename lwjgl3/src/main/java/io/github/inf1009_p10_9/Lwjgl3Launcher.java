package io.github.inf1009_p10_9;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.inf1009_p10_9.interfaces.IMovementStrategyRegisterable;
import io.github.inf1009_p10_9.managers.CollisionManager;
import io.github.inf1009_p10_9.managers.InputManager;
import io.github.inf1009_p10_9.managers.MovementManager;
import io.github.inf1009_p10_9.managers.OutputManager;
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

            // Set up input handling
            Keyboard keyboard = new Keyboard(moveMgr, inputMgr);
            GameContext.getInputManager().registerPeripheral(keyboard);

            // Set up collision detection
            AABBCollisionStrategy collisionStrategy = new AABBCollisionStrategy();
            GameContext.getCollisionManager().setCollisionStrategy(collisionStrategy);

            // Register movement strategies that the game can use
            IMovementStrategyRegisterable movementStrategyRegisterable = GameContext.getMovementManager(); //
            UserMovement userMovement = new UserMovement(250f);
            movementStrategyRegisterable.registerMovementStrategy("Player", userMovement);

            AIMovement enemyMovement = new AIMovement(200f, AIMovement.AIMovementPattern.FLEE); // Demo
            movementStrategyRegisterable.registerMovementStrategy("Enemy", enemyMovement);


            // Add all game scenes
            GameContext.getSceneManager().addScene(new StartScene(inputMgr, collisionMgr, outMgr, outMgr.getBGManager()));
            GameContext.getSceneManager().addScene(new MidScene(moveMgr, moveMgr, inputMgr, collisionMgr,
                collisionMgr, outMgr.getSFXManager(), outMgr, outMgr, outMgr.getBGManager())); //gameplay scene
            GameContext.getSceneManager().addScene(new EndScene(inputMgr, collisionMgr, outMgr, outMgr.getBGManager()));

            // Start with first scene
            GameContext.getSceneManager().switchScene("StartScene");

            System.out.println("Game Engine started successfully!");
        }
    }
}
