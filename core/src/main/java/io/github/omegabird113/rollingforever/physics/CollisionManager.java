package io.github.omegabird113.rollingforever.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public final class CollisionManager {
    private CollisionManager() {}

    private static final Array<WallBounds> wallBoundsList = new Array<>();

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
