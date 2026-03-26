package io.github.inf1009_p10_9.engine.interfaces;

public interface IMovementStrategyRetrievable {
    IMovementStrategy getMovementStrategy(Class<?> objectClass);
    Iterable<IMovementStrategy> getAllStrategies();
}
