package io.github.inf1009_p10_9.game.ui;
import io.github.inf1009_p10_9.engine.core.UIElement;

import com.badlogic.gdx.Gdx;

import io.github.inf1009_p10_9.engine.interfaces.IUICollector;

// shared decorative background used by menu-style scenes
public class SceneBackdrop {

    // decorative scene elements
    private final BackgroundElement background;
    private final CloudElement[] clouds;
    private final CarElement grassCar;
    private final boolean hasGrassCar;

    public SceneBackdrop(boolean includeGrassCar) {
        float screenHeight = Gdx.graphics.getHeight();

        // background layer
        background = new BackgroundElement();

        // drifting clouds reused across scenes
        clouds = new CloudElement[3];
        clouds[0] = new CloudElement(100, screenHeight * 0.88f, 160, 50, 24f);
        clouds[1] = new CloudElement(500, screenHeight * 0.93f, 210, 62, 18f);
        clouds[2] = new CloudElement(960, screenHeight * 0.87f, 150, 46, 28f);

        // optional grass car at the bottom of the screen
        hasGrassCar = includeGrassCar;
        if (includeGrassCar) {
            float grassY = screenHeight * 0.16f;
            grassCar = new CarElement(
                100, grassY - 26, 110f,
                0, 133, 64, 26,
                128, 52
            );
        } else {
            grassCar = null;
        }
    }

    // registers the background elements into the scene ui layer
    public void addToScene(IUICollector uiCollector) {
        uiCollector.addUI(background);

        for (CloudElement cloud : clouds) {
            uiCollector.addUI(cloud);
        }

        if (hasGrassCar && grassCar != null) {
            uiCollector.addUI(grassCar);
        }
    }

    // updates decorative animation, with optional speed multiplier
    public void update(float delta, float cloudMultiplier, float carMultiplier) {
        for (CloudElement cloud : clouds) {
            cloud.update(delta);
            if (cloudMultiplier != 1f) {
                cloud.update(delta * (cloudMultiplier - 1f));
            }
        }

        if (hasGrassCar && grassCar != null) {
            grassCar.update(delta);
            if (carMultiplier != 1f) {
                grassCar.update(delta * (carMultiplier - 1f));
            }
        }
    }

    public CarElement getGrassCar() {
        return grassCar;
    }

    public CloudElement[] getClouds() {
        return clouds;
    }
}