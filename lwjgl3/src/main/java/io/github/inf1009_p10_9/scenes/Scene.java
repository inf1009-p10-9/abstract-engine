package io.github.inf1009_p10_9.scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.UIElement;

public abstract class Scene implements Screen {
    private String name;
    private Array<Entity> entities;
    private Array<UIElement> uiElements;
    private boolean loaded;
    
    // Interface dependencies (NOT concrete managers!)
    protected final IEntityRegisterable entityRegisterable;
    protected final IUIDisplayable uiDisplayable;
    protected final ICollidableRegisterable collidableRegisterable;  // Has both register & unregister
    protected final IRenderRegisterable renderRegisterable;          // Has both register & unregister
    protected final IMusicPlayable musicPlayable;

    public Scene(String name,
                 IEntityRegisterable entityRegisterable,
                 IUIDisplayable uiDisplayable,
                 ICollidableRegisterable collidableRegisterable,
                 IRenderRegisterable renderRegisterable,
                 IMusicPlayable musicPlayable) {
        this.name = name;
        this.entities = new Array<>();
        this.uiElements = new Array<>();
        this.loaded = false;

        this.entityRegisterable = entityRegisterable;
        this.uiDisplayable = uiDisplayable;
        this.collidableRegisterable = collidableRegisterable;
        this.renderRegisterable = renderRegisterable;
        this.musicPlayable = musicPlayable;
    }

    public void load() {
        if (!loaded) {
            loadEntities();

            // Register all entities with managers
            for (Entity entity : entities) {
                entityRegisterable.addEntity(entity);  // Uses interface
            }

            // Register all UI elements
            for (UIElement uiElement : uiElements) {
                uiDisplayable.displayUI(uiElement);  // Uses interface
            }

            loaded = true;
        }
    }

    public void unload() {
        if (loaded) {
            System.out.println(">>> UNLOADING: " + name + " (Entities: " + entities.size + ", UI: " + uiElements.size + ")");

            // Unregister all entities from ALL managers
            for (Entity entity : entities) {
                entityRegisterable.removeEntity(entity);  // Uses interface

                // If entity is renderable, unregister from OutputManager
                if (entity instanceof IRenderable) {
                    renderRegisterable.unregisterRenderable((IRenderable) entity);  // Same interface!
                }

                // If entity is collidable, unregister from CollisionManager
                if (entity instanceof ICollidable) {
                    collidableRegisterable.unregisterCollidable((ICollidable) entity);  // Same interface!
                }
            }

            // Unregister all UI elements
            for (UIElement uiElement : uiElements) {
                uiDisplayable.removeUI(uiElement);  // Uses interface
            }

            entities.clear();
            uiElements.clear();
            loaded = false;

            System.out.println(">>> UNLOAD COMPLETE");
        }
    }

    public void update() {
        // Scene-specific update logic here
    }

    protected abstract void loadEntities();

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

    // LibGDX Screen interface methods
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
