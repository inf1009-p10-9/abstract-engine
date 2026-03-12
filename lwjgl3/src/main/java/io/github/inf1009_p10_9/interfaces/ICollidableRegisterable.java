package io.github.inf1009_p10_9.interfaces;

// registers and unregisters collidables with the collision manager
public interface ICollidableRegisterable {
    void registerCollidable(ICollidable obj);
    void unregisterCollidable(ICollidable obj);
}
