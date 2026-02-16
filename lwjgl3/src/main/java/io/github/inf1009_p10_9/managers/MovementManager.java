package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.ObjectMap;
import io.github.inf1009_p10_9.interfaces.*;

public class MovementManager implements IMovementCalculatable, IMovementStrategyReturnable, IMovementStrategyRegisterable {
    private static MovementManager instance;

    private ObjectMap<String, IMovementStrategy> movementStrategies;

    private MovementManager() {
        movementStrategies = new ObjectMap<>();
    }

    public static MovementManager getInstance() {
        if (instance == null) {
            instance = new MovementManager();
        }
        return instance;
    }

    public void initialize() {
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

//    public void update() {
//        //
//    }

    public void clear() {
        movementStrategies.clear();
    }
}
