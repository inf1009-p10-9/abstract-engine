package io.github.inf1009_p10_9.interfaces;

import io.github.inf1009_p10_9.ui.BackgroundElement;
import io.github.inf1009_p10_9.ui.CloudElement;
import io.github.inf1009_p10_9.ui.UIElement;

// adds and removes ui elements from the output manager
public interface IUIDisplayable {
    void displayUI(UIElement uiElement);
    void removeUI(UIElement uiElement);
    void addUI(UIElement uiElement);
  
}
