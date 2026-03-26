package io.github.inf1009_p10_9.engine.interfaces;

import com.badlogic.gdx.math.Rectangle;

// swappable algorithm for testing whether two bounding boxes overlap
public interface ICollisionStrategy {
    boolean checkCollision(Rectangle a, Rectangle b);
}
