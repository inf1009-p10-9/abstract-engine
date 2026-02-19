package io.github.inf1009_p10_9.interfaces;

import io.github.inf1009_p10_9.entities.Entity;

/**
 * Interface for adding/removing entities
 */
public interface IEntityRegisterable {
    void addEntity(Entity entity);
    void removeEntity(Entity entity);
}
