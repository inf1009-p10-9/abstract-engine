package io.github.inf1009_p10_9.interfaces;

// allows scenes and other components to trigger a scene transition by name
public interface ISceneSwitchable {
    void switchScene(String sceneName);
}
