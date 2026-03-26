package io.github.inf1009_p10_9.engine.interfaces;

import com.badlogic.gdx.math.Vector2;

import io.github.inf1009_p10_9.engine.core.Entity;

// anything with a position that movement strategies can read and write
public interface IPositionable {
    void setPosition(Vector2 newPosition);
    Vector2 getPosition();
}
