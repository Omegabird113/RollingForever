/*
 RollingForever - The Java Roll-a-ball game
 Copyright (c) 2026 Omegabird113.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.omegabird113.rollingforever.physics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class WallBounds {
    public final float minX;
    public final float maxX;
    public final float minZ;
    public final float maxZ;

    public WallBounds(ModelInstance instance) {
        BoundingBox box = new BoundingBox();
        Vector3 position = new Vector3();
        Vector3 dimensions = new Vector3();
        Vector3 scale = new Vector3();

        instance.model.calculateBoundingBox(box);
        instance.transform.getTranslation(position);
        instance.transform.getScale(scale);
        box.getDimensions(dimensions);

        float halfWidth = dimensions.x * Math.abs(scale.x) / 2f;
        float halfDepth = dimensions.z * Math.abs(scale.z) / 2f;

        this.minX = position.x - halfWidth;
        this.maxX = position.x + halfWidth;
        this.minZ = position.z - halfDepth;
        this.maxZ = position.z + halfDepth;
    }

    public boolean overlapsCircle(Vector3 circleCenter, float radius) {
        float closestX = MathUtils.clamp(circleCenter.x, minX, maxX);
        float closestZ = MathUtils.clamp(circleCenter.z, minZ, maxZ);

        float dx = circleCenter.x - closestX;
        float dz = circleCenter.z - closestZ;

        return dx * dx + dz * dz <= radius * radius;
    }
}
