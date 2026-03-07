package io.github.inf1009_p10_9;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.ApplicationAdapter;

import io.github.inf1009_p10_9.interfaces.IManager;
import io.github.inf1009_p10_9.managers.*;

public class GameMaster extends ApplicationAdapter {

<<<<<<< HEAD
    // Managers
    private SceneManager sceneManager;
    private EntityManager entityManager;
    private CollisionManager collisionManager;
    private MovementManager movementManager;
    private InputManager inputManager;
    private OutputManager outputManager;
    private QuestionManager questionManager;
=======
    // Managers (injected via constructor)
    protected final SceneManager sceneManager;
    protected final EntityManager entityManager;
    protected final CollisionManager collisionManager;
    protected final MovementManager movementManager;
    protected final InputManager inputManager;
    protected final OutputManager outputManager;
    protected final List<IManager> managers;
>>>>>>> branch 'main' of https://github.com/inf1009-p10-9/abstract-engine

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
        this.managers = new ArrayList<IManager>();
        this.managers.addAll(
            Arrays.asList(
                inputManager,
                sceneManager,
                entityManager,
                collisionManager,
                outputManager));
    }

    @Override
    public void create() {
        initialize();
    }

    public void initialize() {
<<<<<<< HEAD
        // Initialize all managers
        sceneManager = SceneManager.getInstance();
        entityManager = EntityManager.getInstance();
        collisionManager = CollisionManager.getInstance();
        movementManager = MovementManager.getInstance();
        inputManager = InputManager.getInstance();
        outputManager = OutputManager.getInstance();
        questionManager = QuestionManager.getInstance();
        
        sceneManager.initialize();
        entityManager.initialize();
        collisionManager.initialize();
        movementManager.initialize();
        inputManager.initialize();
        outputManager.initialize();
        questionManager.initialize();

        // Register context
        GameContext.setGameMaster(this);

=======
        // Initialize all managers (they're already created)
        for (IManager manager : managers)
            manager.initialize();
>>>>>>> branch 'main' of https://github.com/inf1009-p10-9/abstract-engine
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
        for (IManager manager : managers) {
            manager.update();
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void dispose() {
        // Clean up all managers
<<<<<<< HEAD
        entityManager.clear();
        collisionManager.clear();
        movementManager.clear();
        inputManager.clear();
        outputManager.clear();
        questionManager.clear();
=======
        for (IManager manager : managers) {
            manager.clear();
        }
>>>>>>> branch 'main' of https://github.com/inf1009-p10-9/abstract-engine

        System.out.println("GameMaster disposed");
    }
<<<<<<< HEAD

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
    
    public QuestionManager getQuestionManager() { 
    	return questionManager; 
    }
=======
>>>>>>> branch 'main' of https://github.com/inf1009-p10-9/abstract-engine
}
