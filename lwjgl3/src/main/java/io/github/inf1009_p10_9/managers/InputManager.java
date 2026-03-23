package io.github.inf1009_p10_9.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import io.github.inf1009_p10_9.input.*;
import io.github.inf1009_p10_9.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.interfaces.IInputListens;
import io.github.inf1009_p10_9.interfaces.IKeyBindEvent;
import io.github.inf1009_p10_9.interfaces.IKeyPressConsumable;
import io.github.inf1009_p10_9.interfaces.IManager;
import io.github.inf1009_p10_9.interfaces.IKeyBindObserverTarget;
import io.github.inf1009_p10_9.interfaces.IKeyBindObserves;

// singleton that handles keyboard input, tracks key states, and notifies registered listeners each frame
public class InputManager implements IManager,
                                     InputProcessor,
                                     IInputKeyCheckable,
                                     IKeyPressConsumable,
                                     IKeyBindObserverTarget {
    private static InputManager instance;

    // current state of every key by keycode
    private IntMap<Boolean> keyStates = new IntMap<>();
    private Array<IInputListens> listeners = new Array<>();
    private Array<IInputListens> peripherals = new Array<>();
    private Input currentInput;

    /**
     * holds the most recently pressed key so scenes can consume it for rebinding
     */
    private int lastJustPressedKey = -1;

    private final Map<IKeyBindEvent, List<IKeyBindObserves>> keyBindObservers = new ConcurrentHashMap<>();

    private InputManager() {}

    public static InputManager getInstance() {
        if (instance == null)
            instance = new InputManager();
        return instance;
    }



    ///////////////////////
    // Manager Lifecycle //
    ///////////////////////

    /**
     * Registers this manager as the LibGDX input processor
     */
    @Override
    public void initialize() {
        clear();
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Dispatches the current input event and polls any peripherals that need continuous updates
     */
    @Override
    public void update() {
        processInput();
        for (IInputListens peripheral : peripherals) {
            if (peripheral instanceof Keyboard) {
                ((Keyboard) peripheral).update();
            }
        }
    }

    @Override
    public void clear() {
        keyStates.clear();
        listeners.clear();
        peripherals.clear();
        keyBindObservers.clear();
    }



    ////////////////////////
    // Standalone Methods //
    ////////////////////////

    public void registerPeripheral(IInputListens peripheral) {
        if (!peripherals.contains(peripheral, true)) {
            peripherals.add(peripheral);
        }
    }

    public boolean isKeyPressed(int keyCode) {
        return keyStates.get(keyCode, false);
    }

    // forwards the current input event to all peripherals and listeners, then clears it
    public void processInput() {
        if (currentInput != null) {
            for (IInputListens peripheral : peripherals) {
                peripheral.onInput(currentInput);
            }

            for (IInputListens listener : listeners) {
                listener.onInput(currentInput);
            }

            currentInput = null;
        }
    }

    // returns the last pressed key and resets it, used by settings scene for rebinding
    public int consumeLastJustPressedKey() {
        int key = lastJustPressedKey;
        lastJustPressedKey = -1;
        return key;
    }

    @Override
    public void registerKeyBindObserver(IKeyBindObserves keyBindObserver, IKeyBindEvent keyBindEvent) {
        System.out.printf("[registerKeyBindObserver] Registering keybind observer \"%s\" for keybind event \"%s\" (hashCode: %d)\n",
                          keyBindObserver.getClass().getName(),
                          keyBindEvent.toString(),
                          keyBindEvent.hashCode());
        List<IKeyBindObserves> observers = keyBindObservers.get(keyBindEvent);
        if (observers == null) {
            observers = new ArrayList<>();
            keyBindObservers.put(keyBindEvent, observers);
        }
        observers.add(keyBindObserver);
    }

    @Override
    public void deregisterAllKeyBindObservers() {
        keyBindObservers.clear();
    }


    @Override
    public void deregisterKeyBindObserver(IKeyBindObserves keyBindObserver) {
        System.out.println("[deregisterKeyBindObserver] Deregistering observer \"" + keyBindObserver.getClass().getCanonicalName() + "\"...");
        for (Entry<IKeyBindEvent,List<IKeyBindObserves>> entry : keyBindObservers.entrySet())
            if (entry.getValue().remove(keyBindObserver))
                System.out.println("[deregisterKeyBindObserver] Deregistered keybind observer \"" + keyBindObserver.getClass().getCanonicalName() + "\"from keybidn event " + entry.getKey());
        System.out.println("[deregisterKeyBindObserver] Remaining observers: " + keyBindObservers.size());
    }

    @Override
    public void deregisterKeyBindObserver(IKeyBindObserves keyBindObserver, IKeyBindEvent keyBindEvent) {
        List<IKeyBindObserves> observers = keyBindObservers.get(keyBindEvent);
        if (observers != null)
            observers.remove(keyBindObserver);
    }

    private void notifyKeyBindObservers(IKeyBindEvent keyBindEvent) {
        System.out.printf("[notifyKeyBindObservers] Notifying keybind observers for keybind event \"%s\" (hashCode: %d)\n",
                          keyBindEvent.toString(),
                          keyBindEvent.hashCode());
        List<IKeyBindObserves> observers = keyBindObservers.get(keyBindEvent);
        if (observers != null) {
            for (IKeyBindObserves observer : observers) {
                System.out.printf("[notifyKeyBindObservers] Notifying observer \"%s\"...\n",
                                   observer.getClass().getName());
                observer.observeKeyBindEvent(keyBindEvent);
            }
        } else {
            System.out.println("[notifyKeyBindObservers] No matching observers found.");
        }
    }



    /////////////////////////////////////
    // LibGDX InputProcessor callbacks //
    /////////////////////////////////////

    @Override
    public boolean keyDown(int keycode) {
        keyStates.put(keycode, true);
        currentInput = new Input(keycode, "KEY_DOWN");
        lastJustPressedKey = keycode;

        SettingsManager settings = SettingsManager.getInstance();
        String keybind = settings.getKeybind(keycode);

        if (keybind != null) {
            notifyKeyBindObservers(new KeyBindEvent(keybind, "KEY_DOWN"));
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyStates.put(keycode, false);
        currentInput = new Input(keycode, "KEY_UP");

        SettingsManager settings = SettingsManager.getInstance();
        String keybind = settings.getKeybind(keycode);

        if (keybind != null) {
            notifyKeyBindObservers(new KeyBindEvent(keybind, "KEY_UP"));
        }

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
        return false;
    }
}
