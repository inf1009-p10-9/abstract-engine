package io.github.inf1009_p10_9.lwjgl3;

import java.util.ArrayList;
import java.util.List;

public class InputManager {

    private KeyboardInput keyboard;
    private MouseInput mouse;
    
    // Listeners that react to input
    private List<InputListens> listeners;

    public InputManager() {
        keyboard = new KeyboardInput();
        mouse = new MouseInput();
        listeners = new ArrayList<>();
    }

    public KeyboardInput getKeyboard() {
        return keyboard;
    }

    public MouseInput getMouse() {
        return mouse;
    }
    
    //Listener management

    public void registerListener(InputListens listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unregisterListener(InputListens listener) {
        listeners.remove(listener);
    }
    
    // Entry point from adapter/framework
    public void keyPressed(int keycode) {
        keyboard.keyPressed(keycode);
        for (InputListens listener : listeners) {
            listener.onKeyPressed(keycode);
        }
    }

    // Entry point from adapter/framework
    public void keyReleased(int keycode) {
        keyboard.keyReleased(keycode);
        for (InputListens listener : listeners) {
            listener.onKeyReleased(keycode);
        }
    }

    // Entry point from adapter/framework
    public void mouseClicked(int button) {
        mouse.mouseClicked();
        for (InputListens listener : listeners) {
            listener.onMouseClicked(button);
        }
    }
    
}

    
