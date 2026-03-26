package io.github.inf1009_p10_9.engine.interfaces;
import com.badlogic.gdx.utils.Array;

import io.github.inf1009_p10_9.engine.core.Entity;

// read-only access to the current entity list
public interface IEntityQueryable {
    Array<Entity> getEntities();
    <T extends Entity> T getFirstOfType(Class<T> type);
}