package io.github.inf1009_p10_9.interfaces;

import com.badlogic.gdx.utils.Array;

import io.github.inf1009_p10_9.entities.Entity;

/**
 * Interface for adding/removing entities
 */
public interface IEntityRegisterable extends IEntityQueryable {
    void addEntity(Entity entity);
    void removeEntity(Entity entity);
}
