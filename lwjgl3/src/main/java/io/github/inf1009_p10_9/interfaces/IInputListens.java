package io.github.inf1009_p10_9.interfaces;

import io.github.inf1009_p10_9.input.Input;

// implemented by anything that should be notified when an input event occurs
public interface IInputListens {
    void onInput(Input input);
}
