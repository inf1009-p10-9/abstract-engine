package io.github.inf1009_p10_9.input;

import com.badlogic.gdx.Input;
import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.IInputListens;

public class Keyboard implements IInputListens {

    @Override
    public void onInput(io.github.inf1009_p10_9.input.Input input) {
        if (input == null) {
            return;
        }
    }

    // This method will be called every frame from the game loop
    public void update() {
        // Get all entities and find the player
        com.badlogic.gdx.utils.Array<Entity> entities = GameContext.getEntityManager().getEntities();

        for (Entity entity : entities) {
            // Check if this entity is a Player
            if (entity.getClass().getSimpleName().equals("Player")) {

                // Check which keys are currently held down
                boolean moved = false;

                // Move Up
                if (GameContext.getInputManager().isKeyPressed(Input.Keys.W) || GameContext.getInputManager().isKeyPressed(Input.Keys.UP)) {
                    GameContext.getMovementManager().move(entity, 0); // 0 = up
                    moved = true;
                }

                // Move Down
                if (GameContext.getInputManager().isKeyPressed(Input.Keys.S) || GameContext.getInputManager().isKeyPressed(Input.Keys.DOWN)) {
                    GameContext.getMovementManager().move(entity, 1); // 1 = down
                    moved = true;
                }

                // Move Left
                if (GameContext.getInputManager().isKeyPressed(Input.Keys.A) || GameContext.getInputManager().isKeyPressed(Input.Keys.LEFT)) {
                    GameContext.getMovementManager().move(entity, 2); // 2 = left
                    moved = true;
                }

                // Move Right
                if (GameContext.getInputManager().isKeyPressed(Input.Keys.D) || GameContext.getInputManager().isKeyPressed(Input.Keys.RIGHT)) {
                    GameContext.getMovementManager().move(entity, 3); // 3 = right
                    moved = true;
                }

                // Action keys (these still work on press)
                //if (GameContext.getInputManager().isKeyPressed(Input.Keys.SPACE)) {
                    // Add action logic here
                //}
            }
        }
    }
}
