package io.github.inf1009_p10_9.interfaces;

import com.badlogic.gdx.utils.Array;
import io.github.inf1009_p10_9.entities.Entity;

// read-only access to the current entity list
public interface IEntityQueryable {
    Array<Entity> getEntities();
}
