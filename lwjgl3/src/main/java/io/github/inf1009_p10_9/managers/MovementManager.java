package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.ObjectMap;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.*;

// singleton that holds a movement strategy per entity type and applies the right one when move() is called
public class MovementManager implements IManagerMinimal,
                                        IMovementCalculatable,
                                        IMovementStrategyRegisterable {
    private static MovementManager instance;

    // keyed by entity .class types
    private final ObjectMap<Class<?>, IMovementStrategy> movementStrategies = new ObjectMap<>();

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
    public <T extends Entity> void registerMovementStrategy(Class<T> entityType, IMovementStrategy strategy) {
        movementStrategies.put(entityType, strategy);
    }

    @Override
    public IMovementStrategy getMovementStrategy(Class<?> objectClass) {
        return movementStrategies.get(objectClass);
    }

    // looks up the strategy by the entity's class name and delegates movement calculation to it
    @Override
    public void move(IPositionable object, int moveDirection) {
        Class<?> objectClass = object.getClass();
        IMovementStrategy strategy = getMovementStrategy(objectClass);

        if (strategy != null) {
            strategy.calculateMovement(object, moveDirection);
        }
    }
}
