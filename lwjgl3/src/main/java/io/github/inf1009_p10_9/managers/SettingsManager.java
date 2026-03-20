package io.github.inf1009_p10_9.managers;

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

    // scenery chosen on the level select screen, defaults to City
    private String selectedScenery = "City";


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
        keybinds.put("MOVE_UP", Keys.W);
        keybinds.put("MOVE_DOWN", Keys.S);
        keybinds.put("MOVE_LEFT", Keys.A);
        keybinds.put("MOVE_RIGHT", Keys.D);

        keybinds.put("MENU_UP", Keys.UP);
        keybinds.put("MENU_DOWN", Keys.DOWN);
        keybinds.put("MENU_LEFT", Keys.LEFT);
        keybinds.put("MENU_RIGHT", Keys.RIGHT);

        keybinds.put("CONFIRM", Keys.ENTER);
        keybinds.put("BACK", Keys.ESCAPE);
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

    public void rebindKey(String action, int keycode) {
        keybinds.put(action, keycode);
    }

    public ObjectMap<String, Integer> getAllKeybinds() {
        return keybinds;
    }
    // scenery selection persists for the whole session until overwritten
    public String getSelectedScenery() { return selectedScenery; }
 
    public void setSelectedScenery(String scenery) {
        this.selectedScenery = scenery;
    }
}
