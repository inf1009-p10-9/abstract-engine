package io.github.inf1009_p10_9.interfaces;

public interface IMusicPlayable {
    void playMusic(String musicFile);
    void stopMusic();
    void setVolume(float volume);
    float getVolume();
}
