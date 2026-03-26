package io.github.inf1009_p10_9.engine.input;

import io.github.inf1009_p10_9.game.entities.Player;
import io.github.inf1009_p10_9.engine.interfaces.IEntityQueryable;
import io.github.inf1009_p10_9.engine.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.engine.interfaces.IInputListens;
import io.github.inf1009_p10_9.engine.interfaces.IMovementCalculatable;
import io.github.inf1009_p10_9.game.interfaces.ISettingsKBRetrievable;
import io.github.inf1009_p10_9.game.managers.SettingsManager;




// polls held keys each frame and moves the player entity using the current keybindings
public class Keyboard implements IInputListens {

    private IMovementCalculatable movementCalculatable;
    private IInputKeyCheckable inputKeyCheckable;
    private IEntityQueryable entityQueryable;
    private ISettingsKBRetrievable settingsKBRetrievable;

    public Keyboard(IMovementCalculatable movementCalculatable,
                    IInputKeyCheckable inputKeyCheckable,
                    IEntityQueryable entityQueryable,
                    ISettingsKBRetrievable settingsKBRetrievable) {
        this.movementCalculatable = movementCalculatable;
        this.inputKeyCheckable = inputKeyCheckable;
        this.entityQueryable = entityQueryable;
        this.settingsKBRetrievable = settingsKBRetrievable;
    }

    @Override
    public void onInput(Input input) {
        // movement is handled in update() by polling, not by reacting to events
    }

    // called every frame, finds the player and applies movement for any held movement keys
    public void update() {
        Player player = entityQueryable.getFirstOfType(Player.class);
        if (player == null) return;


        if (inputKeyCheckable.isKeyPressed(settingsKBRetrievable.getKeybind("MOVE_UP"))) {
            movementCalculatable.move(player, 0);
        }

        if (inputKeyCheckable.isKeyPressed(settingsKBRetrievable.getKeybind("MOVE_DOWN"))) {
            movementCalculatable.move(player, 1);
        }

        if (inputKeyCheckable.isKeyPressed(settingsKBRetrievable.getKeybind("MOVE_LEFT"))) {
            movementCalculatable.move(player, 2);
        }

        if (inputKeyCheckable.isKeyPressed(settingsKBRetrievable.getKeybind("MOVE_RIGHT"))) {
            movementCalculatable.move(player, 3);
        }
    }
 }
