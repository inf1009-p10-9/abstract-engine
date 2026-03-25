package io.github.inf1009_p10_9.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import io.github.inf1009_p10_9.entities.Gate;
import io.github.inf1009_p10_9.entities.Player;
import io.github.inf1009_p10_9.entities.Rock;
import io.github.inf1009_p10_9.interfaces.ISFXPlayable;
import io.github.inf1009_p10_9.ui.LivesElement;

public class RockObstacleController {

    // Number of gameplay rocks on the road
    private static final int ROCK_COUNT = 3;

    // Bigger size so the rocks are more visible
    private static final float ROCK_SIZE = 68f;

    // Prevents the player from losing lives every frame while touching a rock
    private static final float HIT_COOLDOWN_DURATION = 1.0f;

    private final Array<Rock> rocks;
    private final Player player;
    private final LivesElement livesElement;
    private final ISFXPlayable sfxPlayable;

    private final float roadLeftEdge;
    private final float roadRightEdge;

    // Used to read the gate's real current Y so rocks never spawn above it
    private Gate gate;

    private float hitCooldown = 0f;

    // Used to detect when the gate resets back to the top of the screen
    private float previousGateY = -1f;

    // After getting hit once, the player must leave the rock before taking damage again
    private boolean playerMustExitRockBeforeNextHit = false;

    public RockObstacleController(
            Player player,
            LivesElement livesElement,
            ISFXPlayable sfxPlayable,
            float roadLeftEdge,
            float roadRightEdge
    ) {
        this.player = player;
        this.livesElement = livesElement;
        this.sfxPlayable = sfxPlayable;
        this.roadLeftEdge = roadLeftEdge;
        this.roadRightEdge = roadRightEdge;
        this.rocks = new Array<>();
    }

    public void createRocks() {
        rocks.clear();

        for (int i = 0; i < ROCK_COUNT; i++) {
            Rock rock = new Rock(0, 0, ROCK_SIZE);
            rocks.add(rock);
        }

        repositionRocks();
    }

    public Array<Rock> getRocks() {
        return rocks;
    }

    // Call this after the gate is created in GameScene
    public void setGate(Gate gate) {
        this.gate = gate;
    }

    public void update() {
        if (hitCooldown > 0f) {
            hitCooldown -= Gdx.graphics.getDeltaTime();
        }

        // When the gate resets it jumps back to screenHeight (top of screen).
        // Detect that upward jump and reposition rocks so they stay below the gate.
        if (gate != null) {
            float currentGateY = gate.getPosition().y;
            if (previousGateY >= 0f && currentGateY > previousGateY + 50f) {
                repositionRocks();
            }
            previousGateY = currentGateY;
        }

        checkCollision();
    }

    public void repositionRocks() {
        float horizontalPadding = 35f;
        float minX = roadLeftEdge + horizontalPadding;
        float maxX = roadRightEdge - ROCK_SIZE - horizontalPadding;

        // Spawn rocks higher up so players have more reaction time
        float screenHeight = Gdx.graphics.getHeight();
        float minY = screenHeight * 0.52f;
        float maxY = screenHeight * 0.65f - ROCK_SIZE - 90f;

        // Slightly smaller spacing so placement feels more natural
        float minDistanceBetweenRocks = 85f;

        // Split the Y range into separate bands so rocks do not appear in one straight row
        float totalYRange = maxY - minY;
        float bandHeight = totalYRange / rocks.size;

        for (int i = 0; i < rocks.size; i++) {
            Rock rock = rocks.get(i);

            boolean validPositionFound = false;
            int attempts = 0;

            // Each rock gets its own Y band, but still random inside that band
            float bandMinY = minY + i * bandHeight;
            float bandMaxY = bandMinY + bandHeight - 20f;

            while (!validPositionFound && attempts < 100) {
                float randomX = MathUtils.random(minX, Math.max(minX, maxX));
                float randomY = MathUtils.random(bandMinY, Math.max(bandMinY, bandMaxY));

                validPositionFound = true;

                for (int j = 0; j < i; j++) {
                    Rock otherRock = rocks.get(j);

                    float distance = rockDistance(
                            randomX,
                            randomY,
                            otherRock.getPosition().x,
                            otherRock.getPosition().y
                    );

                    if (distance < minDistanceBetweenRocks) {
                        validPositionFound = false;
                        break;
                    }
                }

                if (validPositionFound) {
                    rock.getPosition().set(randomX, randomY);
                    rock.update();
                }

                attempts++;
            }

            // If random placement fails too many times, still place it randomly inside its own band
            if (!validPositionFound) {
                float fallbackX = MathUtils.random(minX, Math.max(minX, maxX));
                float fallbackY = MathUtils.random(bandMinY, Math.max(bandMinY, bandMaxY));
                rock.getPosition().set(fallbackX, fallbackY);
                rock.update();
            }
        }

        // Reset collision lock after respawning rocks
        playerMustExitRockBeforeNextHit = false;
    }


    private void checkCollision() {
        if (player == null || livesElement == null) {
            return;
        }

        boolean touchingRock = isPlayerTouchingAnyRock();

        // Once the player leaves all rocks, allow damage again on the next collision
        if (!touchingRock) {
            playerMustExitRockBeforeNextHit = false;
        }

        // Still cooling down from a previous hit
        if (hitCooldown > 0f) {
            return;
        }

        // Still overlapping the same rock from the previous hit
        if (playerMustExitRockBeforeNextHit) {
            return;
        }

        if (touchingRock) {
            livesElement.setLivesCounter(livesElement.getLivesCounter() - 1);
            hitCooldown = HIT_COOLDOWN_DURATION;
            playerMustExitRockBeforeNextHit = true;

            // Optional feedback sound
            sfxPlayable.playSound("sound/wrong.mp3");
        }
    }

    private boolean isPlayerTouchingAnyRock() {
        for (Rock rock : rocks) {
            if (player.getBounds().overlaps(rock.getBounds())) {
                return true;
            }
        }
        return false;
    }

    private float rockDistance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}