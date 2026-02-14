package io.github.inf1009_p10_9.lwjgl3;

import com.badlogic.gdx.math.Rectangle;

public interface ICollidable {

    Rectangle getBounds();

    // Called when a collision occurs (e.g., take damage, play sound) 
    void onCollision(ICollidable other);

    // Returns the layer ID for filtering (e.g., 1=Player, 2=Enemy)
    int getCollisionLayer();

    // Returns true if the object is immovable (e.g., walls)
    // Returns false if it is movable (e.g., players, crates)
    boolean isStatic();

    //Moves the object by the specified amount.
    void move(float dx, float dy);
}
