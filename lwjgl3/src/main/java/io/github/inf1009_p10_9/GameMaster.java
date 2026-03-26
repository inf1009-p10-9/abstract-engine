package io.github.inf1009_p10_9;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.ApplicationAdapter;

import io.github.inf1009_p10_9.engine.interfaces.IManager;
import io.github.inf1009_p10_9.engine.interfaces.IManagerMinimal;
import io.github.inf1009_p10_9.engine.managers.CollisionManager;
import io.github.inf1009_p10_9.engine.managers.EntityManager;
import io.github.inf1009_p10_9.engine.managers.InputManager;
import io.github.inf1009_p10_9.engine.managers.MovementManager;
import io.github.inf1009_p10_9.engine.managers.OutputManager;
import io.github.inf1009_p10_9.engine.managers.SceneManager;

public class GameMaster extends ApplicationAdapter {

    // all managers injected via constructor
    protected final SceneManager sceneManager;
    protected final EntityManager entityManager;
    protected final CollisionManager collisionManager;
    protected final MovementManager movementManager;
    protected final InputManager inputManager;
    protected final OutputManager outputManager;
    protected final List<IManager> managers;
    protected final List<IManagerMinimal> managersMinimal;

    // controls whether the game loop is running
    private boolean running;

    // all dependencies are passed in from outside, nothing is created here
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
        this.managersMinimal = new ArrayList<IManagerMinimal>();
        this.managersMinimal.add(this.movementManager);
    }

    @Override
    public void create() {
        initialize();
    }

    public void initialize() {
        // kick off each manager in order
        for (IManager manager : managers)
            manager.initialize();
        for (IManagerMinimal managerMinimal : managersMinimal)
            managerMinimal.initialize();
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
        // clean up all managers on shutdown
        for (IManager manager : managers)
            manager.clear();
        for (IManagerMinimal managerMinimal : managersMinimal)
            managerMinimal.clear();

        System.out.println("GameMaster disposed");
    }
}
