package io.github.inf1009_p10_9.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.inf1009_p10_9.interfaces.IMovementStrategy;
import io.github.inf1009_p10_9.interfaces.IPositionable;

public class ScrollerMovement implements IMovementStrategy {
    private static final float SPEED = 60f;

    public void calculateMovement(IPositionable object, int moveDirection){
        if (object == null){
            return;
        }

        Vector2 currentPos = object.getPosition();
        Vector2 newPos = new Vector2(currentPos);
        
        if (object.getClass().getSimpleName() == "RoadDashes") {
        	System.out.println(object.getPosition());

        }
        newPos.y -= SPEED * Gdx.graphics.getDeltaTime(); //scroll downwards
        if(newPos.y < 0){
            newPos.y = Gdx.graphics.getHeight(); //wrap back to top
        }

        object.setPosition(newPos);
    }
}
