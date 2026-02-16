package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import io.github.inf1009_p10_9.input.*;
import io.github.inf1009_p10_9.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.interfaces.IInputListens;


public class InputManager implements InputProcessor, IInputKeyCheckable {
    private static InputManager instance;

    private IntMap<Boolean> keyStates;
    private Array<IInputListens> listeners;
    private Array<IInputListens> peripherals;
    private Input currentInput;

    private InputManager() {
        keyStates = new IntMap<>();
        listeners = new Array<>();
        peripherals = new Array<>();
    }

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    public void initialize() {
        keyStates.clear();
        listeners.clear();
        peripherals.clear();
        Gdx.input.setInputProcessor(this);
    }

    public void registerPeripheral(IInputListens peripheral) {
        if (!peripherals.contains(peripheral, true)) {
            peripherals.add(peripheral);
        }
    }

    public boolean isKeyPressed(int keyCode) {
        return keyStates.get(keyCode, false);
    }

    public void processInput() {
        if (currentInput != null) {
            // Notify all peripherals
            for (IInputListens peripheral : peripherals) {
                peripheral.onInput(currentInput);
            }

            // Notify all listeners
            for (IInputListens listener : listeners) {
                listener.onInput(currentInput);
            }

            currentInput = null;
        }
    }

    public void update() {
        processInput();
     // Update all peripherals that need continuous checking
        for (IInputListens peripheral : peripherals) {
            if (peripheral instanceof Keyboard) {
                ((Keyboard) peripheral).update();
            }
        }
    }

    public void clear() {
        keyStates.clear();
        listeners.clear();
        peripherals.clear();
    }

    // InputProcessor methods
    @Override
    public boolean keyDown(int keycode) {
        keyStates.put(keycode, true);
        currentInput = new Input(keycode, "KEY_DOWN");
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyStates.put(keycode, false);
        currentInput = new Input(keycode, "KEY_UP");
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
}
