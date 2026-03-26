package io.github.inf1009_p10_9;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;


import io.github.inf1009_p10_9.game.entities.*;
import io.github.inf1009_p10_9.game.managers.QuestionManager;
import io.github.inf1009_p10_9.game.managers.SettingsManager;
import io.github.inf1009_p10_9.engine.managers.*;
import io.github.inf1009_p10_9.engine.movement.ScrollerMovement;
import io.github.inf1009_p10_9.engine.movement.UserMovement;
import io.github.inf1009_p10_9.engine.input.*;
import io.github.inf1009_p10_9.game.scenes.*;
import io.github.inf1009_p10_9.engine.collision.*;
import io.github.inf1009_p10_9.game.ui.FontLoader;


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
        private final FontLoader fontManager;
        private final PlayerState playerState;
        private final SettingsManager settingsManager;


        // creates all managers and wires everything together
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
            this.fontManager = FontLoader.getInstance();
            this.managersMinimal.add(this.fontManager);
            this.playerState = PlayerState.getInstance();
            this.settingsManager = SettingsManager.getInstance();

        }

        @Override
        public void create() {
            super.create();
            // Create Keyboard with interfaces
            Keyboard keyboard = new Keyboard(
                movementManager,      // as IMovementCalculatable
                inputManager,     // as IInputKeyCheckable
                entityManager,     // as IEntityQueryable
                settingsManager   // as ISettingsKBRetrievable
            );
            inputManager.registerPeripheral(keyboard);

            // Set up collision detection
            AABBCollisionStrategy collisionStrategy = new AABBCollisionStrategy();
            collisionManager.setCollisionStrategy(collisionStrategy);

            // Register movement strategies
            float gameWidth  = Gdx.graphics.getWidth();
            float laneWidth = 200f;

            float playerWidth = 52f;
            float LeftEdge  = gameWidth * 0.3f  - laneWidth / 2;
            float RightEdge = gameWidth * 0.70f + laneWidth / 2 - playerWidth;

            //UserMovement parameters : float speed, float leftBoundary, float rightBoundary, boolean enableVerticalMovement
            movementManager.registerMovementStrategy(Player.class, new UserMovement(250f, LeftEdge, RightEdge, false));

            movementManager.registerMovementStrategy(Building.class, new ScrollerMovement());
            movementManager.registerMovementStrategy(Cactus.class, new ScrollerMovement());
            movementManager.registerMovementStrategy(Rock.class, new ScrollerMovement());
            movementManager.registerMovementStrategy(Tree.class, new ScrollerMovement());
            movementManager.registerMovementStrategy(StreetLamp.class, new ScrollerMovement());
            movementManager.registerMovementStrategy(Bones.class, new ScrollerMovement());
            movementManager.registerMovementStrategy(Gate.class, new ScrollerMovement());
            movementManager.registerMovementStrategy(RoadDashes.class, new ScrollerMovement());

            // Create scenes with interfaces
            sceneManager.addScene(new StartScene(
                entityManager,                  // as IEntityRegisterable
                outputManager,                  // as IUIDisplayable
                collisionManager,               // as ICollidableRegisterable
                outputManager,                  // as IRenderRegisterable
                outputManager.getBGManager(),   // as IMusicPlayable

                inputManager,                   // as IInputKeyCheckable
                sceneManager,                    // as ISceneSwitchable

                fontManager,
                settingsManager                 // as ISettingsKBRetrievable
            ));

            sceneManager.addScene(new MapSelectScene(
                entityManager,                  // as IEntityRegisterable
                outputManager,                  // as IUIDisplayable
                collisionManager,               // as ICollidableRegisterable
                outputManager,                  // as IRenderRegisterable
                outputManager.getBGManager(),   // as IMusicPlayable

                inputManager,                   // as IInputKeyCheckable
                sceneManager,                   // as ISceneSwitchable

                fontManager,
                settingsManager
            ));

            sceneManager.addScene(new SettingsScene(
            	    entityManager,                // as IEntityRegisterable
            	    outputManager,                // as IUIDisplayable
            	    collisionManager,             // as ICollidableRegisterable
            	    outputManager,                // as IRenderRegisterable
            	    outputManager.getBGManager(), // as IMusicPlayable
            	    outputManager.getSFXManager(),// as ISFXPlayable
            	    inputManager,                 // IInputKeyCheckable
            	    inputManager,                 // IKeyPressConsumable
            	    sceneManager,                 // as ISceneSwitchable
            	    settingsManager,              // as ISettingsControllable
            	    fontManager,
                    settingsManager               // as ISettingsKBRetrievable
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
                fontManager,
                playerState,
                settingsManager                 // as ISettingsKBRetrievable
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
                fontManager,
                movementManager,                // as IMovementCalculatable
                movementManager,                // as IMovementStrategyRegisterable
                settingsManager                 // as IScenerySelect
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
            sceneManager.addScene(new CustomisationScene(
                entityManager,                  // as IEntityRegisterable
                outputManager,                  // as IUIDisplayable
                collisionManager,               // as ICollidableRegisterable
                outputManager,                  // as IRenderRegisterable
                outputManager.getBGManager(),   // as IMusicPlayable
                inputManager,                   // as IKeyBindObserverTarget
                sceneManager,                   // as ISceneSwitchable
                fontManager,                     // as FontManager
                playerState
            ));

            // kick off the game on the start screen
            sceneManager.switchScene("StartScene");

            System.out.println("Game Engine started successfully!");
        }
    }
}
