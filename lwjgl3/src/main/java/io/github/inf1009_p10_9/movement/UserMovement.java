package io.github.inf1009_p10_9.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.IPositionable;
import io.github.inf1009_p10_9.interfaces.IMovementStrategy;

public class UserMovement implements IMovementStrategy {
    private float speed;

    public UserMovement() {
        this.speed = 200f; // pixels per second
    }

    public UserMovement(float speed) {
        this.speed = speed;
    }


    public void calculateMovement(IPositionable object, int moveCount) {
        if (object == null) {
            return;
        }

        Vector2 currentPos = object.getPosition();
        Vector2 newPos = new Vector2(currentPos);

        float delta = Gdx.graphics.getDeltaTime();
        float moveDistance = speed * delta;

        // Calculate new position based on moveCount
        // moveCount could represent direction: 0=up, 1=down, 2=left, 3=right
        switch (moveCount) {
            case 0: // Up
                newPos.y += moveDistance;
                break;
            case 1: // Down
                newPos.y -= moveDistance;
                break;
            case 2: // Left
                newPos.x -= moveDistance;
                break;
            case 3: // Right
                newPos.x += moveDistance;
                break;
        }

        // Apply the movement through IPositionable
        object.setPosition(newPos);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
