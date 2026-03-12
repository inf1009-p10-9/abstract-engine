package io.github.inf1009_p10_9.input;

// represents a single input event, capturing the key involved and whether it was pressed or released
public class Input {
    public int keyCode;
    public String type; // "KEY_DOWN", "KEY_UP", or "KEY_TYPED"

    public Input() {
    }

    public Input(int keyCode, String type) {
        this.keyCode = keyCode;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Input{keyCode=" + keyCode + ", type='" + type + "'}";
    }
}
