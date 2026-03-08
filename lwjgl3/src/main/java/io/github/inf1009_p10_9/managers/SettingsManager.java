package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.ObjectMap;

public class SettingsManager {
    private static SettingsManager instance;

    private float musicVolume = 0.5f;
    private float sfxVolume = 0.7f;

    private final ObjectMap<String, Integer> keybinds = new ObjectMap<>();

    private SettingsManager() {
        setDefaultKeybinds();
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

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

    public int getKeybind(String action) {
        return keybinds.get(action, Keys.UNKNOWN);
    }

    public void rebindKey(String action, int keycode) {
        keybinds.put(action, keycode);
    }

    public ObjectMap<String, Integer> getAllKeybinds() {
        return keybinds;
    }
}