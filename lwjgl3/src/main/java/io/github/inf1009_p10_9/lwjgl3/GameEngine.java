package io.github.inf1009_p10_9.lwjgl3;
import com.badlogic.gdx.Gdx;

public final class GameEngine {
	private static GameEngine instance;
	private final SceneManager sceneManager;
    private final MovementManager movementManager;

	private boolean running;
	private final int TARGET_FPS;
	private final long OPTIMAL_TIME;

	private GameEngine() throws Exception {
		this.TARGET_FPS = 60;
		this.OPTIMAL_TIME = 1_000_000_000L;
		this.sceneManager = SceneManager.getInstance();
		this.running = false;

        //Movement Manager
        this.movementManager = new MovementManager();
        movementManager.addStrategy("player", new PlayerMovementStrategy());
        movementManager.addStrategy("ai", new GenericAIMovementStrategy());
	}

	public static GameEngine getInstance() {
		if (instance == null) {
			instance = new GameEngine();
		}
		return instance;
	}

	public void initialize() {
		running = true;
		sceneManager.initialize();
	}

	public void gameLoop() {
		if (!running) return;
		sceneManager.update();
	}

	public void stop() {
		running = false;
	}

	public SceneManager getSceneManager() { return sceneManager; }
}
