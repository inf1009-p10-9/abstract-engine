package io.github.inf1009_p10_9.interfaces;
import io.github.inf1009_p10_9.entities.Entity;

// maps entity type class to their movement strategies
public interface IMovementStrategyRegisterable {
    <T extends Entity> void registerMovementStrategy(Class<T> entityType, IMovementStrategy strategy);
}
