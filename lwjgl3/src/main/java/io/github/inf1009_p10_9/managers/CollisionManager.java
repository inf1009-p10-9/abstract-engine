package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.Array;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.ICollidableRegisterable;
import io.github.inf1009_p10_9.interfaces.ICollisionStrategy;
import io.github.inf1009_p10_9.interfaces.IManager;

public class CollisionManager implements IManager,
                                         ICollidableRegisterable {
    private static CollisionManager instance;

    private Array<ICollidable> collidables = new Array<>();
    private ICollisionStrategy collisionStrategy;

    private CollisionManager() {}

    protected void resolveCollision(ICollidable a, ICollidable b) {
        a.onCollision(b);
        b.onCollision(a);
    }

    public static synchronized CollisionManager getInstance() {
        if (instance == null)
            instance = new CollisionManager();
        return instance;
    }

    @Override
    public void initialize() {
        clear();
    }

    @Override
    public void update() {
        checkCollisions();
    }

    @Override
    public void clear() {
        collidables.clear();
    }

    @Override
    public void registerCollidable(ICollidable obj) {
        if (!collidables.contains(obj, true)) {
            collidables.add(obj);
        }
    }

    @Override
    public void unregisterCollidable(ICollidable obj) {
        collidables.removeValue(obj, true);
    }

    public void setCollisionStrategy(ICollisionStrategy strategy) {
        collisionStrategy = strategy;
    }

    public void checkCollisions() {
        if (collisionStrategy == null) {
            return;
        }

        for (int i = 0; i < collidables.size; i++) {
            ICollidable a = collidables.get(i);
            for (int j = i+1; j < collidables.size; j++) {
                ICollidable b = collidables.get(j);
                // Check if collision layers allow collision
                if (a.getCollisionLayer() != b.getCollisionLayer()) {
                    continue;
                }

                if (collisionStrategy.checkCollision(a.getBounds(), b.getBounds())) {
                    resolveCollision(a, b);
                }
            }
        }
    }
}
