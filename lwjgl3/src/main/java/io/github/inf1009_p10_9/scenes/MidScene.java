package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.entities.Player;
import io.github.inf1009_p10_9.entities.Enemy;
import io.github.inf1009_p10_9.entities.Wall;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.TextLabel;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.movement.AIMovement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;

// a transitional scene with a player, enemies, and walls, used between gameplay sections
public class MidScene extends Scene {

    // ui elements
    private TextLabel titleLabel;
    private TextLabel instructionLabel;

    // input state
    private boolean spacePressed = false;
    private float sceneLoadTime = 0;

    // dependencies
    private IInputKeyCheckable inputKeyCheckable;
    private IMovementCalculatable movementCalculatable;
    private IMovementStrategyRegisterable movementStrategyRegisterable;
    private ISceneSwitchable sceneSwitchable;
    private ISFXPlayable sfxPlayable;

    // how often the ai recalculates enemy movement, in seconds
    private float aiUpdateTimer = 0;
    private static final float AI_UPDATE_INTERVAL = 0.2f;

    public MidScene(IEntityRegisterable entityRegisterable,
                    IUIDisplayable uiDisplayable,
                    ICollidableRegisterable collidableRegisterable,
                    IRenderRegisterable renderRegisterable,
                    IMusicPlayable musicPlayable,

                    IInputKeyCheckable inputKeyCheckable,
                    IMovementCalculatable movementCalculatable,
                    IMovementStrategyRegisterable movementStrategyRegisterable,
                    ISceneSwitchable sceneSwitchable,
                    ISFXPlayable sfxPlayable) {
        super("MidScene",
              entityRegisterable,
              uiDisplayable,
              collidableRegisterable,
              renderRegisterable,
              musicPlayable);
        this.inputKeyCheckable = inputKeyCheckable;
        this.movementCalculatable = movementCalculatable;
        this.movementStrategyRegisterable = movementStrategyRegisterable;
        this.sceneSwitchable = sceneSwitchable;
        this.sfxPlayable = sfxPlayable;
    }

    @Override
    protected void loadEntities() {
        titleLabel = new TextLabel("MID SCENE", 330, 580);
        titleLabel.setColor(Color.CYAN);
        addUI(titleLabel);

        instructionLabel = new TextLabel("Press SPACE to go to End Scene", 220, 20);
        instructionLabel.setColor(Color.YELLOW);
        addUI(instructionLabel);

        // player, enemy, boundary walls, and a few interior obstacles
        Entity[] entities = {
            new Player(400, 300, sfxPlayable),
            new Enemy(500, 300, sfxPlayable),
            new Wall(0, 568, 800, 32),
            new Wall(0, 0, 800, 32),
            new Wall(0, 0, 32, 600),
            new Wall(768, 0, 32, 600),
            new Wall(200, 200, 100, 32),
            new Wall(500, 400, 64, 64),
            new Wall(300, 450, 150, 32)
        };

        for (Entity entity : entities) {
            addEntity(entity);
            renderRegisterable.registerRenderable(entity);
            collidableRegisterable.registerCollidable(entity);
        }

        System.out.println("MidScene loaded - Player and Walls only");
    }

    @Override
    public void load() {
        super.load();
        sceneLoadTime = 0;
    }

    @Override
    public void update() {
        super.update();
        float deltaTime = Gdx.graphics.getDeltaTime();
        sceneLoadTime += deltaTime;

        // ignore input briefly after loading to avoid accidental presses
        if (sceneLoadTime < 0.2f) {
            return;
        }

        // periodically update enemy ai targeting
        aiUpdateTimer += deltaTime;
        if (aiUpdateTimer >= AI_UPDATE_INTERVAL) {
            aiUpdateTimer = 0;

            Array<Entity> entities = entityRegisterable.getEntities();

            // find the player in the entity list
            Entity player = null;
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    player = entity;
                    break;
                }
            }

            // if the player exists, make all enemies flee from their position
            if (player != null) {
                Vector2 playerPos = player.getPosition();

                for (Entity entity : entities) {
                    if (entity instanceof Enemy) {
                        IMovementStrategy strategy = movementStrategyRegisterable.getMovementStrategy("Enemy");

                        if (strategy instanceof AIMovement) {
                            ((AIMovement)strategy).setTargetPosition(playerPos);
                        }

                        movementCalculatable.move(entity, 0);
                    }
                }
            }
        }

        // press space to skip to the end scene
        if (inputKeyCheckable.isKeyPressed(Keys.SPACE)) {
            if (!spacePressed) {
                spacePressed = true;
                System.out.println("Going to EndScene...");
                sceneSwitchable.switchScene("EndScene");
            }
        } else {
            spacePressed = false;
        }
    }
}
