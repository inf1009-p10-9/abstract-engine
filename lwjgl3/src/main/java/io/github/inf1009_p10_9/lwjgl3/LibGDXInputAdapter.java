package io.github.inf1009_p10_9.lwjgl3;

import com.badlogic.gdx.InputProcessor;

/**
 * Bridges libGDX input to InputManager.
 * This class is framework glue, NOT engine logic.
 */
public class LibGDXInputAdapter implements InputProcessor {

    private InputManager inputManager;

    public LibGDXInputAdapter(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public boolean keyDown(int keycode) {
        inputManager.keyPressed(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        inputManager.keyReleased(keycode);
        return true;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        inputManager.mouseClicked(button);
        return true;
    }

    // --- Unused but REQUIRED methods ---

    @Override public boolean keyTyped(char c) { return false; }
    @Override public boolean touchUp(int x, int y, int p, int b) { return false; }
    @Override public boolean touchDragged(int x, int y, int p) { return false; }
    @Override public boolean mouseMoved(int x, int y) { return false; }
    @Override public boolean scrolled(float ax, float ay) { return false; }

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
}
