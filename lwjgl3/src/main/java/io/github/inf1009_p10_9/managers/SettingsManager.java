package io.github.inf1009_p10_9.managers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.ObjectMap;
import io.github.inf1009_p10_9.interfaces.ISettingsControllable;

// singleton that stores player preferences, volume levels and keybindings, across the session
public class SettingsManager implements ISettingsControllable {
    private static SettingsManager instance;

    // volume levels, clamped to 0.0 to 1.0 on write
    private float musicVolume = 0.5f;
    private float sfxVolume = 0.7f;

    // keybindings keyed by action name, e.g. "MOVE_UP", "CONFIRM"
    private final ObjectMap<String, Integer> keybinds = new ObjectMap<>();
    private final Map<Integer, String> keybindsInverse = new HashMap<>();

    private SettingsManager() {
        setDefaultKeybinds();
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    // sets up the default keyboard layout used on first launch
    private void setDefaultKeybinds() {
        rebindKey("MOVE_UP", Keys.W);
        rebindKey("MOVE_DOWN", Keys.S);
        rebindKey("MOVE_LEFT", Keys.A);
        rebindKey("MOVE_RIGHT", Keys.D);

        rebindKey("MENU_UP", Keys.UP);
        rebindKey("MENU_DOWN", Keys.DOWN);
        rebindKey("MENU_LEFT", Keys.LEFT);
        rebindKey("MENU_RIGHT", Keys.RIGHT);

        rebindKey("CONFIRM", Keys.ENTER);
        rebindKey("BACK", Keys.ESCAPE);
    }

    // getters and setters for volume, setters clamp the value to a valid range
    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = Math.max(0f, Math.min(1f, musicVolume));
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(float sfxVolume) {
        this.sfxVolume = Math.max(0f, Math.min(1f, sfxVolume));
    }

    // returns the keycode for the given action, or Keys.UNKNOWN if not found
    public int getKeybind(String action) {
        return keybinds.get(action, Keys.UNKNOWN);
    }

    public String getKeybind(int keycode) {
        return keybindsInverse.get(keycode);
    }

    public void rebindKey(String action, int keycode) {
        keybinds.put(action, keycode);
        keybindsInverse.put(keycode, action);
    }

    public ObjectMap<String, Integer> getAllKeybinds() {
        return keybinds;
    }
}
