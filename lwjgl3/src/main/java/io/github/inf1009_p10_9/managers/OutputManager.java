package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import io.github.inf1009_p10_9.audio.BGManager;
import io.github.inf1009_p10_9.audio.SFXManager;
import io.github.inf1009_p10_9.audio.Speaker;
import io.github.inf1009_p10_9.interfaces.IRenderRegisterable;
import io.github.inf1009_p10_9.interfaces.IRenderable;
import io.github.inf1009_p10_9.ui.UIElement;

public class OutputManager implements IRenderRegisterable {
    private static OutputManager instance;

    private Array<IRenderable> renderables;
    private Array<UIElement> uiElements;

    // LibGDX rendering tools - entities will use these directly
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private BGManager bgManager;
    private SFXManager sfxManager;

    private OutputManager() {
        renderables = new Array<>();
        uiElements = new Array<>();

        // Initialize audio managers with a shared speaker
        Speaker speaker = new Speaker();
        bgManager = new BGManager(speaker);
        sfxManager = new SFXManager(speaker);
    }

    public static OutputManager getInstance() {
        if (instance == null) {
            instance = new OutputManager();
        }
        return instance;
    }

    public void initialize() {
        renderables.clear();
        uiElements.clear();

        // Initialize LibGDX rendering tools
        if (batch == null) {
            batch = new SpriteBatch();
        }
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }
    }

    @Override
    public void registerRenderable(IRenderable obj) {
        if (!renderables.contains(obj, true)) {
            renderables.add(obj);
        }
    }

    @Override
    public void unregisterRenderable(IRenderable obj) {
        renderables.removeValue(obj, true);
    }

    public void displayUI(UIElement uiElement) {
        if (!uiElements.contains(uiElement, true)) {
            uiElements.add(uiElement);
        }
    }

    public void removeUI(UIElement uiElement) {
        uiElements.removeValue(uiElement, true);
    }

    public void playBackgroundMusic(String musicFile) {
        bgManager.playMusic(musicFile);
    }

    public void stopBackgroundMusic() {
        bgManager.stopMusic();
    }

    public void playSoundEffect(String soundFile) {
        sfxManager.playSound(soundFile);
    }


    //coordinates rendering but delegates actual drawing to entities
    public void render() {
        if (batch == null || shapeRenderer == null) {
            return;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1); // Black background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Combine all renderables (entities + UI) and sort by z-index
        Array<IRenderable> allRenderables = new Array<>();
        allRenderables.addAll(renderables);
        allRenderables.addAll(uiElements);
        allRenderables.sort((a, b) -> Integer.compare(a.getZIndex(), b.getZIndex()));


        // Render all shapes (walls, colored rectangles) with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (IRenderable renderable : allRenderables) {
            renderable.renderShapes(shapeRenderer);
        }
        shapeRenderer.end();

        // Render all textures/text with SpriteBatch (draw ON TOP of shapes)
        batch.begin();
        for (IRenderable renderable : allRenderables) {
            renderable.render(batch);
        }
        batch.end();
    }



    public void clear() {
        renderables.clear();
        uiElements.clear();
        bgManager.stopMusic();
    }

    public void dispose() {
        // Clean up LibGDX resources
        if (batch != null) {
            batch.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }

        // Clean up audio
        bgManager.stopMusic();

        // Clear collections
        renderables.clear();
        uiElements.clear();
    }

    public BGManager getBGManager() {
        return bgManager;
    }

    public SFXManager getSFXManager() {
        return sfxManager;
    }

    public void playMusic(String musicFile) {
        playBackgroundMusic(musicFile);
    }
}
