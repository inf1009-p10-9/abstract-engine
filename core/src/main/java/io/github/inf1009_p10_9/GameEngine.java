package io.github.inf1009_p10_9;

import com.badlogic.gdx.Gdx;

public final class GameEngine {
	private static GameEngine instance;
	private final SceneManager sceneManager;
	
	private boolean running;
	private final int TARGET_FPS;
	private final long OPTIMAL_TIME;
	
	private GameEngine() {
		this.TARGET_FPS = 60;
		this.OPTIMAL_TIME = 1_000_000_000L;
		this.sceneManager = SceneManager.getInstance();
		this.running = false;
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
