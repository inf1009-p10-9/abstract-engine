package io.github.inf1009_p10_9.engine.interfaces;
// allows scenes to check whether a key is currently held down
public interface IInputKeyCheckable {
    boolean isKeyPressed(int keyCode);
}
