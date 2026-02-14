package io.github.inf1009_p10_9.lwjgl3;

public class PlayerMovementStrategy implements iMovementStrategy {
    @Override
    public void applyMovement(Entity target, int moveCount, String moveDirection) {
        //TODO: get iPositionable bean of entity
        float currentX = BEAN.getPosition().getX();
        float currentY = BEAN.getPosition().getY();
        float currentSpeed = BEAN.getSpeed();
        float displacement = currentSpeed * moveCount;

        switch (moveDirection.toLowerCase()) {
            case "left":
                currentX -= displacement;
                System.out.println("PlayerMovementStrategy: left by " + displacement);
                break;
            case "right":
                currentX += displacement;
                System.out.println("PlayerMovementStrategy: right by " + displacement);
                break;
            case "up":
                currentY += displacement;
                System.out.println("PlayerMovementStrategy: up by " + displacement);
                break;
            case "down":
                currentY -= displacement;
                System.out.println("PlayerMovementStrategy: down by " + displacement);
                break;
            default:
                throw new IllegalArgumentException("Invalid moveDirection: " + moveDirection);
        }

        //TODO: see if need to return/update bean
    }
}
