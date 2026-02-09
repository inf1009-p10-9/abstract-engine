package io.github.inf1009_p10_9.lwjgl3;
import java.util.HashMap;

public class KeyboardInput {
	private HashMap<Integer, Boolean> keyStates;

    public KeyboardInput() {
        keyStates = new HashMap<>();
    }
    
    public boolean isKeyPressed(int keycode) {
        return keyStates.getOrDefault(keycode, false);
    }

    public void keyPressed(int keycode) {
        keyStates.put(keycode, true);
    }

    public void keyReleased(int keycode) {
        keyStates.put(keycode, false);
    }

}
