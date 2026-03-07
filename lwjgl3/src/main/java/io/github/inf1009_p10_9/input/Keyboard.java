package io.github.inf1009_p10_9.input;

import com.badlogic.gdx.Input;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.IEntityQueryable;
import io.github.inf1009_p10_9.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.interfaces.IInputListens;
import io.github.inf1009_p10_9.interfaces.IMovementCalculatable;

public class Keyboard implements IInputListens {

    private IMovementCalculatable movementCalculatable;
    private IInputKeyCheckable inputKeyCheckable;
    private IEntityQueryable entityQueryable;  // Interface injection

    public Keyboard(IMovementCalculatable movementCalculatable,
                    IInputKeyCheckable inputKeyCheckable,
                    IEntityQueryable entityQueryable) {
        this.movementCalculatable = movementCalculatable;
        this.inputKeyCheckable = inputKeyCheckable;
        this.entityQueryable = entityQueryable;
    }

    @Override
    public void onInput(io.github.inf1009_p10_9.input.Input input) {
        if (input == null) {
            return;
        }
    }

    // This method will be called every frame from the game loop
    public void update() {
        // Get all entities and find the player
        com.badlogic.gdx.utils.Array<Entity> entities = entityQueryable.getEntities();  // Uses interface

        for (Entity entity : entities) {
            // Check if this entity is a Player
            if (entity.getClass().getSimpleName().equals("Player")) {

                // Check which keys are currently held down
                boolean moved = false;

                // Move Up
                if (inputKeyCheckable.isKeyPressed(Input.Keys.W) || inputKeyCheckable.isKeyPressed(Input.Keys.UP)) {
                    movementCalculatable.move(entity, 0); // 0 = up
                    moved = true;
                }

                // Move Down
                if (inputKeyCheckable.isKeyPressed(Input.Keys.S) || inputKeyCheckable.isKeyPressed(Input.Keys.DOWN)) {
                    movementCalculatable.move(entity, 1); // 1 = down
                    moved = true;
                }

                // Move Left
                if (inputKeyCheckable.isKeyPressed(Input.Keys.A) || inputKeyCheckable.isKeyPressed(Input.Keys.LEFT)) {
                    movementCalculatable.move(entity, 2); // 2 = left
                    moved = true;
                }

                // Move Right
                if (inputKeyCheckable.isKeyPressed(Input.Keys.D) || inputKeyCheckable.isKeyPressed(Input.Keys.RIGHT)) {
                    movementCalculatable.move(entity, 3); // 3 = right
                    moved = true;
                }

                // Action keys
                //if (inputKeyCheckable.isKeyPressed(Input.Keys.SPACE)) {
                    // Add action logic here
                //}
            }
        }
    }
}
