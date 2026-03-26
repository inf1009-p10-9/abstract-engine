package io.github.inf1009_p10_9.engine.interfaces;

// triggers movement on an object using whichever strategy is registered for its type
public interface IMovementCalculatable {
    void move(IPositionable object, int moveDirection);
}
