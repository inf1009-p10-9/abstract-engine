package io.github.inf1009_p10_9.engine.interfaces;

// controls sound effect playback
public interface ISFXPlayable {
    void playSound(String soundFile);

	void setVolume(float sfxVolume);
}
