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

    public void stopBackgroundMusic() {
        bgManager.stopMusic();
    }

    public void playSoundEffect(String soundFile) {
        sfxManager.playSound(soundFile);
    }

    public void setBackgroundColor(float r, float g, float b) {
        bgR = r;
        bgG = g;
        bgB = b;
    }

    // clears the screen, then draws all renderables and ui sorted by z-index.
    // shapes are drawn first, then textures and text on top.
    public void render() {
        if (batch == null || shapeRenderer == null) {
            return;
        }

        Gdx.gl.glClearColor(bgR, bgG, bgB, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // merge entities and ui into one list and sort by z-index
        Array<IRenderable> allRenderables = new Array<>();
        allRenderables.addAll(renderables);
        allRenderables.addAll(uiElements);
        allRenderables.sort((a, b) -> Integer.compare(a.getZIndex(), b.getZIndex()));

        // first pass: shapes (walls, colored rectangles)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (IRenderable renderable : allRenderables) {
            renderable.renderShapes(shapeRenderer);
        }
        shapeRenderer.end();

        // second pass: textures and text drawn on top of shapes
        batch.begin();
        for (IRenderable renderable : allRenderables) {
            renderable.render(batch);
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
