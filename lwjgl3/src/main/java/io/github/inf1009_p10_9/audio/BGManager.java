package io.github.inf1009_p10_9.audio;

import com.badlogic.gdx.audio.Music;
import io.github.inf1009_p10_9.interfaces.IMusicPlayable;

public class BGManager implements IMusicPlayable {
    private String currentMusic;
    private float volume;
    private Speaker speaker;
    private Music musicInstance;

    public BGManager(Speaker speaker) {
        this.speaker = speaker;
        this.volume = 0.5f;
    }

    /*public void playMusic(String musicFile) {
        System.out.println("Trying to play music: " + musicFile);
        System.out.println("Playing: " + musicFile);
    }*/



    public void playMusic(String musicFile) {
        // Stop current music if playing
        if (musicInstance != null) {
            stopMusic();
        }

        // Load and play new music
        musicInstance = speaker.loadMusic(musicFile);
        if (musicInstance != null) {
            musicInstance.setVolume(volume);
            musicInstance.setLooping(true);
            musicInstance.play();
            currentMusic = musicFile;
        }
    }

    public void stopMusic() {
        if (musicInstance != null) {
            musicInstance.stop();
            musicInstance.dispose();
            musicInstance = null;
        }
        currentMusic = null;
    }

//    public void setVolume(float volume) {
//        this.volume = Math.max(0f, Math.min(1f, volume)); // Clamp between 0 and 1
//        if (musicInstance != null) {
//            musicInstance.setVolume(this.volume);
//        }
//    }
//
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
