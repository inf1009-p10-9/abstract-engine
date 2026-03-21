package io.github.inf1009_p10_9.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.inf1009_p10_9.interfaces.IPositionable;
import io.github.inf1009_p10_9.interfaces.IMovementStrategy;

// movement strategy for player-controlled entities, maps a direction integer to positional movement
public class UserMovement implements IMovementStrategy {
    private float speed;
    private final float leftBoundary;
    private final float rightBoundary;
    private final boolean enableVerticalMovement;
    
    public UserMovement(float speed, float leftBoundary, float rightBoundary, boolean enableVerticalMovement) {
        this.speed = speed;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.enableVerticalMovement = enableVerticalMovement;
    }
    
    // moveDirection: 0 = up, 1 = down, 2 = left, 3 = right
    public void calculateMovement(IPositionable object, int moveDirection) {
        if (object == null) {
            return;
        }

        Vector2 currentPos = object.getPosition();
        Vector2 newPos = new Vector2(currentPos);

        // multiply by delta so movement is consistent regardless of frame rate
        float delta = Gdx.graphics.getDeltaTime();
        float moveDistance = speed * delta;
        
        
        // if enable vertical movement, else skip this block of code
        if (enableVerticalMovement) {
	        switch (moveDirection) {
	            case 0: // up
	                newPos.y += moveDistance;
	                break;
	            case 1: // down
	                newPos.y -= moveDistance;
	                break;
	        }	
        }
        
        switch (moveDirection) {
            case 2: // left
                newPos.x -= moveDistance;
                break;
            case 3: // right
                newPos.x += moveDistance;
                break;
        }
        newPos.x = Math.max(leftBoundary, Math.min(newPos.x, rightBoundary));

        object.setPosition(newPos);
    }
}
