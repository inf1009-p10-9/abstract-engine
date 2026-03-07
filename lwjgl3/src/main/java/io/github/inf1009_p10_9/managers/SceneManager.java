package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.ObjectMap;

import io.github.inf1009_p10_9.interfaces.IManager;
import io.github.inf1009_p10_9.interfaces.ISceneSwitchable;
import io.github.inf1009_p10_9.scenes.Scene;

public class SceneManager implements IManager, ISceneSwitchable {
    private static SceneManager instance;

    private ObjectMap<String, Scene> scenes = new ObjectMap<>();
    private Scene currentScene;

    private SceneManager() {}

    public synchronized static SceneManager getInstance() {
        if (instance == null)
            instance = new SceneManager();
        return instance;
    }

    @Override
    public void initialize() {
        clear();
    }

    @Override
    public void update() {
        if (currentScene != null) {
            currentScene.update();
        }
    }

    @Override
    public void clear() {
        scenes.clear();
        currentScene = null;
    }

    public void addScene(Scene scene) {
        scenes.put(scene.getName(), scene);
    }

    @Override
    public void switchScene(String sceneName) {
        Scene newScene = scenes.get(sceneName);

        if (newScene == null) {
            System.err.println("Scene not found: " + sceneName);
            return;
        }

        // Unload current scene
        if (currentScene != null) {
            currentScene.unload();
        }

        // Load new scene
        currentScene = newScene;
        currentScene.load();
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public boolean hasScene(String sceneName) {
        return scenes.containsKey(sceneName);
    }
}
