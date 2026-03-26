package io.github.inf1009_p10_9.game.ui;
import io.github.inf1009_p10_9.engine.core.UIElement;

import com.badlogic.gdx.graphics.Color;

// one complete settings row, grouping the button, labels, and arrow into a single object
public class SettingsRow {

    // row metadata
    private final String optionText;
    private final String bindAction;

    // row ui elements
    private final MenuButtonElement button;
    private final TextLabel optionLabel;
    private final TextLabel valueLabel;
    private final TextLabel arrowLabel;

    public SettingsRow(String optionText,
                       String bindAction,
                       float rowX,
                       float rowY,
                       float rowWidth,
                       float rowHeight,
                       Color rowColor,
                       Color highlightColor,
                       Color normalTextColor,
                       Color valueTextColor,
                       Color arrowColor,
                       FontManager fontManager) {
        this.optionText = optionText;
        this.bindAction = bindAction;

        // row panel
        button = new MenuButtonElement(
            rowX, rowY - rowHeight + 10,
            rowWidth, rowHeight,
            rowColor, highlightColor
        );

        // arrow for the selected row
        float arrowX = rowX - 45;
        arrowLabel = new TextLabel(">>", arrowX, rowY, fontManager.getMediumFont());
        arrowLabel.setColor(arrowColor);
        arrowLabel.setZIndex(200);

        // option text on the left
        optionLabel = new TextLabel(optionText, rowX + 22, rowY, fontManager.getMediumFont());
        optionLabel.setColor(normalTextColor);

        // value text on the right
        valueLabel = new TextLabel("", rowX + rowWidth - 150, rowY, fontManager.getMediumFont());
        valueLabel.setColor(valueTextColor);
    }

    // registers all row ui elements into the scene
    public void addToScene(io.github.inf1009_p10_9.engine.interfaces.IUICollector uiCollector) {
        uiCollector.addUI(button);
        uiCollector.addUI(arrowLabel);
        uiCollector.addUI(optionLabel);
        uiCollector.addUI(valueLabel);
    }

    // updates the visual highlight state of the row
    public void setHighlighted(boolean highlighted,
                               Color normalTextColor,
                               Color highlightedTextColor,
                               Color valueColor,
                               Color valueHighlightedColor,
                               float arrowBaseX) {
        button.setHighlighted(highlighted);

        if (highlighted) {
            optionLabel.setColor(highlightedTextColor);
            valueLabel.setColor(valueHighlightedColor);
            arrowLabel.setVisible(true);
        } else {
            optionLabel.setColor(normalTextColor);
            valueLabel.setColor(valueColor);
            arrowLabel.setVisible(false);
            arrowLabel.setPosition(arrowBaseX, arrowLabel.getY());
        }
    }

    public String getOptionText() {
        return optionText;
    }

    public String getBindAction() {
        return bindAction;
    }

    public MenuButtonElement getButton() {
        return button;
    }

    public TextLabel getOptionLabel() {
        return optionLabel;
    }

    public TextLabel getValueLabel() {
        return valueLabel;
    }

    public TextLabel getArrowLabel() {
        return arrowLabel;
    }
}