package io.github.inf1009_p10_9.audio;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.LongMap;

public class SFXManager {
    private LongMap<String> activeSounds; // Maps sound IDs to sound file names
    private float volume;
    private Speaker speaker;
    
    public SFXManager(Speaker speaker) {
        this.speaker = speaker;
        this.activeSounds = new LongMap<>();
        this.volume = 0.7f;
    }
    
    public void playSound(String soundFile) {
        Sound sound = speaker.loadSound(soundFile);
        if (sound != null) {
            long soundId = sound.play(volume);
            activeSounds.put(soundId, soundFile);
        }
    }
    
    public void stopSound(String soundId) {
        try {
            long id = Long.parseLong(soundId);
            if (activeSounds.containsKey(id)) {
                String soundFile = activeSounds.get(id);
                Sound sound = speaker.loadSound(soundFile);
                if (sound != null) {
                    sound.stop(id);
                }
                activeSounds.remove(id);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid sound ID: " + soundId);
        }
    }
    
    public void setVolume(float volume) {
        this.volume = Math.max(0f, Math.min(1f, volume)); // Clamp between 0 and 1
    }
    
    public void stopAllSounds() {
        activeSounds.clear();
    }
}
