package io.github.inf1009_p10_9.lwjgl3;

public interface ICollisionResolver {

    // Resolves the collision by moving objects apart so they no longer overlap.
    void resolve(ICollidable a, ICollidable b);
}
