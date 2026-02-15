package io.github.inf1009_p10_9.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import io.github.inf1009_p10_9.interfaces.ICollisionStrategy;

public class AABBCollisionStrategy implements ICollisionStrategy {
    @Override
    public boolean checkCollision(Rectangle a, Rectangle b) {
        return Intersector.overlaps(a, b);
    }
}
