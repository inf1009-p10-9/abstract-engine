package io.github.inf1009_p10_9.interfaces;

// triggers movement on an object using whichever strategy is registered for its type
public interface IMovementCalculatable {
    void move(IPositionable object, int moveDirection);
}
