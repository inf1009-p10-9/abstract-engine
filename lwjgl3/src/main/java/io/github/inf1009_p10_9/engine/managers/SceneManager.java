package io.github.inf1009_p10_9.engine.managers;

import com.badlogic.gdx.utils.ObjectMap;

import io.github.inf1009_p10_9.engine.interfaces.IManager;
import io.github.inf1009_p10_9.engine.interfaces.ISceneSwitchable;
import io.github.inf1009_p10_9.engine.core.Scene;

// singleton that owns all registered scenes and handles switching between them
public class SceneManager implements IManager, ISceneSwitchable {
    private static SceneManager instance;

    // all scenes keyed by name, e.g. "StartScene", "GameScene"
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

    // unloads the current scene and loads the named one, logs an error if the name is not found
    @Override
    public void switchScene(String sceneName) {
        Scene newScene = scenes.get(sceneName);

        if (newScene == null) {
            System.err.println("Scene not found: " + sceneName);
            return;
        }

        if (currentScene != null) {
            currentScene.unload();
        }

        //InputManager.getInstance().clearKeyStates();

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
