package io.github.inf1009_p10_9.interfaces;

public interface IMovementStrategyRegisterable {
    void registerMovementStrategy(String objectType, IMovementStrategy strategy);
}
