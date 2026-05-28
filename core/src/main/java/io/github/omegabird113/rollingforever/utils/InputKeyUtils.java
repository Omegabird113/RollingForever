package io.github.omegabird113.rollingforever.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public final class InputKeyUtils {
    private InputKeyUtils() {}

    public enum KeyType {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    public static boolean isKeyDown(KeyType keyType) {
        return switch (keyType) {
            case FORWARD -> Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.DPAD_UP);
            case BACKWARD -> Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN);
            case LEFT -> Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT);
            case RIGHT -> Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT);
        };
    }
}
