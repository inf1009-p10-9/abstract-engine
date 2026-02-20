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

public class MidScene extends Scene {
    private TextLabel titleLabel;
    private TextLabel instructionLabel;
    private boolean spacePressed = false;
    private float sceneLoadTime = 0;

    private IInputKeyCheckable inputKeyCheckable;
    private IMovementCalculatable movementCalculatable;
    private IMovementStrategyRegisterable movementStrategyRegisterable;
    private ISceneSwitchable sceneSwitchable;
    private ISFXPlayable sfxPlayable;

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
        // Create scene title
        titleLabel = new TextLabel("MID SCENE", 330, 580);
        titleLabel.setColor(Color.CYAN);
        addUI(titleLabel);

        // Create instruction text
        instructionLabel = new TextLabel("Press SPACE to go to End Scene", 220, 20);
        instructionLabel.setColor(Color.YELLOW);
        addUI(instructionLabel);

        Entity[] entities = {
            // Player / Enemy
            new Player(400, 300, sfxPlayable),
            new Enemy(500, 300, sfxPlayable),
            // Walls
            new Wall(0, 568, 800, 32),
            new Wall(0, 0, 800, 32),
            new Wall(0, 0, 32, 600),
            new Wall(768, 0, 32, 600),
            // Obstacles
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

        // Only accept input after 0.2 seconds (prevents accidental double-press)
        if (sceneLoadTime < 0.2f) {
            return;
        }

        aiUpdateTimer += deltaTime;
        if (aiUpdateTimer >= AI_UPDATE_INTERVAL) {
            aiUpdateTimer = 0;

            Array<Entity> entities = entityRegisterable.getEntities();

            // FIND THE PLAYER FIRST
            Entity player = null;
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    player = entity;
                    break;
                }
            }

            // IF PLAYER FOUND, MAKE ENEMIES FLEE FROM PLAYER
            if (player != null) {
                Vector2 playerPos = player.getPosition();

                for (Entity entity : entities) {
                    if (entity instanceof Enemy) {
                        // Get the Enemy's AI movement strategy
                        IMovementStrategy strategy = movementStrategyRegisterable.getMovementStrategy("Enemy");

                        if (strategy instanceof AIMovement) {
                            // constantly set player's position as target to flee from:
                            ((AIMovement)strategy).setTargetPosition(playerPos);
                        }

                        //Move the enemy (will flee from target)
                        movementCalculatable.move(entity, 0);
                    }
                }
            }
        }

        // Press SPACE to go to end scene
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
