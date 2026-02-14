package io.github.inf1009_p10_9.lwjgl3;

import java.util.Random;

public class GenericAIMovementStrategy implements iMovementStrategy {
    @Override
    public void applyMovement(Entity target, int moveCount, String moveDirection) {
        //Random Wander
        String actualDirection = moveDirection;

        if (actualDirection == null || actualDirection.isEmpty()) {
            Random random = new Random();
            String[] directions = {"left", "right", "up", "down"};

            actualDirection = directions[random.nextInt(directions.length)];
        }

        //TODO: get position bean of entity
        float currentX = BEAN.getPosition().getX();
        float currentY = BEAN.getPosition().getY();
        float currentSpeed = BEAN.getSpeed();
        float displacement = currentSpeed * moveCount;

        System.out.println("AI Entity: decided to move: " + actualDirection);

        switch (actualDirection.toLowerCase()) {
            case "left":  currentX -= displacement; break;
            case "right": currentX += displacement; break;
            case "up":    currentY += displacement; break;
            case "down":  currentY -= displacement; break;
            default:
                // do nothing
        }

        //TODO: see if need to return/update bean
    }
}
