package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.utils.ObjectMap;
import io.github.inf1009_p10_9.scenes.Scene;

public class SceneManager {
    private static SceneManager instance;
    
    private ObjectMap<String, Scene> scenes;
    private Scene currentScene;
    
    private SceneManager() {
        scenes = new ObjectMap<>();
    }
    
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }
    
    public void initialize() {
        scenes.clear();
        currentScene = null;
    }
    
    public void addScene(Scene scene) {
        scenes.put(scene.getName(), scene);
    }
    
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
    
    public void update() {
        if (currentScene != null) {
            currentScene.update();
        }
    }
    
    public boolean hasScene(String sceneName) {
        return scenes.containsKey(sceneName);
    }
}
