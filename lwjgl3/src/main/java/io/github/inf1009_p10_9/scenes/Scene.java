package io.github.inf1009_p10_9.scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.UIElement;


public abstract class Scene implements Screen {
    protected String name;
    protected Array<Entity> entities;
    protected Array<UIElement> uiElements;
    protected boolean loaded;
    private ICollidableUnregisterable collidableUnregistrable;
    private IRenderUnregisterable renderUnregistrable;
    private IMusicPlayable musicPlayable;

    public Scene(String name,
                 ICollidableUnregisterable collidableUnregistrable,
                 IRenderUnregisterable renderUnregistrable,
                 IMusicPlayable musicPlayable) {
        this.name = name;
        this.entities = new Array<>();
        this.uiElements = new Array<>();
        this.loaded = false;

        this.collidableUnregistrable = collidableUnregistrable;
        this.renderUnregistrable = renderUnregistrable;
        this.musicPlayable = musicPlayable;
    }

    public void load() {
        if (!loaded) {
            loadEntities();

            // Register all entities with managers
            for (Entity entity : entities) {
                GameContext.getEntityManager().addEntity(entity);
            }

            // Register all UI elements
            for (UIElement uiElement : uiElements) {
                GameContext.getOutputManager().displayUI(uiElement);
            }

            //GameContext.getOutputManager().playMusic("music/Super Mario Bros. medley.mp3-");

            loaded = true;
        }
    }

    public void unload() {
        if (loaded) {
            System.out.println(">>> UNLOADING: " + name + " (Entities: " + entities.size + ", UI: " + uiElements.size + ")");

            // Unregister all entities from ALL managers
            for (Entity entity : entities) {
                GameContext.getEntityManager().removeEntity(entity);

                // If entity is renderable, unregister from OutputManager
                if (entity instanceof IRenderable) {
                    renderUnregistrable.unregisterRenderable(
                        (io.github.inf1009_p10_9.interfaces.IRenderable) entity);
                }

                // If entity is collidable, unregister from CollisionManager
                if (entity instanceof ICollidable) {
                    collidableUnregistrable.unregisterCollidable( (ICollidable) entity);
                }
            }

            // Unregister all UI elements
            for (UIElement uiElement : uiElements) {
                GameContext.getOutputManager().removeUI(uiElement);
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

    public Array<Entity> getEntities() {
        return entities;
    }

    protected void addEntity(Entity entity) {
        entities.add(entity);
    }


    protected void addUI(UIElement uiElement) {
        uiElements.add(uiElement);
    }

//    protected void registerRenderable(IRenderable renderable) {
//        GameContext.getOutputManager().registerRenderable(renderable);
//    }
//
//    protected void registerCollidable(ICollidable collidable) {
//        GameContext.getCollisionManager().registerCollidable(collidable);
//    }

    // LibGDX Screen interface methods
    @Override
    public void show() {
        load();

        // Start background music for this scene
        musicPlayable.playMusic("music/Super Mario Bros. medley.mp3");
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
        GameContext.getOutputManager().getBGManager().stopMusic();
        unload();
    }

    @Override
    public void dispose() {
        unload();
    }
}
