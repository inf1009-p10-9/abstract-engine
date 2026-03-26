package io.github.inf1009_p10_9.engine.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

// low-level audio loader shared by BGManager and SFXManager.
// sounds are cached by file path to avoid reloading the same file multiple times.
public class Speaker {
    private Music currentMusic;

    // sounds are cached by file path so the same file is only loaded once
    private ObjectMap<String, Sound> loadedSounds;

    public Speaker() {
        loadedSounds = new ObjectMap<>();
    }

    // generic hook for audio playback, actual logic is handled by BGManager and SFXManager
    public void playAudio(String audioFile) {
    }

    // loads and returns a new Music instance each call, caller is responsible for disposing it
    public Music loadMusic(String musicFile) {
        try {
            return Gdx.audio.newMusic(Gdx.files.internal(musicFile));
        } catch (Exception e) {
            System.err.println("Failed to load music: " + musicFile);
            return null;
        }
    }

    // returns a cached Sound if already loaded, otherwise loads and caches it
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

    // disposes all loaded audio resources, should be called on shutdown
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
