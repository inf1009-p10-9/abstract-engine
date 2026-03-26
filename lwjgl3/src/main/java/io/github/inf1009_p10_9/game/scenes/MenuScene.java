package io.github.inf1009_p10_9.game.scenes;

import io.github.inf1009_p10_9.engine.core.Scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

import io.github.inf1009_p10_9.engine.interfaces.ICollidableRegisterable;
import io.github.inf1009_p10_9.engine.interfaces.IEntityRegisterable;
import io.github.inf1009_p10_9.engine.interfaces.IInputKeyCheckable;
import io.github.inf1009_p10_9.engine.interfaces.IMusicPlayable;
import io.github.inf1009_p10_9.engine.interfaces.IRenderRegisterable;
import io.github.inf1009_p10_9.engine.interfaces.ISceneSwitchable;
import io.github.inf1009_p10_9.engine.interfaces.IUIDisplayable;
import io.github.inf1009_p10_9.game.ui.MenuButtonElement;
import io.github.inf1009_p10_9.game.ui.TextLabel;
import io.github.inf1009_p10_9.game.interfaces.ISettingsKBRetrievable;
import io.github.inf1009_p10_9.game.managers.SettingsManager;

// shared base scene for menu-style screens with arrow navigation and highlighted buttons
public abstract class MenuScene extends Scene {

    // shared menu ui
    protected TextLabel[] menuOptionLabels;
    protected TextLabel[] arrowIndicators;
    protected MenuButtonElement[] menuButtons;

    // shared menu navigation state
    protected int highlightedIndex = 0;
    protected boolean upDownPressed = false;
    protected boolean enterPressed = false;
    protected boolean escPressed = false;
    protected float sceneLoadTime = 0f;

    // shared menu animation timer
    protected float arrowPulseTimer = 0f;

    // shared menu colors
    protected static final Color NORMAL_COLOR = Color.WHITE;
    protected static final Color HIGHLIGHTED_COLOR = new Color(0.08f, 0.08f, 0.08f, 1f);
    protected static final Color ARROW_COLOR = new Color(1f, 0.9f, 0.1f, 1f);

    // shared scene dependencies
    protected final IInputKeyCheckable inputKeyCheckable;
    protected final ISceneSwitchable sceneSwitchable;
    protected final ISettingsKBRetrievable settingsKBRetrievable;

    public MenuScene(String name,
                     IEntityRegisterable entityRegisterable,
                     IUIDisplayable uiDisplayable,
                     ICollidableRegisterable collidableRegisterable,
                     IRenderRegisterable renderRegisterable,
                     IMusicPlayable musicPlayable,
                     IInputKeyCheckable inputKeyCheckable,
                     ISceneSwitchable sceneSwitchable,
                     ISettingsKBRetrievable settingsKBRetrievable) {
        super(name,
              entityRegisterable,
              uiDisplayable,
              collidableRegisterable,
              renderRegisterable,
              musicPlayable);
        this.inputKeyCheckable = inputKeyCheckable;
        this.sceneSwitchable = sceneSwitchable;
        this.settingsKBRetrievable = settingsKBRetrievable;
    }

    // resets common menu navigation state each time the scene is opened
    @Override
    public void load() {
        super.load();
        sceneLoadTime = 0f;
        upDownPressed = false;
        enterPressed = false;
        escPressed = false;
        arrowPulseTimer = 0f;
    }

    // shared input logic for up/down menu navigation and enter confirm
    protected void updateMenuNavigation(int optionCount) {
        float delta = Gdx.graphics.getDeltaTime();
        sceneLoadTime += delta;

        if (sceneLoadTime < 0.2f) {
            return;
        }

        boolean upKeyPressed   = inputKeyCheckable.isKeyPressed(settingsKBRetrievable.getKeybind("MENU_UP")) || inputKeyCheckable.isKeyPressed(settingsKBRetrievable.getKeybind("MOVE_UP"));
        boolean downKeyPressed = inputKeyCheckable.isKeyPressed(settingsKBRetrievable.getKeybind("MENU_DOWN")) || inputKeyCheckable.isKeyPressed(settingsKBRetrievable.getKeybind("MOVE_DOWN"));


        if (upKeyPressed || downKeyPressed) {
            System.out.println("KEY PRESSED: up=" + upKeyPressed + " down=" + downKeyPressed + " upDownPressed=" + upDownPressed + " t=" + sceneLoadTime);
        }

        // wait for all keys to be fully released before accepting any navigation input
        if (upDownPressed) {
            if (!upKeyPressed && !downKeyPressed) {
                upDownPressed = false;
            }
            return;
        }

        if (upKeyPressed || downKeyPressed) {
            upDownPressed = true;

            if (downKeyPressed) {
                highlightedIndex++;
            } else {
                highlightedIndex--;
            }

            if (highlightedIndex < 0) {
                highlightedIndex = 0;
            }
            if (highlightedIndex >= optionCount) {
                highlightedIndex = optionCount - 1;
            }

            updateHighlight();
        }

        // confirm selection on enter
        if (inputKeyCheckable.isKeyPressed(Keys.ENTER)) {
            if (!enterPressed) {
                enterPressed = true;
                handleMenuSelection();
            }
        } else {
            enterPressed = false;
        }
    }

    // refreshes colors, arrow visibility, and button highlight state to match the current selection
    protected void updateHighlight() {
        float arrowBaseX = getArrowBaseX();

        for (int i = 0; i < menuButtons.length; i++) {
            if (i == highlightedIndex) {
                menuButtons[i].setHighlighted(true);
                menuOptionLabels[i].setColor(HIGHLIGHTED_COLOR);
                arrowIndicators[i].setVisible(true);
                arrowPulseTimer = 0f;
            } else {
                menuButtons[i].setHighlighted(false);
                menuOptionLabels[i].setColor(NORMAL_COLOR);
                arrowIndicators[i].setVisible(false);
                arrowIndicators[i].setPosition(arrowBaseX, arrowIndicators[i].getY());
            }
        }
    }

    // adds a small pulsing motion to the visible selection arrow
    protected void animateArrow(float delta) {
        float arrowBaseX = getArrowBaseX();

        arrowPulseTimer += delta;
        float arrowOffset = (float) Math.sin(arrowPulseTimer * 6f) * 5f;

        for (int i = 0; i < arrowIndicators.length; i++) {
            if (i == highlightedIndex) {
                arrowIndicators[i].setPosition(arrowBaseX + arrowOffset, arrowIndicators[i].getY());
            }
        }
    }

    // each subclass tells the base class where the left arrow should sit
    protected abstract float getArrowBaseX();

    // each subclass handles its own option action
    protected abstract void handleMenuSelection();
}
