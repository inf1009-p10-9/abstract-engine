package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.IEntityQueryable;
import io.github.inf1009_p10_9.interfaces.IEntityRegisterable;
import io.github.inf1009_p10_9.interfaces.IManager;

// singleton that maintains the list of active entities and processes additions, removals, and updates each frame
public class EntityManager implements IManager,
                                      IEntityQueryable,
                                      IEntityRegisterable {
    private static EntityManager instance;

    // the live entity list, plus queues to safely add/remove during iteration
    private Array<Entity> entities = new Array<>();
    private Queue<Entity> entitiesToAdd = new Queue<>();
    private Queue<Entity> entitiesToRemove = new Queue<>();

    private EntityManager() {}

    public static synchronized EntityManager getInstance() {
        if (instance == null)
            instance = new EntityManager();
        return instance;
    }

    @Override
    public void initialize() {
        clear();
    }

    // flushes pending additions and removals, then updates all active entities
    @Override
    public void update() {
        while (entitiesToAdd.size > 0) {
            entities.add(entitiesToAdd.removeFirst());
        }

        while (entitiesToRemove.size > 0) {
            entities.removeValue(entitiesToRemove.removeFirst(), true);
        }

        for (Entity entity : entities) {
            if (entity.isActive())
                entity.update();
        }
    }

    // clears all entity lists and queues
    public void clear() {
        entities.clear();
        entitiesToAdd.clear();
        entitiesToRemove.clear();
    }

    // queued so additions happen safely between frames, not mid-iteration
    @Override
    public void addEntity(Entity entity) {
        entitiesToAdd.addLast(entity);
    }

    // queued for the same reason as addEntity
    @Override
    public void removeEntity(Entity entity) {
        entitiesToRemove.addLast(entity);
    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }

    public <T extends Entity> T getFirstOfType(Class<T> type) {
        for (Entity entity : entities) {
            if (type.isInstance(entity)) {
                return type.cast(entity);
            }
        }
        return null;
    }
    
    public int getEntityCount() {
        return entities.size;
    }
    
}
