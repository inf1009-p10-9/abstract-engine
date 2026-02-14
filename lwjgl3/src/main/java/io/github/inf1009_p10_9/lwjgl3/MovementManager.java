package io.github.inf1009_p10_9.lwjgl3;
import java.util.Map;
import java.util.HashMap;

public class MovementManager implements iMovementRegisterable, iMovementCalculatable {
    private static MovementManager instance;
    private Map<String, iMovementStrategy> roleToStrategyMap = new HashMap<>();
    private Map<Entity, String> entityToRoleMap = new HashMap<>();

    public MovementManager() throws Exception { //Singleton enforcement
        if (MovementManager.instance != null){
            throw new Exception("Singleton: Movement Manager instance already exists.");
        }
        MovementManager.instance = this;
    }

    public void addStrategy(String role, iMovementStrategy strategy) {
        roleToStrategyMap.put(role.toLowerCase(), strategy);
    }

    @Override
    public void registerStrategy(Entity entity, String entityRole){
        if (entityRole != null && entityRole.matches("[a-zA-Z0-9]+]")){
            entityToRoleMap.put(entity, entityRole.toLowerCase());
        } else {
            throw new IllegalArgumentException(entityRole + " is not a valid entity role. Only alphanumeric allowed");
        }
    }

    @Override
    public void move(Entity target, int moveCount, String moveDirection) {
        String role = entityToRoleMap.get(target);
        iMovementStrategy strategy = roleToStrategyMap.get(role);

        if (strategy != null) {
            strategy.applyMovement(target, moveCount, moveDirection);
        } else{
            throw new IllegalArgumentException("target has " + role + ", is not a valid entity role.");
        }
    }
}
