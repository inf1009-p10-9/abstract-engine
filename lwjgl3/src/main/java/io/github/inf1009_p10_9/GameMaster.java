package io.github.inf1009_p10_9;

import com.badlogic.gdx.ApplicationAdapter;
import io.github.inf1009_p10_9.managers.*;

public class GameMaster extends ApplicationAdapter {
    
    // Managers (injected via constructor)
    private final SceneManager sceneManager;
    private final EntityManager entityManager;
    private final CollisionManager collisionManager;
    private final MovementManager movementManager;
    private final InputManager inputManager;
    private final OutputManager outputManager;
    
    // Game loop control
    private boolean running;
    
    // Constructor Injection (Dependency Injection)
    public GameMaster(SceneManager sceneManager,
                      EntityManager entityManager,
                      CollisionManager collisionManager,
                      MovementManager movementManager,
                      InputManager inputManager,
                      OutputManager outputManager) {
        this.sceneManager = sceneManager;
        this.entityManager = entityManager;
        this.collisionManager = collisionManager;
        this.movementManager = movementManager;
        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }
    
    @Override
    public void create() {
        initialize();
    }
    
    public void initialize() {
        // Initialize all managers (they're already created)
        sceneManager.initialize();
        entityManager.initialize();
        collisionManager.initialize();
        movementManager.initialize();
        inputManager.initialize();
        outputManager.initialize();
        
        running = true;
        
        System.out.println("GameMaster initialized");
    }

    @Override
    public void render() {
        if (!running) {
            return;
        }

        gameLoop();
    }

    public void gameLoop() {
        // Input processing
        inputManager.update();

        // Scene update
        sceneManager.update();

        // Entity updates
        entityManager.update();

        // Movement processing
        //movementManager.update();

        // Collision detection
        collisionManager.update();

        // Rendering
        outputManager.render();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void dispose() {
        // Clean up all managers
        entityManager.clear();
        collisionManager.clear();
        movementManager.clear();
        inputManager.clear();
        outputManager.clear();

        System.out.println("GameMaster disposed");
    }

    // Getters for managers
    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public MovementManager getMovementManager() {
        return movementManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public OutputManager getOutputManager() {
        return outputManager;
    }
}
