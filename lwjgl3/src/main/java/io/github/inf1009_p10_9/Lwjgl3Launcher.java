package io.github.inf1009_p10_9;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.inf1009_p10_9.managers.*;
import io.github.inf1009_p10_9.movement.*;
import io.github.inf1009_p10_9.input.*;
import io.github.inf1009_p10_9.scenes.*;
import io.github.inf1009_p10_9.collision.*;
import io.github.inf1009_p10_9.questions.QuestionManager;
import io.github.inf1009_p10_9.ui.FontManager;

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
        configuration.setWindowedMode(1280, 720);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }

    static class GameApplication extends GameMaster {
        private final QuestionManager questionManager;
        private final FontManager fontManager;

        // COMPOSITION ROOT - Creates ALL dependencies
        public GameApplication() {
            super(
                  SceneManager.getInstance(),
                  EntityManager.getInstance(),
                  CollisionManager.getInstance(),
                  MovementManager.getInstance(),
                  InputManager.getInstance(),
                  OutputManager.getInstance());

            this.questionManager = QuestionManager.getInstance();
            this.managersMinimal.add(this.questionManager);
            this.fontManager = FontManager.getInstance();
            this.managersMinimal.add(this.fontManager);
        }

        @Override
        public void create() {
            super.create();
            // Create Keyboard with interfaces
            Keyboard keyboard = new Keyboard(
                movementManager,      // as IMovementCalculatable
                inputManager,     // as IInputKeyCheckable
                entityManager     // as IEntityQueryable
            );
            inputManager.registerPeripheral(keyboard);

            // Set up collision detection
            AABBCollisionStrategy collisionStrategy = new AABBCollisionStrategy();
            collisionManager.setCollisionStrategy(collisionStrategy);

            // Register movement strategies
            movementManager.registerMovementStrategy("Player",
                                                     new UserMovement(250f));
            movementManager.registerMovementStrategy("Enemy",
                                                     new AIMovement(200f, AIMovement.AIMovementPattern.FLEE));

            // Create scenes with interfaces
            sceneManager.addScene(new StartScene(
                entityManager,                  // as IEntityRegisterable
                outputManager,                  // as IUIDisplayable
                collisionManager,               // as ICollidableRegisterable
                outputManager,                  // as IRenderRegisterable
                outputManager.getBGManager(),   // as IMusicPlayable

                inputManager,                   // as IInputKeyCheckable
                sceneManager,                    // as ISceneSwitchable

                fontManager
            ));

            sceneManager.addScene(new MidScene(
                entityManager,                  // as IEntityRegisterable
                outputManager,                  // as IUIDisplayable
                collisionManager,               // as ICollidableRegisterable
                outputManager,                  // as IRenderRegisterable
                outputManager.getBGManager(),   // as IMusicPlayable

                inputManager,                   // as IInputKeyCheckable
                movementManager,                // as IMovementCalculatable
                movementManager,                // as IMovementStrategyRegisterable
                sceneManager,                   // as ISceneSwitchable
                outputManager.getSFXManager()   // as ISFXPlayable
            ));

            sceneManager.addScene(new SettingsScene(
            	    entityManager,
            	    outputManager,
            	    collisionManager,
            	    outputManager,
            	    outputManager.getBGManager(),
            	    outputManager.getSFXManager(),
            	    inputManager,                 // IInputKeyCheckable
            	    inputManager,                 // IKeyPressConsumable
            	    sceneManager,
            	    SettingsManager.getInstance(),
            	    fontManager
            	));

            sceneManager.addScene(new EndScene(
                entityManager,                  // as IEntityRegisterable
                outputManager,                  // as IUIDisplayable
                collisionManager,               // as ICollidableRegisterable
                outputManager,                  // as IRenderRegisterable
                outputManager.getBGManager(),   // as IMusicPlayable

                inputManager,                   // as IInputKeyCheckable
                sceneManager,                   // as ISceneSwitchable
                questionManager,                 // as QuestionManager (#TODO: Create interface)
                fontManager
            ));

            sceneManager.addScene(new GameScene(
                entityManager,                  // as IEntityRegisterable
                outputManager,                  // as IUIDisplayable
                collisionManager,               // as ICollidableRegisterable
                outputManager,                  // as IRenderRegisterable
                outputManager.getBGManager(),   // as IMusicPlayable

                inputManager,                   // as IInputKeyCheckable
                outputManager.getSFXManager(),  // as ISFXPlayable
                sceneManager,                   // as ISceneSwitchable
                questionManager,                 // as QuestionManager (#TODO: Create interface)
                fontManager
            ));

            sceneManager.addScene(new SubjectSelectScene(
                entityManager,                  // as IEntityRegisterable
                outputManager,                  // as IUIDisplayable
                collisionManager,               // as ICollidableRegisterable
                outputManager,                  // as IRenderRegisterable
                outputManager.getBGManager(),   // as IMusicPlayable

                inputManager,                   // as IInputKeyCheckable
                sceneManager,                   // as ISceneSwitchable
                questionManager,                 // as QuestionManager (#TODO: Create interface)
                fontManager
            ));

            // Start with first scene
            sceneManager.switchScene("StartScene");

            System.out.println("Game Engine started successfully!");
        }
    }
}
