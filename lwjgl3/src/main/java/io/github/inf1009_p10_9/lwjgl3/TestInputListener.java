package io.github.inf1009_p10_9.lwjgl3;

import com.badlogic.gdx.Input.Keys;

public class TestInputListener implements InputListens {

    @Override
    public void onKeyPressed(int keycode) {
        System.out.println("Key pressed: " + Keys.toString(keycode));
    }

    @Override
    public void onKeyReleased(int keycode) {
        System.out.println("Key released: " + Keys.toString(keycode));
    }

    @Override
    public void onMouseClicked(int button) {
        System.out.println("Mouse clicked: button " + button);
    }
}
