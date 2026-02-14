package io.github.inf1009_p10_9.lwjgl3;

import com.badlogic.gdx.math.Rectangle;

public class RigidBodyResolver implements ICollisionResolver {

    @Override
    public void resolve(ICollidable a, ICollidable b) {
        // If both are static (immovable), do nothing
        if (a.isStatic() && b.isStatic()) return;

        Rectangle r1 = a.getBounds();
        Rectangle r2 = b.getBounds();

        // 1. Calculate the overlap on both axes
        float overlapX = Math.min(r1.x + r1.width, r2.x + r2.width) - Math.max(r1.x, r2.x);
        float overlapY = Math.min(r1.y + r1.height, r2.y + r2.height) - Math.max(r1.y, r2.y);

        if (overlapX <= 0 || overlapY <= 0) return; // No actual overlap

        // 2. Determine the "path of least resistance" (smallest overlap)
        if (overlapX < overlapY) {
            // Resolve on X Axis
            float centerA = r1.x + (r1.width / 2);
            float centerB = r2.x + (r2.width / 2);
            // If A is to the left of B, push A left (-1), otherwise right (1)
            float direction = (centerA < centerB) ? -1 : 1;
            
            applyResolution(a, b, overlapX * direction, 0);
        } else {
            // Resolve on Y Axis
            float centerA = r1.y + (r1.height / 2);
            float centerB = r2.y + (r2.height / 2);
            // If A is above B, push A up (1), otherwise down (-1) (assuming Y-up coords)
            // Note: Adjust direction sign based on your engine's coordinate system
            float direction = (centerA < centerB) ? -1 : 1;
            
            applyResolution(a, b, 0, overlapY * direction);
        }
    }

    private void applyResolution(ICollidable a, ICollidable b, float moveX, float moveY) {
        if (!a.isStatic() && !b.isStatic()) {
            // Both movable: push them apart equally (50% each)
            a.move(moveX * 0.5f, moveY * 0.5f);
            b.move(-moveX * 0.5f, -moveY * 0.5f);
        } else if (!a.isStatic()) {
            // Only A is movable: push A 100%
            a.move(moveX, moveY);
        } else if (!b.isStatic()) {
            // Only B is movable: push B 100% (inverse direction)
            b.move(-moveX, -moveY);
        }
    }
}
