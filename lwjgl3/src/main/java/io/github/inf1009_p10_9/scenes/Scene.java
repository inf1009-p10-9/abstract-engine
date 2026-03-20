package io.github.inf1009_p10_9.scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.UIElement;

// base class for all scenes, handles loading, unloading, and the lifecycle hooks LibGDX expects
public abstract class Scene implements Screen {
    private String name;
    private Array<Entity> entities = new Array<>();
    private Array<UIElement> uiElements = new Array<>();
    private boolean loaded = false;

    // interface dependencies passed in from outside, never tied to concrete implementations
    protected final IEntityRegisterable entityRegisterable;
    protected final IUIDisplayable uiDisplayable;
    protected final ICollidableRegisterable collidableRegisterable;
    protected final IRenderRegisterable renderRegisterable;
    protected final IMusicPlayable musicPlayable;

    public Scene(String name,
                 IEntityRegisterable entityRegisterable,
                 IUIDisplayable uiDisplayable,
                 ICollidableRegisterable collidableRegisterable,
                 IRenderRegisterable renderRegisterable,
                 IMusicPlayable musicPlayable) {
        this.name = name;
        this.entityRegisterable = entityRegisterable;
        this.uiDisplayable = uiDisplayable;
        this.collidableRegisterable = collidableRegisterable;
        this.renderRegisterable = renderRegisterable;
        this.musicPlayable = musicPlayable;
    }

    // registers all entities and ui elements on first load, skips if already loaded
    public void load() {
        if (!loaded) {
            loadEntities();

            for (Entity entity : entities) {
                entityRegisterable.addEntity(entity);
            }

            for (UIElement uiElement : uiElements) {
                uiDisplayable.displayUI(uiElement);
            }

            loaded = true;
        }
    }

    // removes all entities and ui elements from their respective managers and clears local lists
    public void unload() {
        if (loaded) {
            System.out.println(">>> UNLOADING: " + name + " (Entities: " + entities.size + ", UI: " + uiElements.size + ")");

            for (Entity entity : entities) {
                entityRegisterable.removeEntity(entity);

                // also unregister from render and collision systems if applicable
                if (entity instanceof IRenderable) {
                    renderRegisterable.unregisterRenderable(entity);
                }

                if (entity instanceof ICollidable) {
                    collidableRegisterable.unregisterCollidable(entity);
                }
            }

            for (UIElement uiElement : uiElements) {
                uiDisplayable.removeUI(uiElement);
            }

            entities.clear();
            uiElements.clear();
            loaded = false;

            System.out.println(">>> UNLOAD COMPLETE");
        }
    }

    // override in subclasses to add per-frame logic
    public void update() {
    }

    // subclasses must implement this to define what entities and ui elements belong to the scene
    protected abstract void loadEntities();

    // getters and helpers
    public String getName() {
        return name;
    }

    public boolean isLoaded() {
        return loaded;
    }

    protected Array<Entity> getEntities() {
        return entities;
    }

    protected void addEntity(Entity entity) {
        entities.add(entity);
    }

    protected void addUI(UIElement uiElement) {
        uiElements.add(uiElement);
    }

    // LibGDX screen lifecycle, mapped to our own load/update/unload methods
    @Override
    public void show() {
        load();
    }

    @Override
    public void render(float delta) {
        update();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        musicPlayable.stopMusic();
        unload();
    }

    @Override
    public void dispose() {
        unload();
    }
}