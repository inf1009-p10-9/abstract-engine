package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.ObjectMap;
import io.github.inf1009_p10_9.interfaces.*;

// singleton that holds a movement strategy per entity type and applies the right one when move() is called
public class MovementManager implements IManagerMinimal,
                                        IMovementCalculatable,
                                        IMovementStrategyRegisterable {
    private static MovementManager instance;

    // keyed by entity class name, e.g. "Player", "Enemy"
    private ObjectMap<String, IMovementStrategy> movementStrategies = new ObjectMap<>();

    private MovementManager() {}

    public static synchronized MovementManager getInstance() {
        if (instance == null)
            instance = new MovementManager();
        return instance;
    }

    @Override
    public void initialize() {
        System.out.println("MovementManger initialised");
        clear();
    }

    @Override
    public void clear() {
        movementStrategies.clear();
    }

    @Override
    public void registerMovementStrategy(String objectType, IMovementStrategy strategy) {
        movementStrategies.put(objectType, strategy);
    }

    @Override
    public IMovementStrategy getMovementStrategy(String objectType) {
        return movementStrategies.get(objectType);
    }

    // looks up the strategy by the entity's class name and delegates movement calculation to it
    @Override
    public void move(IPositionable object, int moveDirection) {
        String entityType = object.getClass().getSimpleName();
        IMovementStrategy strategy = movementStrategies.get(entityType);

        if (strategy != null) {
            strategy.calculateMovement(object, moveDirection);
        }
    }
}
