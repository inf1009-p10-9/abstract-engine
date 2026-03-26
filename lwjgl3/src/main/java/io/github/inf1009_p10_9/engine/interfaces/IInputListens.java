package io.github.inf1009_p10_9.engine.interfaces;

import io.github.inf1009_p10_9.engine.input.Input;

// implemented by anything that should be notified when an input event occurs
public interface IInputListens {
    void onInput(Input input);
}
