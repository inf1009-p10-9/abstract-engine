package io.github.inf1009_p10_9.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.inf1009_p10_9.interfaces.IPositionable;
import io.github.inf1009_p10_9.interfaces.IMovementStrategy;

// movement strategy for ai-controlled entities, supports four behaviour patterns
public class AIMovement implements IMovementStrategy {
    private float speed;
    private Vector2 targetPosition;
    private AIMovementPattern pattern;

    // available movement behaviours, set at construction time
    public enum AIMovementPattern {
        RANDOM,
        PATROL,
        CHASE,
        FLEE
    }

    public AIMovement(float speed, AIMovementPattern pattern) {
        this.speed = speed;
        this.pattern = pattern;
    }

    // delegates to the appropriate pattern method based on the current pattern setting
    public void calculateMovement(IPositionable object, int moveDirection) {
        if (object == null) {
            return;
        }

        Vector2 currentPos = object.getPosition();
        Vector2 newPos = new Vector2(currentPos);

        float delta = Gdx.graphics.getDeltaTime();
        float moveDistance = speed * delta;

        switch (pattern) {
            case RANDOM:
                calculateRandomMovement(newPos, moveDistance);
                break;
            case PATROL:
                calculatePatrolMovement(currentPos, newPos, moveDistance);
                break;
            case CHASE:
                calculateChaseMovement(currentPos, newPos, moveDistance);
                break;
            case FLEE:
                calculateFleeMovement(currentPos, newPos, moveDistance);
                break;
        }

        object.setPosition(newPos);
    }

    // moves in a random direction each frame
    private void calculateRandomMovement(Vector2 position, float distance) {
        float randomAngle = MathUtils.random(0f, 360f);
        position.x += MathUtils.cosDeg(randomAngle) * distance;
        position.y += MathUtils.sinDeg(randomAngle) * distance;
    }

    // moves toward a target point then reverses direction when close enough.
    // note: currently overridden by flee in the demo scene
    private void calculatePatrolMovement(Vector2 current, Vector2 newPos, float distance) {
        if (targetPosition == null) {
            targetPosition = new Vector2(current.x + 200, current.y);
        }

        Vector2 direction = new Vector2(targetPosition).sub(current).nor();
        newPos.add(direction.scl(distance));

        if (current.dst(targetPosition) < 10f) {
            targetPosition.set(current.x - 200, current.y);
        }
    }

    // moves toward targetPosition, which should be updated every frame by the caller
    private void calculateChaseMovement(Vector2 current, Vector2 newPos, float distance) {
        if (targetPosition != null) {
            Vector2 direction = new Vector2(targetPosition).sub(current).nor();
            newPos.add(direction.scl(distance));
        }
    }

    // moves away from targetPosition, which should be updated every frame by the caller
    private void calculateFleeMovement(Vector2 current, Vector2 newPos, float distance) {
        if (targetPosition != null) {
            Vector2 direction = new Vector2(current).sub(targetPosition).nor();
            newPos.add(direction.scl(distance));
        }
    }

    // sets the position to chase or flee from, called each frame from MidScene
    public void setTargetPosition(Vector2 target) {
        this.targetPosition = target;
    }
}
