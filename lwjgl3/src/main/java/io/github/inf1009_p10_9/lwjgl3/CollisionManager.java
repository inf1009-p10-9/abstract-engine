package io.github.inf1009_p10_9.lwjgl3;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {

    private List<ICollidable> collidables;
    private ICollisionStrategy collisionStrategy;
    private ICollisionResolver collisionResolver;

    public CollisionManager() {
        collidables = new ArrayList<>();
        collisionResolver = new RigidBodyResolver(); 
    }

    public void registerCollidable(ICollidable obj) {
        if (!collidables.contains(obj)) {
            collidables.add(obj);
        }
    }

    public void unregisterCollidable(ICollidable obj) {
        collidables.remove(obj);
    }

    public void setCollisionStrategy(ICollisionStrategy strategy) {
        this.collisionStrategy = strategy;
    }

    public void setCollisionResolver(ICollisionResolver resolver) {
        this.collisionResolver = resolver;
    }

    public void update() {
        checkCollisions();
    }

    public void checkCollisions() {
        if (collisionStrategy == null) return;

        for (int i = 0; i < collidables.size(); i++) {
            for (int j = i + 1; j < collidables.size(); j++) {
                ICollidable a = collidables.get(i);
                ICollidable b = collidables.get(j);

                if (collisionStrategy.checkCollision(a.getBounds(), b.getBounds())) {
                    
                    // 1. Resolve Physics (Move them apart)
                    if (collisionResolver != null) {
                        collisionResolver.resolve(a, b);
                    }

                    // 2. Notify Game Logic (Events)
                    notifyCollision(a, b);
                }
            }
        }
    }

    private void notifyCollision(ICollidable a, ICollidable b) {
        a.onCollision(b);
        b.onCollision(a);
    }

    public void clear() {
        collidables.clear();
    }
}
