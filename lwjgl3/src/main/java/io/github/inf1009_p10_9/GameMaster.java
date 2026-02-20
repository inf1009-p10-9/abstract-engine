package io.github.inf1009_p10_9;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.ApplicationAdapter;

import io.github.inf1009_p10_9.interfaces.IManager;
import io.github.inf1009_p10_9.managers.*;

public class GameMaster extends ApplicationAdapter {

    // Managers (injected via constructor)
    protected final SceneManager sceneManager;
    protected final EntityManager entityManager;
    protected final CollisionManager collisionManager;
    protected final MovementManager movementManager;
    protected final InputManager inputManager;
    protected final OutputManager outputManager;
    protected final List<IManager> managers;

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
        // Initialize all managers (they're already created)
        for (IManager manager : managers)
            manager.initialize();
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
        for (IManager manager : managers) {
            manager.clear();
        }

        System.out.println("GameMaster disposed");
    }
}
