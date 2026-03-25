package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.Array;
import io.github.inf1009_p10_9.interfaces.ICollidableDetectable;
import io.github.inf1009_p10_9.interfaces.ICollidableResolvable;
import io.github.inf1009_p10_9.interfaces.ICollidableRegisterable;
import io.github.inf1009_p10_9.interfaces.ICollisionStrategy;
import io.github.inf1009_p10_9.interfaces.IManager;

// singleton that checks for collisions between all registered collidables each frame
public class CollisionManager implements IManager,
                                         ICollidableRegisterable {
    private static CollisionManager instance;

    private Array<ICollidableDetectable> collidables = new Array<>();

    // the strategy determines how collision bounds are tested, swappable at runtime
    private ICollisionStrategy collisionStrategy;

    private CollisionManager() {}

    // notifies both objects when a collision is detected
    protected void resolveCollision(ICollidableDetectable a, ICollidableDetectable b) {
        ICollidableResolvable resA = a.asResolvable();
        ICollidableResolvable resB = b.asResolvable();

        if (resA != null) resA.onCollision(b);
        if (resB != null) resB.onCollision(a);
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

    // registering and unregistering collidables
    @Override
    public void registerCollidable(ICollidableDetectable obj) {
        if (!collidables.contains(obj, true)) {
            collidables.add(obj);
        }
    }

    @Override
    public void unregisterCollidable(ICollidableDetectable obj) {
        collidables.removeValue(obj, true);
    }

    public void setCollisionStrategy(ICollisionStrategy strategy) {
        collisionStrategy = strategy;
    }

    // tests each unique pair of collidables, skipping pairs on different collision layers
    public void checkCollisions() {
        if (collisionStrategy == null) {
            return;
        }

        for (int i = 0; i < collidables.size; i++) {
            ICollidableDetectable a = collidables.get(i);
            for (int j = i+1; j < collidables.size; j++) {
                ICollidableDetectable b = collidables.get(j);

                // objects on different layers do not collide with each other
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
