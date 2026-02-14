package io.github.inf1009_p10_9.lwjgl3;

import com.badlogic.gdx.math.Rectangle;

public interface ICollisionStrategy {
    /** Returns true if the two rectangles overlap. */
    boolean checkCollision(Rectangle a, Rectangle b);
}
