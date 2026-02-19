package io.github.inf1009_p10_9;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.inf1009_p10_9.managers.*;
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
        
        // COMPOSITION ROOT - Creates ALL dependencies
        public GameApplication() {
            super(
                new SceneManager(),
                new EntityManager(),
                new CollisionManager(),
                new MovementManager(),
                new InputManager(),
                new OutputManager()
            );
        }
        
        @Override
        public void create() {
            super.create();
            
            // Get managers from parent (already injected)
            SceneManager sceneMgr = getSceneManager();
            EntityManager entityMgr = getEntityManager();
            CollisionManager collisionMgr = getCollisionManager();
            MovementManager moveMgr = getMovementManager();
            InputManager inputMgr = getInputManager();
            OutputManager outMgr = getOutputManager();
            
            // Create Keyboard with interfaces
            Keyboard keyboard = new Keyboard(
                moveMgr,      // as IMovementCalculatable
                inputMgr,     // as IInputKeyCheckable
                entityMgr     // as IEntityQueryable
            );
            inputMgr.registerPeripheral(keyboard);
            
            // Set up collision detection
            AABBCollisionStrategy collisionStrategy = new AABBCollisionStrategy();
            collisionMgr.setCollisionStrategy(collisionStrategy);
            
            // Register movement strategies
            UserMovement userMovement = new UserMovement(250f);
            moveMgr.registerMovementStrategy("Player", userMovement);
            
            AIMovement enemyMovement = new AIMovement(200f, AIMovement.AIMovementPattern.FLEE);
            moveMgr.registerMovementStrategy("Enemy", enemyMovement);
            
            // Create scenes with interfaces (simpler now!)
            sceneMgr.addScene(new StartScene(
                sceneMgr,           // as ISceneSwitchable
                entityMgr,          // as IEntityRegisterable
                outMgr,             // as IUIDisplayable
                inputMgr,           // as IInputKeyCheckable
                collisionMgr,       // as ICollidableRegisterable (has both register & unregister)
                outMgr,             // as IRenderRegisterable (has both register & unregister)
                outMgr.getBGManager()   // as IMusicPlayable
            ));
            
            sceneMgr.addScene(new MidScene(
                sceneMgr,           // as ISceneSwitchable
                entityMgr,          // as IEntityRegisterable
                outMgr,             // as IUIDisplayable
                moveMgr,            // as IMovementCalculatable
                moveMgr,            // as IMovementStrategyRegisterable (has both register & get)
                inputMgr,           // as IInputKeyCheckable
                collisionMgr,       // as ICollidableRegisterable
                outMgr.getSFXManager(), // as ISFXPlayable
                outMgr,             // as IRenderRegisterable
                outMgr.getBGManager()   // as IMusicPlayable
            ));
            
            sceneMgr.addScene(new EndScene(
                sceneMgr,           // as ISceneSwitchable
                entityMgr,          // as IEntityRegisterable
                outMgr,             // as IUIDisplayable
                inputMgr,           // as IInputKeyCheckable
                collisionMgr,       // as ICollidableRegisterable
                outMgr,             // as IRenderRegisterable
                outMgr.getBGManager()   // as IMusicPlayable
            ));
            
            // Start with first scene
            sceneMgr.switchScene("StartScene");
            
            System.out.println("Game Engine started successfully!");
        }
    }
}
