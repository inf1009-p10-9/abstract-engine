package io.github.inf1009_p10_9.lwjgl3;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

public class Speaker {

    private Music bgm;
    private HashMap<String, Sound> sounds;
    private float volume;

    public Speaker() {
        sounds = new HashMap<>();
        volume = 1.0f;
    }

    public void playMusic(String path) {
        stopMusic();
        bgm = Gdx.audio.newMusic(Gdx.files.internal(path));
        bgm.setLooping(true);
        bgm.setVolume(volume);
        bgm.play();
    }

    public void stopMusic() {
        if (bgm != null) {
            bgm.stop();
            bgm.dispose();
            bgm = null;
        }
    }

    public void loadSound(String id, String path) {
        sounds.put(id, Gdx.audio.newSound(Gdx.files.internal(path)));
    }

    public void playSound(String id) {
        Sound s = sounds.get(id);
        if (s != null) {
            s.play(volume);
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (bgm != null) bgm.setVolume(volume);
    }
}
