package io.github.inf1009_p10_9.engine.input;

import io.github.inf1009_p10_9.engine.interfaces.IKeyBindEvent;

public class KeyBindEvent implements IKeyBindEvent {
    private final String keyBind;
    private final String event;

    public KeyBindEvent(String keyBind, String event) {
        this.keyBind = keyBind;
        this.event = event;
    }

    public String getKeyBind() {
        return keyBind;
    }

    public String getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof KeyBindEvent))
            return false;
        KeyBindEvent other = (KeyBindEvent)o;
        boolean keyBindEquals = (keyBind == null && other.keyBind == null ) || (keyBind != null && keyBind.equals(other.keyBind));
        boolean eventEquals = (event == null && other.event == null ) || (event != null && event.equals(other.event));
        return keyBindEquals && eventEquals;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((keyBind == null) ? 0 : keyBind.hashCode());
        result = prime * result + ((event == null) ? 0 : event.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "KeyBindEvent{keyBind=\"" + keyBind + "\", event=\"" + event + "\"}";
    }
}
