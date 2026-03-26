package io.github.inf1009_p10_9.engine.interfaces;

// only some entities participate in collision resolving
public interface ICollidableResolvable {
    void onCollision(ICollidableDetectable other); // called on both objects when a collision is detected
}
