package io.github.inf1009_p10_9.game.managers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.ObjectMap;
import io.github.inf1009_p10_9.engine.interfaces.ISettingsControllable;
import io.github.inf1009_p10_9.game.interfaces.IScenerySelect;
import io.github.inf1009_p10_9.game.interfaces.ISettingsKBRetrievable;


// singleton that stores player preferences, volume levels and keybindings, across the session
public class SettingsManager implements ISettingsControllable,
                                        IScenerySelect,
                                        ISettingsKBRetrievable{
    private static SettingsManager instance;

    // volume levels, clamped to 0.0 to 1.0 on write
    private float musicVolume = 0.5f;
    private float sfxVolume = 0.7f;

    // keybindings keyed by action name, e.g. "MOVE_UP", "CONFIRM"
    private final ObjectMap<String, Integer> keybinds = new ObjectMap<>();
    private final Map<Integer, String> keybindsInverse = new HashMap<>();

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

    // scenery selection persists for the whole session until overwritten
    public String getSelectedScenery() {
        return selectedScenery;
    }

    public void setSelectedScenery(String scenery) {
        this.selectedScenery = scenery;
    }
}
