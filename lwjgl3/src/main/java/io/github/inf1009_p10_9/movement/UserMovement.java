package io.github.inf1009_p10_9.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.inf1009_p10_9.interfaces.IPositionable;
import io.github.inf1009_p10_9.interfaces.IMovementStrategy;

public class UserMovement implements IMovementStrategy {
    private float speed;

    public UserMovement(float speed) {
        this.speed = speed;
    }

    public void calculateMovement(IPositionable object, int moveDirection) {
        if (object == null) {
            return;
        }

        Vector2 currentPos = object.getPosition();
        Vector2 newPos = new Vector2(currentPos);

        float delta = Gdx.graphics.getDeltaTime();
        float moveDistance = speed * delta; //calculate considering delta: consistent across all fps

        //Calculate new position based on direction
        switch (moveDirection) {
            case 0: //Up
                newPos.y += moveDistance;
                break;
            case 1: //Down
                newPos.y -= moveDistance;
                break;
            case 2: //Left
                newPos.x -= moveDistance;
                break;
            case 3: //Right
                newPos.x += moveDistance;
                break;
        }

        //Apply movement through IPositionable
        object.setPosition(newPos);
    }
}
