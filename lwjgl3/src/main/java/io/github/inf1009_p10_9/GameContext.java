package io.github.inf1009_p10_9;

import io.github.inf1009_p10_9.managers.*;


//Helper file
public class GameContext {
    private static GameMaster GameMaster;
    
    public static void setGameMaster(GameMaster engine) {
        GameMaster = engine;
    }
    
    public static EntityManager getEntityManager() {
        return GameMaster != null ? GameMaster.getEntityManager() : null;
    }
    
    public static CollisionManager getCollisionManager() {
        return GameMaster != null ? GameMaster.getCollisionManager() : null;
    }
    
    public static SceneManager getSceneManager() {
        return GameMaster != null ? GameMaster.getSceneManager() : null;
    }
    
    public static MovementManager getMovementManager() {
        return GameMaster != null ? GameMaster.getMovementManager() : null;
    }
    
    public static InputManager getInputManager() {
        return GameMaster != null ? GameMaster.getInputManager() : null;
    }
    
    public static OutputManager getOutputManager() {
        return GameMaster != null ? GameMaster.getOutputManager() : null;
    }
}
