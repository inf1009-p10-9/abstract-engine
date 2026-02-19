	package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.IEntityQueryable;
import io.github.inf1009_p10_9.interfaces.IEntityRegisterable;

public class EntityManager implements IEntityQueryable, IEntityRegisterable {

    private Array<Entity> entities;
    private Queue<Entity> entitiesToAdd;
    private Queue<Entity> entitiesToRemove;

    public EntityManager() {
        entities = new Array<Entity>();
        entitiesToAdd = new Queue<Entity>();
        entitiesToRemove = new Queue<Entity>();
    }


    public void initialize() {
        entities.clear();
        entitiesToAdd.clear();
        entitiesToRemove.clear();
    }

    @Override
    public void addEntity(Entity entity) {
        entitiesToAdd.addLast(entity);
    }

    @Override
    public void removeEntity(Entity entity) {
        entitiesToRemove.addLast(entity);
    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }

    public void update() {
        // Process pending additions
        while (entitiesToAdd.size > 0) {
            entities.add(entitiesToAdd.removeFirst());
        }

        // Process pending removals
        while (entitiesToRemove.size > 0) {
            entities.removeValue(entitiesToRemove.removeFirst(), true);
        }

        // Update all entities
        for (Entity entity : entities) {
            if (entity.isActive()) {
                entity.update();
            }
        }
    }

    public void clear() {
        entities.clear();
        entitiesToAdd.clear();
        entitiesToRemove.clear();
    }

    public int getEntityCount() {
        return entities.size;
    }
}
