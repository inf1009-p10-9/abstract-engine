package io.github.inf1009_p10_9.interfaces;

import com.badlogic.gdx.math.Rectangle;

public interface ICollisionStrategy {
    boolean checkCollision(Rectangle a, Rectangle b);
}
