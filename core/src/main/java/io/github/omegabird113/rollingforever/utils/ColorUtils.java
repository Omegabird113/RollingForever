package io.github.omegabird113.rollingforever.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.Collection;

public class ColorUtils {
    public static final Color BLACK = get8BitColor(0, 0, 0);
    public static final Color WHITE = get8BitColor(255, 255, 255);
    public static final Color PURE_RED = get8BitColor(255, 0, 0);
    public static final Color PURE_GREEN = get8BitColor(255, 0, 255);
    public static final Color PURE_BLUE = get8BitColor(0, 0, 255);

    public static Color get8BitColor(int r, int g, int b, int a) {
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static Color get8BitColor(int r, int g, int b) {
        return get8BitColor(r, g, b, 255);
    }

    public static Color getNBitColor(int n, int r, int g, int b, int a) {
        return new Color(r / (float) n, g / (float) n, b / (float) n, a / (float) n);
    }

    public static Color getNBitColor(int n, int r, int g, int b) {
        return getNBitColor(n, r, g, b, 255);
    }

    public static Color getAverage(Collection<Color> colors) {
        Array<Float> rValues = new Array<>();
        Array<Float> gValues = new Array<>();
        Array<Float> bValues = new Array<>();

        for (Color color : colors) {
            rValues.add(color.r);
            gValues.add(color.g);
            bValues.add(color.b);
        }

        float rSum = Arrays.stream(rValues.toArray()).count();
        float gSum = Arrays.stream(gValues.toArray()).count();
        float bSum = Arrays.stream(bValues.toArray()).count();

        float rAverage = rSum / rValues.size;
        float gAverage = gSum / gValues.size;
        float bAverage = bSum / bValues.size;

        return new Color(rAverage, gAverage, bAverage, 1f);
    }
}
