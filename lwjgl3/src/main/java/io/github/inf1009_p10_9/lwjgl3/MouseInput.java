package io.github.inf1009_p10_9.lwjgl3;
public class MouseInput {

    private boolean clicked;

    public MouseInput() {
        clicked = false;
    }

    public void mouseClicked() {
        clicked = true;
    }

    //returns true once per click, then resets to false
    public boolean isClicked() {
        boolean wasClicked = clicked;
        clicked = false; // reset after read
        return wasClicked;
    }
}
