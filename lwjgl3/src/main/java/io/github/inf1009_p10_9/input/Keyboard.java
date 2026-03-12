package io.github.inf1009_p10_9.input;

import com.badlogic.gdx.Input;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.IEntityQueryable;
import io.github.inf1009_p10_9.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.interfaces.IInputListens;
import io.github.inf1009_p10_9.interfaces.IMovementCalculatable;
import io.github.inf1009_p10_9.managers.SettingsManager;

// polls held keys each frame and moves the player entity using the current keybindings
public class Keyboard implements IInputListens {

    private IMovementCalculatable movementCalculatable;
    private IInputKeyCheckable inputKeyCheckable;
    private IEntityQueryable entityQueryable;

    public Keyboard(IMovementCalculatable movementCalculatable,
                    IInputKeyCheckable inputKeyCheckable,
                    IEntityQueryable entityQueryable) {
        this.movementCalculatable = movementCalculatable;
        this.inputKeyCheckable = inputKeyCheckable;
        this.entityQueryable = entityQueryable;
    }

    @Override
    public void onInput(io.github.inf1009_p10_9.input.Input input) {
        // movement is handled in update() by polling, not by reacting to events
    }

    // called every frame, finds the player and applies movement for any held movement keys
    public void update() {
        com.badlogic.gdx.utils.Array<Entity> entities = entityQueryable.getEntities();

        for (Entity entity : entities) {
            if (entity.getClass().getSimpleName().equals("Player")) {

                SettingsManager settings = SettingsManager.getInstance();

                if (inputKeyCheckable.isKeyPressed(settings.getKeybind("MOVE_UP"))) {
                    movementCalculatable.move(entity, 0);
                }

                if (inputKeyCheckable.isKeyPressed(settings.getKeybind("MOVE_DOWN"))) {
                    movementCalculatable.move(entity, 1);
                }

                if (inputKeyCheckable.isKeyPressed(settings.getKeybind("MOVE_LEFT"))) {
                    movementCalculatable.move(entity, 2);
                }

                if (inputKeyCheckable.isKeyPressed(settings.getKeybind("MOVE_RIGHT"))) {
                    movementCalculatable.move(entity, 3);
                }

                /*
                // old hardcoded key checks, replaced by keybinding lookup above
                if (inputKeyCheckable.isKeyPressed(Input.Keys.W) || inputKeyCheckable.isKeyPressed(Input.Keys.UP)) {
                    movementCalculatable.move(entity, 0);
                }
                if (inputKeyCheckable.isKeyPressed(Input.Keys.S) || inputKeyCheckable.isKeyPressed(Input.Keys.DOWN)) {
                    movementCalculatable.move(entity, 1);
                }
                if (inputKeyCheckable.isKeyPressed(Input.Keys.A) || inputKeyCheckable.isKeyPressed(Input.Keys.LEFT)) {
                    movementCalculatable.move(entity, 2);
                }
                if (inputKeyCheckable.isKeyPressed(Input.Keys.D) || inputKeyCheckable.isKeyPressed(Input.Keys.RIGHT)) {
                    movementCalculatable.move(entity, 3);
                }
                */

                // action keys placeholder
                //if (inputKeyCheckable.isKeyPressed(Input.Keys.SPACE)) {
                //    // add action logic here
                //}
            }
        }
    }
}
