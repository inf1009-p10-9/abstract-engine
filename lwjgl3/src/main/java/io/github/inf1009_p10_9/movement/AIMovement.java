package io.github.inf1009_p10_9.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.IPositionable;
import io.github.inf1009_p10_9.interfaces.IMovementStrategy;

public class AIMovement implements IMovementStrategy {
    private float speed;
    private Vector2 targetPosition;
    private MovementPattern pattern;

    // All movement patterns for AI entity to choose from
    public enum MovementPattern {
        RANDOM,
        PATROL,
        CHASE,
        FLEE
    }

    public AIMovement() {
        this.speed = 100f;
        this.pattern = MovementPattern.RANDOM;
    }

    public AIMovement(float speed, MovementPattern pattern) {
        this.speed = speed;
        this.pattern = pattern;
    }

    public void calculateMovement(IPositionable object, int moveCount) {
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

    private void calculateRandomMovement(Vector2 position, float distance) {
        // Random direction
        float angle = MathUtils.random(0f, 360f);
        position.x += MathUtils.cosDeg(angle) * distance;
        position.y += MathUtils.sinDeg(angle) * distance;
    }

    private void calculatePatrolMovement(Vector2 current, Vector2 newPos, float distance) {
        // Simple back-and-forth patrol
        if (targetPosition == null) {
            targetPosition = new Vector2(current.x + 200, current.y);
        }

        Vector2 direction = new Vector2(targetPosition).sub(current).nor();
        newPos.add(direction.scl(distance));

        // Check if reached target
        if (current.dst(targetPosition) < 10f) {
            // Reverse direction
            targetPosition.set(current.x - 200, current.y);
        }
    }

    private void calculateChaseMovement(Vector2 current, Vector2 newPos, float distance) {
        // Chase a target (would need target reference in real implementation)
        if (targetPosition != null) {
            Vector2 direction = new Vector2(targetPosition).sub(current).nor();
            newPos.add(direction.scl(distance));
        }
    }

    private void calculateFleeMovement(Vector2 current, Vector2 newPos, float distance) {
        // Flee from a target (opposite of chase)
        if (targetPosition != null) {
            Vector2 direction = new Vector2(current).sub(targetPosition).nor();
            newPos.add(direction.scl(distance));
        }
    }

    public void setTargetPosition(Vector2 target) {
        this.targetPosition = target;
    }

    public void setPattern(MovementPattern pattern) {
        this.pattern = pattern;
    }
}
