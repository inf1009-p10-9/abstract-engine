package io.github.inf1009_p10_9.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class InputTestGame extends ApplicationAdapter {

    @Override
    public void create() {
        System.out.println("🔥 INPUT TEST GAME STARTED 🔥");

        InputManager inputManager = new InputManager();
        inputManager.registerListener(new TestInputListener());

        Gdx.input.setInputProcessor(
                new LibGDXInputAdapter(inputManager)
        );
    }
}

