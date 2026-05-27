package io.github.omegabird113.rollingforever.utils;

import com.badlogic.gdx.graphics.Color;

public class ColorUtils {
    public static Color get8BitColor(int r, int g, int b, int a) {
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static Color get8BitColor(int r, int g, int b) {
        return get8BitColor(r, g, b, 255);
    }
}
