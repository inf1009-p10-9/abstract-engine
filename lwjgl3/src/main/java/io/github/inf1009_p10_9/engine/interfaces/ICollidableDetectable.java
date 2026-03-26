package io.github.inf1009_p10_9.engine.interfaces;
import com.badlogic.gdx.math.Rectangle;

// all entities participates in collision detection
public interface ICollidableDetectable {
    Rectangle getBounds();   // get collision rectangle bounds
    int getCollisionLayer(); // objects only collide with others on the same layer

    default ICollidableResolvable asResolvable() { //returns ICollidableResolvable if available, otherwise return null (to be overriden by entity)
        return null;
    }
}
