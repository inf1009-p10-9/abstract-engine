package io.github.inf1009_p10_9.lwjgl3;

public interface iMovementStrategy {
    public void applyMovement(Entity target, int moveCount, String moveDirection);
}
