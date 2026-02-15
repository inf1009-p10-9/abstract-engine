package io.github.inf1009_p10_9.input;

public class Input {
    public int keyCode;
    public String type; // "KEY_DOWN", "KEY_UP", "KEY_TYPED"
    
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
