package io.github.inf1009_p10_9.interfaces;

// maps entity type names to their movement strategies
public interface IMovementStrategyRegisterable {
    void registerMovementStrategy(String objectType, IMovementStrategy strategy);
    IMovementStrategy getMovementStrategy(String objectType);
}
