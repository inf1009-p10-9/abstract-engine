package io.github.inf1009_p10_9.input;

import com.badlogic.gdx.utils.Array;

import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.entities.Player;
import io.github.inf1009_p10_9.input.Input;
import io.github.inf1009_p10_9.interfaces.IEntityQueryable;
import io.github.inf1009_p10_9.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.interfaces.IInputListens;
import io.github.inf1009_p10_9.interfaces.IMovementCalculatable;
import io.github.inf1009_p10_9.managers.SettingsManager;
import io.github.inf1009_p10_9.entities.Player;

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
    public void onInput(Input input) {
        // movement is handled in update() by polling, not by reacting to events
    }

    // called every frame, finds the player and applies movement for any held movement keys
    public void update() {
        Player player = entityQueryable.getFirstOfType(Player.class);
        if (player == null) return;

        SettingsManager settings = SettingsManager.getInstance();

        if (inputKeyCheckable.isKeyPressed(settings.getKeybind("MOVE_UP"))) {
            movementCalculatable.move(player, 0);
        }

        if (inputKeyCheckable.isKeyPressed(settings.getKeybind("MOVE_DOWN"))) {
            movementCalculatable.move(player, 1);
        }

        if (inputKeyCheckable.isKeyPressed(settings.getKeybind("MOVE_LEFT"))) {
            movementCalculatable.move(player, 2);
        }

        if (inputKeyCheckable.isKeyPressed(settings.getKeybind("MOVE_RIGHT"))) {
            movementCalculatable.move(player, 3);
        }
    }
 }
