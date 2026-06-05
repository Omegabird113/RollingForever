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

package io.github.omegabird113.rollingforever.utils;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public final class CameraFollowUtils {
    private static final Vector3 relativePosition = new Vector3(0f, 22f, -18f);

    private CameraFollowUtils() {
    }

    public static Vector3 getCameraPosition(Vector3 position) {
        return position.cpy().add(relativePosition);
    }

    public static void updateCameraTo(PerspectiveCamera camera, Vector3 position) {
        camera.position.set(getCameraPosition(position));
        camera.lookAt(position);
    }

    public static Vector3 getRelativePosition() {
        return relativePosition;
    }

    public static void setRelativePosition(Vector3 relativePosition) {
        CameraFollowUtils.relativePosition.set(relativePosition);
    }
}
