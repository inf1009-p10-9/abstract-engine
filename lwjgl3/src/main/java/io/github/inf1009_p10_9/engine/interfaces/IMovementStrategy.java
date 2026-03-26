package io.github.inf1009_p10_9.engine.interfaces;

// swappable algorithm that calculates and applies movement to a positionable object
public interface IMovementStrategy {
    void calculateMovement(IPositionable object, int moveDirection);
}
