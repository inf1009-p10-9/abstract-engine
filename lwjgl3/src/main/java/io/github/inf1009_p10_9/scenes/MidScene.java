package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.entities.Player;
import io.github.inf1009_p10_9.entities.Enemy;
import io.github.inf1009_p10_9.entities.Wall;
import io.github.inf1009_p10_9.interfaces.*;
import io.github.inf1009_p10_9.ui.TextLabel;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.movement.AIMovement;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;

public class MidScene extends Scene {
    private TextLabel titleLabel;
    private TextLabel instructionLabel;
    private boolean spacePressed = false;
    private float sceneLoadTime = 0;

    private float aiUpdateTimer = 0;
    private static final float AI_UPDATE_INTERVAL = 0.2f;

    private IMovementCalculatable movementCalculatable;
    private IMovementStrategyReturnable movementStrategyReturnable;
    private IInputKeyCheckable inputKeyCheckable;
    private ICollidableRegisterable collidableRegisterable;
    private ISFXPlayable sfxPlayable;
    private IRenderRegisterable renderRegisterable;

    public MidScene(IMovementCalculatable movementCalculatable,
                    IMovementStrategyReturnable movementStrategyReturnable,
                    IInputKeyCheckable inputKeyCheckable,
                    ICollidableUnregisterable collidableUnregisterable,
                    ICollidableRegisterable collidableRegisterable,
                    ISFXPlayable sfxPlayable,
                    IRenderUnregisterable renderUnregisterable,
                    IRenderRegisterable renderRegisterable,
                    IMusicPlayable musicPlayable) {
        super("MidScene", collidableUnregisterable, renderUnregisterable, musicPlayable);
        this.movementCalculatable = movementCalculatable;
        this.movementStrategyReturnable = movementStrategyReturnable;
        this.inputKeyCheckable = inputKeyCheckable;
        this.collidableRegisterable = collidableRegisterable;
        this.sfxPlayable = sfxPlayable;
        this.renderRegisterable = renderRegisterable;
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

        // Create player at center
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
        Wall topWall = new Wall(0, 568, 800, 32);
        addEntity(topWall);
        renderRegisterable.registerRenderable(topWall);
        collidableRegisterable.registerCollidable(topWall);

        // Bottom wall
        Wall bottomWall = new Wall(0, 0, 800, 32);
        addEntity(bottomWall);
        renderRegisterable.registerRenderable(bottomWall);
        collidableRegisterable.registerCollidable(bottomWall);

        // Left wall
        Wall leftWall = new Wall(0, 0, 32, 600);
        addEntity(leftWall);
        renderRegisterable.registerRenderable(leftWall);
        collidableRegisterable.registerCollidable(leftWall);

        // Right wall
        Wall rightWall = new Wall(768, 0, 32, 600);
        addEntity(rightWall);
        renderRegisterable.registerRenderable(rightWall);
        collidableRegisterable.registerCollidable(rightWall);

        // Create some obstacles in the middle
        Wall obstacle1 = new Wall(200, 200, 100, 32);
        addEntity(obstacle1);
        renderRegisterable.registerRenderable(obstacle1);
        collidableRegisterable.registerCollidable(obstacle1);

        Wall obstacle2 = new Wall(500, 400, 64, 64);
        addEntity(obstacle2);
        renderRegisterable.registerRenderable(obstacle2);
        collidableRegisterable.registerCollidable(obstacle2);

        Wall obstacle3 = new Wall(300, 450, 150, 32);
        addEntity(obstacle3);
        renderRegisterable.registerRenderable(obstacle3);
        collidableRegisterable.registerCollidable(obstacle3);

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
        float deltaTime = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        sceneLoadTime += deltaTime;

        // Only accept input after 0.2 seconds (prevents accidental double-press)
        if (sceneLoadTime < 0.2f) {
            return;
        }

        aiUpdateTimer += deltaTime;
        if (aiUpdateTimer >= AI_UPDATE_INTERVAL) {
            aiUpdateTimer = 0;

            Array<Entity> entities = GameContext.getEntityManager().getEntities();

            // FIND THE PLAYER FIRST
            Entity player = null;
            for (Entity entity : entities) {
                if (entity.getClass().getSimpleName().equals("Player")) {
                    player = entity;
                    break;
                }
            }

            // IF PLAYER FOUND, MAKE ENEMIES FLEE FROM PLAYER
            if (player != null) {
                Vector2 playerPos = player.getPosition();

                for (Entity entity : entities) {
                    if (entity.getClass().getSimpleName().equals("Enemy")) {
                        // Get the Enemy's AI movement strategy
                        IMovementStrategy strategy = movementStrategyReturnable.getMovementStrategy("Enemy");

                        if (strategy instanceof AIMovement) {
                            AIMovement aiMovement = (AIMovement) strategy;

                            aiMovement.setTargetPosition(playerPos); // constantly set player's position as target to flee from
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
                GameContext.getSceneManager().switchScene("EndScene");
            }
        } else {
            spacePressed = false;
        }
    }
}
