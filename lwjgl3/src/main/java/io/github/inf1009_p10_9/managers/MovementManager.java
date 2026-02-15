package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.IMovementStrategy;
import io.github.inf1009_p10_9.interfaces.IPositionable;

public class MovementManager {
    private static MovementManager instance;

    private ObjectMap<String, IMovementStrategy> movementStrategies;
    private IntMap<String> keyMovementMap; // Maps key codes to movement types

    private MovementManager() {
        movementStrategies = new ObjectMap<>();
        keyMovementMap = new IntMap<>();
    }

    public static MovementManager getInstance() {
        if (instance == null) {
            instance = new MovementManager();
        }
        return instance;
    }

    public void initialize() {
        movementStrategies.clear();
        keyMovementMap.clear();
    }

    public void registerMovementStrategy(String objectType, IMovementStrategy strategy) {
        movementStrategies.put(objectType, strategy);
    }

    public IMovementStrategy getMovementStrategy(String objectType) {
        return movementStrategies.get(objectType);
    }

    public void registerKeyMapping(int keyCode, String movementType) {
        keyMovementMap.put(keyCode, movementType);
    }

    public void move(IPositionable object, int moveCount) {
        // Determine which strategy to use based on entity type
        String entityType = object.getClass().getSimpleName();
        IMovementStrategy strategy = movementStrategies.get(entityType);

        if (strategy != null) {
            strategy.calculateMovement(object, moveCount);
        }
    }

    public void update() {
        // Movement is triggered by input or AI, not on every frame
    }

    public void clear() {
        movementStrategies.clear();
        keyMovementMap.clear();
    }

    public String getMovementTypeForKey(int keyCode) {
        return keyMovementMap.get(keyCode);
    }
}
