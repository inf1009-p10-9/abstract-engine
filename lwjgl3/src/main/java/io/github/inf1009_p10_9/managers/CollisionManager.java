package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.Array;
import io.github.inf1009_p10_9.interfaces.ICollidable;
import io.github.inf1009_p10_9.interfaces.ICollisionStrategy;

public class CollisionManager {
    private static CollisionManager instance;
    
    private Array<ICollidable> collidables;
    private ICollisionStrategy collisionStrategy;
    
    private CollisionManager() {
        collidables = new Array<>();
    }
    
    public static CollisionManager getInstance() {
        if (instance == null) {
            instance = new CollisionManager();
        }
        return instance;
    }
    
    public void initialize() {
        collidables.clear();
    }
    
    public void registerCollidable(ICollidable obj) {
        if (!collidables.contains(obj, true)) {
            collidables.add(obj);
        }
    }
    
    public void unregisterCollidable(ICollidable obj) {
        collidables.removeValue(obj, true);
    }
    
    public void setCollisionStrategy(ICollisionStrategy strategy) {
        this.collisionStrategy = strategy;
    }
    
    public void checkCollisions() {
        if (collisionStrategy == null) {
            return;
        }
        
        for (int i = 0; i < collidables.size; i++) {
            ICollidable a = collidables.get(i);
            
            for (int j = i + 1; j < collidables.size; j++) {
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
    
    public void resolveCollision(ICollidable a, ICollidable b) {
        a.onCollision(b);
        b.onCollision(a);
    }
    
    public void update() {
        checkCollisions();
    }
    
    public void clear() {
        collidables.clear();
    }
}
