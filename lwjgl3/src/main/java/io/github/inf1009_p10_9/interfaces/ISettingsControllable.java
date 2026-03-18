package io.github.inf1009_p10_9.interfaces;

public interface ISettingsControllable {
    float getMusicVolume();
    void setMusicVolume(float volume);

    float getSfxVolume();
    void setSfxVolume(float volume);

    int getKeybind(String action);
    void rebindKey(String action, int keycode);
}