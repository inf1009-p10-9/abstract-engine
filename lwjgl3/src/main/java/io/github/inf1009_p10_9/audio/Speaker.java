package io.github.inf1009_p10_9.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

public class Speaker {
    private Music currentMusic;
    private ObjectMap<String, Sound> loadedSounds;
    
    public Speaker() {
        loadedSounds = new ObjectMap<>();
    }
    
    public void playAudio(String audioFile) {
        // This is generic, specific implementations in BGManager/SFXManager
    }
    
    public Music loadMusic(String musicFile) {
        try {
            return Gdx.audio.newMusic(Gdx.files.internal(musicFile));
        } catch (Exception e) {
            System.err.println("Failed to load music: " + musicFile);
            return null;
        }
    }
    
    public Sound loadSound(String soundFile) {
        if (loadedSounds.containsKey(soundFile)) {
            return loadedSounds.get(soundFile);
        }
        
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(soundFile));
            loadedSounds.put(soundFile, sound);
            return sound;
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + soundFile);
            return null;
        }
    }
    
    public void stopAudio() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
    }
    
    public void setVolume(float volume) {
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }
    
    public void dispose() {
        if (currentMusic != null) {
            currentMusic.dispose();
        }
        
        for (Sound sound : loadedSounds.values()) {
            sound.dispose();
        }
        loadedSounds.clear();
    }
}
