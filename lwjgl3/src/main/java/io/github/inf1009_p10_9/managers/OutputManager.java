package io.github.inf1009_p10_9.managers;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import io.github.inf1009_p10_9.audio.BGManager;
import io.github.inf1009_p10_9.audio.SFXManager;
import io.github.inf1009_p10_9.audio.Speaker;
import io.github.inf1009_p10_9.interfaces.IManager;
import io.github.inf1009_p10_9.interfaces.IRenderRegisterable;
import io.github.inf1009_p10_9.interfaces.IRenderable;
import io.github.inf1009_p10_9.interfaces.IUIDisplayable;
import io.github.inf1009_p10_9.ui.UIElement;

// singleton that owns the render loop, ui list, and audio managers
public class OutputManager implements IManager,
                                      IRenderRegisterable,
                                      IUIDisplayable {
    private static OutputManager instance;

    // separate lists so entities and ui elements can be tracked independently
    private Array<IRenderable> renderables = new Array<>();
    private Array<UIElement> uiElements = new Array<>();

    // shared LibGDX rendering tools passed down to entities for drawing
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    // audio managers sharing a single speaker
    private BGManager bgManager;
    private SFXManager sfxManager;

    // background clear color, defaults to black
    private float bgR = 0, bgG = 0, bgB = 0;

    private OutputManager() {
        Speaker speaker = new Speaker();
        bgManager = new BGManager(speaker);
        sfxManager = new SFXManager(speaker);
    }

    public static synchronized OutputManager getInstance() {
        if (instance == null)
            instance = new OutputManager();
        return instance;
    }

    // creates the SpriteBatch and ShapeRenderer if they haven't been created yet
    @Override
    public void initialize() {
        renderables.clear();
        uiElements.clear();

        if (batch == null) {
            batch = new SpriteBatch();
        }
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }
    }

    @Override
    public void update() {
        render();
    }

    @Override
    public void clear() {
        renderables.clear();
        uiElements.clear();
        bgManager.stopMusic();
    }

    // releases LibGDX native resources, should be called on shutdown
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }

        clear();
    }

    // registering and unregistering renderables and ui elements
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

    @Override
    public void displayUI(UIElement uiElement) {
        if (!uiElements.contains(uiElement, true)) {
            uiElements.add(uiElement);
        }
    }

    @Override
    public void removeUI(UIElement uiElement) {
        uiElements.removeValue(uiElement, true);
    }

    // audio helpers that delegate to the bg and sfx managers
    public void playBackgroundMusic(String musicFile) {
        bgManager.playMusic(musicFile);
    }

    // clears the screen, then draws all renderables and ui sorted by z-index.
    // shapes are drawn first, then textures and text on top.
    public void render() {
        if (batch == null || shapeRenderer == null) {
            return;
        }

        Gdx.gl.glClearColor(bgR, bgG, bgB, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // merge entities and ui into one list and sort by z-index
        Array<IRenderable> allRenderables = new Array<>();
        allRenderables.addAll(renderables);
        allRenderables.addAll(uiElements);
        allRenderables.sort((a, b) -> Integer.compare(a.getZIndex(), b.getZIndex()));

        // below pause overlay (zIndex < 100)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (IRenderable r : allRenderables) {
            if (r.getZIndex() <= 100) r.renderShapes(shapeRenderer);
        }
        shapeRenderer.end();

        batch.begin();
        for (IRenderable r : allRenderables) {
            if (r.getZIndex() <= 100) r.render(batch);
        }
        batch.end();

        // pause overlay and above (zIndex >= 100)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (IRenderable r : allRenderables) {
            if (r.getZIndex() > 100) r.renderShapes(shapeRenderer);
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        for (IRenderable r : allRenderables) {
            if (r.getZIndex() > 100) r.render(batch);
        }
        batch.end();
    }

    public BGManager getBGManager() {
        return bgManager;
    }

    public SFXManager getSFXManager() {
        return sfxManager;
    }

    // convenience alias for playBackgroundMusic
    public void playMusic(String musicFile) {
        playBackgroundMusic(musicFile);
    }

}
