package io.github.inf1009_p10_9.interfaces;

import io.github.inf1009_p10_9.ui.UIElement;

/**
 * Interface for displaying/removing UI elements
 */
public interface IUIDisplayable {
    void displayUI(UIElement uiElement);
    void removeUI(UIElement uiElement);
}
