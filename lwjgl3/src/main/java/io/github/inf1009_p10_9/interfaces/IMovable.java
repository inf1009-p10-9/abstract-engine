package io.github.inf1009_p10_9.interfaces;

import com.badlogic.gdx.math.Vector2;
import io.github.inf1009_p10_9.entities.Entity;

public interface IMovable {
    void moveEntity(Entity entity, Vector2 newPosition);
}
