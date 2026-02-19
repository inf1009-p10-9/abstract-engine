package io.github.inf1009_p10_9.interfaces;

import com.badlogic.gdx.utils.Array;
import io.github.inf1009_p10_9.entities.Entity;

/**
 * Interface for querying entities
 */
public interface IEntityQueryable {
    Array<Entity> getEntities();
}
