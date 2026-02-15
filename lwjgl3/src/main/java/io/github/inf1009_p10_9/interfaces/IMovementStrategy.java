package io.github.inf1009_p10_9.interfaces;

import io.github.inf1009_p10_9.entities.Entity;

public interface IMovementStrategy {
    void calculateMovement(Entity object, int moveCount, IMovable movable);
}
