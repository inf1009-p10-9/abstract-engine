package io.github.inf1009_p10_9.audio;

import com.badlogic.gdx.audio.Music;
import io.github.inf1009_p10_9.interfaces.IMusicPlayable;

// manages background music playback, one track at a time
public class BGManager implements IMusicPlayable {
    private String currentMusic;
    private float volume;
    private Speaker speaker;
    private Music musicInstance;

    public BGManager(Speaker speaker) {
        this.speaker = speaker;
        this.volume = 0.5f;
    }

    // stops any currently playing track, then loads and starts the new one on loop
    public void playMusic(String musicFile) {
        if (musicInstance != null) {
            stopMusic();
        }

        musicInstance = speaker.loadMusic(musicFile);
        if (musicInstance != null) {
            musicInstance.setVolume(volume);
            musicInstance.setLooping(true);
            musicInstance.play();
            currentMusic = musicFile;
        }
    }

    // stops and disposes the current track
    public void stopMusic() {
        if (musicInstance != null) {
            musicInstance.stop();
            musicInstance.dispose();
            musicInstance = null;
        }
        currentMusic = null;
    }

    // clamps volume to 0-1 and applies it to the current track if one is playing
    public void setVolume(float volume) {
        this.volume = Math.max(0f, Math.min(1f, volume));
        if (musicInstance != null) {
            musicInstance.setVolume(this.volume);
        }
    }


//    public String getCurrentMusic() {
//        return currentMusic;
//    }
//
//    public void pause() {
//        if (musicInstance != null && musicInstance.isPlaying()) {
//            musicInstance.pause();
//        }
//    }
//
//    public void resume() {
//        if (musicInstance != null && !musicInstance.isPlaying()) {
//            musicInstance.play();
//        }
//    }
}
