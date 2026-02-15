	package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import io.github.inf1009_p10_9.entities.Entity;

public class EntityManager {
    private static EntityManager instance;

    private Array<Entity> entities;
    private Queue<Entity> entitiesToAdd;
    private Queue<Entity> entitiesToRemove;

    private EntityManager() {
        entities = new Array<Entity>();
        entitiesToAdd = new Queue<Entity>();
        entitiesToRemove = new Queue<Entity>();
    }

    public static EntityManager getInstance() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    public void initialize() {
        entities.clear();
        entitiesToAdd.clear();
        entitiesToRemove.clear();
    }

    public void addEntity(Entity entity) {
        entitiesToAdd.addLast(entity);
    }

    public void removeEntity(Entity entity) {
        entitiesToRemove.addLast(entity);
    }

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
