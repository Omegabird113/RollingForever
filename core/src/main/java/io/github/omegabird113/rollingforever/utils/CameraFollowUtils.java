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
