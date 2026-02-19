package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.ObjectMap;
import io.github.inf1009_p10_9.interfaces.*;

public class MovementManager implements IMovementCalculatable,
                                        IMovementStrategyRegisterable {

    private ObjectMap<String, IMovementStrategy> movementStrategies;

    public MovementManager() {
        movementStrategies = new ObjectMap<>();
    }


    public void initialize() {
        movementStrategies.clear();
    }

    @Override
    public void registerMovementStrategy(String objectType, IMovementStrategy strategy) {
        movementStrategies.put(objectType, strategy);
    }

    public IMovementStrategy getMovementStrategy(String objectType) {
    @Override
        return movementStrategies.get(objectType);
    }

    public void move(IPositionable object, int moveDirection) {
        //Determine which strategy to use based on entity type
    @Override
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
