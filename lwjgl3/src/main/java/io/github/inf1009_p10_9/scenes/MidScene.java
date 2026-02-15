package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.GameContext;
import io.github.inf1009_p10_9.entities.Player;
import io.github.inf1009_p10_9.entities.Enemy;
import io.github.inf1009_p10_9.entities.Wall;
import io.github.inf1009_p10_9.ui.TextLabel;
import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.interfaces.IMovementStrategy;
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
    
    public MidScene() {
        super("MidScene");
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
        Player player = new Player(400, 300);
        addEntity(player);
        GameContext.getOutputManager().registerRenderable(player);
        GameContext.getCollisionManager().registerCollidable(player);
        
        
        // Enemy 1 - top right
        Enemy enemy1 = new Enemy(500, 300);
        addEntity(enemy1);
        GameContext.getOutputManager().registerRenderable(enemy1);
        GameContext.getCollisionManager().registerCollidable(enemy1);
        
        // Create walls around the edge of the screen
        // Top wall
        Wall topWall = new Wall(0, 568, 800, 32);
        addEntity(topWall);
        GameContext.getOutputManager().registerRenderable(topWall);
        GameContext.getCollisionManager().registerCollidable(topWall);
        
        // Bottom wall
        Wall bottomWall = new Wall(0, 0, 800, 32);
        addEntity(bottomWall);
        GameContext.getOutputManager().registerRenderable(bottomWall);
        GameContext.getCollisionManager().registerCollidable(bottomWall);
        
        // Left wall
        Wall leftWall = new Wall(0, 0, 32, 600);
        addEntity(leftWall);
        GameContext.getOutputManager().registerRenderable(leftWall);
        GameContext.getCollisionManager().registerCollidable(leftWall);
        
        // Right wall
        Wall rightWall = new Wall(768, 0, 32, 600);
        addEntity(rightWall);
        GameContext.getOutputManager().registerRenderable(rightWall);
        GameContext.getCollisionManager().registerCollidable(rightWall);
        
        // Create some obstacles in the middle
        Wall obstacle1 = new Wall(200, 200, 100, 32);
        addEntity(obstacle1);
        GameContext.getOutputManager().registerRenderable(obstacle1);
        GameContext.getCollisionManager().registerCollidable(obstacle1);
        
        Wall obstacle2 = new Wall(500, 400, 64, 64);
        addEntity(obstacle2);
        GameContext.getOutputManager().registerRenderable(obstacle2);
        GameContext.getCollisionManager().registerCollidable(obstacle2);
        
        Wall obstacle3 = new Wall(300, 450, 150, 32);
        addEntity(obstacle3);
        GameContext.getOutputManager().registerRenderable(obstacle3);
        GameContext.getCollisionManager().registerCollidable(obstacle3);
        
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
            
            // ⭐ FIND THE PLAYER FIRST
            Entity player = null;
            for (Entity entity : entities) {
                if (entity.getClass().getSimpleName().equals("Player")) {
                    player = entity;
                    break;
                }
            }
            
            // ⭐ IF PLAYER FOUND, MAKE ENEMIES FLEE FROM PLAYER
            if (player != null) {
                Vector2 playerPos = player.getPosition();
                
                for (Entity entity : entities) {
                    if (entity.getClass().getSimpleName().equals("Enemy")) {
                        // Get the Enemy's AI movement strategy
                        IMovementStrategy strategy = GameContext.getMovementManager().getMovementStrategy("Enemy");
                        
                        if (strategy instanceof AIMovement) {
                            AIMovement aiMovement = (AIMovement) strategy;
                            
                            // ⭐ SET PLAYER POSITION AS TARGET TO FLEE FROM
                            aiMovement.setTargetPosition(playerPos);
                        }
                        
                        // Move the enemy (will flee from target)
                        GameContext.getMovementManager().move(entity, 0);
                    }
                }
            }
        }
        
        // Press SPACE to go to end scene
        if (GameContext.getInputManager().isKeyPressed(Keys.SPACE)) {
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