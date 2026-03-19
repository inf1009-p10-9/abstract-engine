package io.github.inf1009_p10_9.audio;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.LongMap;
import io.github.inf1009_p10_9.interfaces.ISFXPlayable;
import io.github.inf1009_p10_9.interfaces.ISFXVolume;

// manages sound effect playback, tracking active sounds by their LibGDX sound id
public class SFXManager implements ISFXPlayable, ISFXVolume {

    // maps sound ids returned by play() back to their file names
    private LongMap<String> activeSounds;
    private float volume;
    private Speaker speaker;

    public SFXManager(Speaker speaker) {
        this.speaker = speaker;
        this.activeSounds = new LongMap<>();
        this.volume = 0.7f;
    }

    // loads the sound via Speaker (cached) and plays it at the current volume
    public void playSound(String soundFile) {
        Sound sound = speaker.loadSound(soundFile);
        if (sound != null) {
            long soundId = sound.play(volume);
            activeSounds.put(soundId, soundFile);
        }
    }

//    public void stopSound(String soundId) {
//        try {
//            long id = Long.parseLong(soundId);
//            if (activeSounds.containsKey(id)) {
//                String soundFile = activeSounds.get(id);
//                Sound sound = speaker.loadSound(soundFile);
//                if (sound != null) {
//                    sound.stop(id);
//                }
//                activeSounds.remove(id);
//            }
//        } catch (NumberFormatException e) {
//            System.err.println("Invalid sound ID: " + soundId);
//        }
//    }

    // clamps volume to 0-1, applies to future sounds only
    public void setVolume(float volume) {
        this.volume = Math.max(0f, Math.min(1f, volume));
    }

//    public void stopAllSounds() {
//        activeSounds.clear();
//    }
}
