package io.github.inf1009_p10_9.interfaces;

import com.badlogic.gdx.math.Rectangle;

public interface ICollidable {
    Rectangle getBounds();
    void onCollision(ICollidable other);
    int getCollisionLayer();
}
