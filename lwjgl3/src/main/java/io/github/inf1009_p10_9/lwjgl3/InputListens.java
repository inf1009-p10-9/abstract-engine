package io.github.inf1009_p10_9.lwjgl3;

public interface InputListens {
	void onKeyPressed(int keycode);

    void onKeyReleased(int keycode);

    void onMouseClicked(int button);
}
