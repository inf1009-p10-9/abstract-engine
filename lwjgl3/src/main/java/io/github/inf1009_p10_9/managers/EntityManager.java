package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.IEntityQueryable;
import io.github.inf1009_p10_9.interfaces.IEntityRegisterable;
import io.github.inf1009_p10_9.interfaces.IManager;

public class EntityManager implements IManager,
                                      IEntityQueryable,
                                      IEntityRegisterable {
    private static EntityManager instance;

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

    @Override
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
            if (entity.isActive())
                entity.update();
        }
    }

    public void clear() {
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

    public int getEntityCount() {
        return entities.size;
    }
}
