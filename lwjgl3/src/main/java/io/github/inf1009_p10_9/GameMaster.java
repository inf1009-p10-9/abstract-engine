package io.github.inf1009_p10_9;

import com.badlogic.gdx.ApplicationAdapter;
import io.github.inf1009_p10_9.managers.*;

public class GameMaster extends ApplicationAdapter {
    private static GameMaster instance;

    // Managers
    private SceneManager sceneManager;
    private EntityManager entityManager;
    private CollisionManager collisionManager;
    private MovementManager movementManager;
    private InputManager inputManager;
    private OutputManager outputManager;

    // Game loop control
    private boolean running;

    protected GameMaster() {
        // Private constructor for singleton
    }

    public static GameMaster getInstance() {
        if (instance == null) {
            instance = new GameMaster();
        }
        return instance;
    }

    @Override
    public void create() {
        initialize();
    }

    public void initialize() {
        // Initialize all managers
        sceneManager = SceneManager.getInstance();
        entityManager = EntityManager.getInstance();
        collisionManager = CollisionManager.getInstance();
        movementManager = MovementManager.getInstance();
        inputManager = InputManager.getInstance();
        outputManager = OutputManager.getInstance();

        sceneManager.initialize();
        entityManager.initialize();
        collisionManager.initialize();
        movementManager.initialize();
        inputManager.initialize();
        outputManager.initialize();

        // Register context
        GameContext.setGameMaster(this);

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
