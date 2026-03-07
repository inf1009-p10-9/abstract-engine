package io.github.inf1009_p10_9;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.ApplicationAdapter;

import io.github.inf1009_p10_9.interfaces.IManager;
import io.github.inf1009_p10_9.managers.*;

public class GameMaster extends ApplicationAdapter {

    // managers (injected via constructor)
    protected final SceneManager sceneManager;
    protected final EntityManager entityManager;
    protected final CollisionManager collisionManager;
    protected final MovementManager movementManager;
    protected final InputManager inputManager;
    protected final OutputManager outputManager;
    protected final List<IManager> managers;

    // question manager (singleton, not injected)
    protected final QuestionManager questionManager;

    // game loop control
    private boolean running;

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

        // get singleton instance
        this.questionManager = QuestionManager.getInstance();

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
        // initialize all managers
        for (IManager manager : managers) {
            manager.initialize();
        }

        // initialize question manager separately since it doesn't implement IManager
        questionManager.initialize();

        // register context
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
        for (IManager manager : managers) {
            manager.update();
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void dispose() {
        for (IManager manager : managers) {
            manager.clear();
        }

        // clean up question manager
        questionManager.clear();

        System.out.println("GameMaster disposed");
    }

    // getters
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
}