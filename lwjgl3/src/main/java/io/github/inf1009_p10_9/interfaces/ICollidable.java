package io.github.inf1009_p10_9.interfaces;

import com.badlogic.gdx.math.Rectangle;

// anything that participates in collision detection
public interface ICollidable {
    Rectangle getBounds();
    void onCollision(ICollidable other); // called on both objects when a collision is detected
    int getCollisionLayer();             // objects only collide with others on the same layer
}
