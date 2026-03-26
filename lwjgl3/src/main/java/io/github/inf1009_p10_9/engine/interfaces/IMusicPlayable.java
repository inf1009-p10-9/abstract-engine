package io.github.inf1009_p10_9.engine.interfaces;

// controls background music playback
public interface IMusicPlayable {
    void playMusic(String musicFile);
    void stopMusic();
    void setVolume(float volume);
}
