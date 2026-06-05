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
import com.badlogic.gdx.utils.Array;

public final class CollisionManager {
    private static final Array<WallBounds> wallBoundsList = new Array<>();

    private CollisionManager() {
    }

    public static WallBounds[] getAllWallBounds() {
        return wallBoundsList.toArray();
    }

    public static void addWallBounds(WallBounds wallBounds) {
        wallBoundsList.add(wallBounds);
    }

    public static boolean collidesWithWall(Vector3 position, float radius) {
        for (WallBounds wall : wallBoundsList)
            if (wall.overlapsCircle(position, radius))
                return true;
        return false;
    }
}
