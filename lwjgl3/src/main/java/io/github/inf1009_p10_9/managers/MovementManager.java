package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.ObjectMap;
import io.github.inf1009_p10_9.interfaces.*;

public class MovementManager implements IManager,
                                        IMovementCalculatable,
                                        IMovementStrategyRegisterable {
    private static MovementManager instance;

    private ObjectMap<String, IMovementStrategy> movementStrategies = new ObjectMap<>();

    private MovementManager() {}

    public static synchronized MovementManager getInstance() {
        if (instance == null)
            instance = new MovementManager();
        return instance;
    }

    @Override
    public void initialize() {
        clear();
    }

    @Override
    public void update() {}

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

    @Override
    public void move(IPositionable object, int moveDirection) {
        //Determine which strategy to use based on entity type
        String entityType = object.getClass().getSimpleName();
        IMovementStrategy strategy = movementStrategies.get(entityType);

        if (strategy != null) {
            strategy.calculateMovement(object, moveDirection);
        }
    }
}
