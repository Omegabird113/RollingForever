/*
 RollingForever - The Java Roll-a-ball game
 Copyright (c) 2026 Omegabird113.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.omegabird113.rollingforever.physics;

import com.badlogic.gdx.math.Vector3;
import io.github.omegabird113.rollingforever.utils.InputKeyUtils;

public class RollingMovement {
    private final Vector3 position = new Vector3(0f, 1.5f, 0f);
    private final Vector3 velocity = new Vector3();
    private final Vector3 input = new Vector3();
    private final Vector3 movementForce = new Vector3();

    private final float FORCE;
    private final float ACTIVE_DRAG;
    private final float COAST_DRAG;
    private final float MAX_SPEED;
    private final float WALL_BOUNCE;
    private final float COLLISION_RADIUS;

    public RollingMovement(float force, float activeDrag, float coastDrag, float maxSpeed, float wallBounce, float CollisionRadius) {
        FORCE = force;
        ACTIVE_DRAG = activeDrag;
        COAST_DRAG = coastDrag;
        MAX_SPEED = maxSpeed;
        WALL_BOUNCE = wallBounce;
        COLLISION_RADIUS = CollisionRadius;
    }

    public Vector3 move(float delta) {
        readInput();
        applyMovementForce(delta);
        applyDrag(delta);
        limitSpeed();
        moveWithCollision(delta);
        return position;
    }

    private void readInput() {
        input.setZero();

        if (InputKeyUtils.isKeyDown(InputKeyUtils.KeyType.FORWARD)) input.z += 1f;
        if (InputKeyUtils.isKeyDown(InputKeyUtils.KeyType.BACKWARD)) input.z -= 1f;
        if (InputKeyUtils.isKeyDown(InputKeyUtils.KeyType.LEFT)) input.x += 1f;
        if (InputKeyUtils.isKeyDown(InputKeyUtils.KeyType.RIGHT)) input.x -= 1f;

        if (!input.isZero())
            input.nor();
    }

    private void applyMovementForce(float delta) {
        movementForce.set(input.x, 0f, input.z).scl(FORCE);
        velocity.mulAdd(movementForce, delta);
    }

    private void applyDrag(float delta) {
        float drag = input.isZero() ? COAST_DRAG : ACTIVE_DRAG;
        float dragFactor = Math.max(0f, 1f - drag * delta);
        velocity.x *= dragFactor;
        velocity.z *= dragFactor;
    }

    private void limitSpeed() {
        float speed2 = velocity.x * velocity.x + velocity.z * velocity.z;
        if (speed2 > MAX_SPEED * MAX_SPEED) {
            float speed = (float) Math.sqrt(speed2);
            velocity.x = velocity.x / speed * MAX_SPEED;
            velocity.z = velocity.z / speed * MAX_SPEED;
        }
    }

    private void moveWithCollision(float delta) {
        float nextX = position.x + velocity.x * delta;
        float nextZ = position.z + velocity.z * delta;

        position.x = nextX;
        if (CollisionManager.collidesWithWall(position, COLLISION_RADIUS)) {
            position.x -= velocity.x * delta;
            velocity.x = -velocity.x * WALL_BOUNCE;
        }

        position.z = nextZ;
        if (CollisionManager.collidesWithWall(position, COLLISION_RADIUS)) {
            position.z -= velocity.z * delta;
            velocity.z = -velocity.z * WALL_BOUNCE;
        }
    }

    public Vector3 getPosition() {
        return position;
    }
}
