package io.github.inf1009_p10_9.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;

public class AudioTestGame extends ApplicationAdapter {

    private OutputManager outputManager;

    @Override
    public void create() {
        System.out.println("=== Audio test started ===");

        outputManager = new OutputManager();

        outputManager.getSpeaker().loadSound(
                "jump", "sounds/jump.mp3");

        outputManager.getSpeaker().playMusic(
                "music/Super Mario Bros. medley.mp3");

        outputManager.getSpeaker().setVolume(0.5f);

        outputManager.getSpeaker().playSound("jump");
    }

    @Override
    public void dispose() {
        outputManager.getSpeaker().stopMusic();
    }
}
