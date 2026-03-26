package io.github.inf1009_p10_9.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.inf1009_p10_9.interfaces.IMovementScrollAdjustable;
import io.github.inf1009_p10_9.interfaces.IMovementStrategy;
import io.github.inf1009_p10_9.interfaces.IPositionable;

public class ScrollerMovement implements IMovementStrategy, IMovementScrollAdjustable {
    private float scrollSpeed = 90f;
    private final float MAX_SPEED_LIMIT = 200f;

    public void calculateMovement(IPositionable object, int moveDirection){
        if (object == null){
            return;
        }

        Vector2 currentPos = object.getPosition();
        Vector2 newPos = new Vector2(currentPos);

        newPos.y -= scrollSpeed * Gdx.graphics.getDeltaTime(); //scroll downwards
        if(newPos.y < 0){
            newPos.y = Gdx.graphics.getHeight(); //wrap back to top
        }

        object.setPosition(newPos);
    }

    public void adjustScrollSpeed(float targetSpeed) {
        if (targetSpeed < MAX_SPEED_LIMIT)
            scrollSpeed = targetSpeed;
        else
            System.out.println("Max scroll speed reached");
    }
}
