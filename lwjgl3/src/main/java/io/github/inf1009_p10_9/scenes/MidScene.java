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
    private final ISceneSwitchable sceneSwitchable;
    private TextLabel titleLabel;
    private TextLabel instructionLabel;
    private boolean spacePressed = false;
    private float sceneLoadTime = 0;

    private float aiUpdateTimer = 0;
    private static final float AI_UPDATE_INTERVAL = 0.2f;

    private IMovementCalculatable movementCalculatable;
    private IMovementStrategyRegisterable movementStrategyRegisterable;
    private IInputKeyCheckable inputKeyCheckable;
    private ISFXPlayable sfxPlayable;
    

    public MidScene(ISceneSwitchable sceneSwitchable,
                IEntityRegisterable entityRegisterable,
                IUIDisplayable uiDisplayable,
                IMovementCalculatable movementCalculatable,
                IMovementStrategyRegisterable movementStrategyRegisterable,
                IInputKeyCheckable inputKeyCheckable,
                ICollidableRegisterable collidableRegisterable,
                ISFXPlayable sfxPlayable,
                IRenderRegisterable renderRegisterable,
                IMusicPlayable musicPlayable) {
    super("MidScene", entityRegisterable, uiDisplayable, collidableRegisterable, renderRegisterable, musicPlayable);
        this.movementCalculatable = movementCalculatable;
        this.movementStrategyRegisterable = movementStrategyRegisterable;
        this.inputKeyCheckable = inputKeyCheckable;
        this.sfxPlayable = sfxPlayable;
        this.sceneSwitchable = sceneSwitchable;
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

        // Player
        Player player = new Player(400, 300, sfxPlayable);
        addEntity(player);
        renderRegisterable.registerRenderable(player);
        collidableRegisterable.registerCollidable(player);


        // Enemy 1 - top right
        Enemy enemy1 = new Enemy(500, 300, sfxPlayable);
        addEntity(enemy1);
        renderRegisterable.registerRenderable(enemy1);
        collidableRegisterable.registerCollidable(enemy1);

        // Create walls around the edge of the screen
        // Top wall

        Wall[] walls = {
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

        for (Wall wall : walls) {
            addEntity(wall);
            renderRegisterable.registerRenderable(wall);
            collidableRegisterable.registerCollidable(wall);
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

            Array<Entity> entities = ((IEntityQueryable)entityRegisterable).getEntities()

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
