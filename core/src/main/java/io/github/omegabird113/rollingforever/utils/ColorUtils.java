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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.Collection;

public class ColorUtils {
    public static final Color BLACK = get8Bit(0, 0, 0);
    public static final Color WHITE = get8Bit(255, 255, 255);
    public static final Color PURE_RED = get8Bit(255, 0, 0);
    public static final Color PURE_GREEN = get8Bit(255, 0, 255);
    public static final Color PURE_BLUE = get8Bit(0, 0, 255);

    public static Color get8Bit(int r, int g, int b, int a) {
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static Color get8Bit(int r, int g, int b) {
        return get8Bit(r, g, b, 255);
    }

    public static Color getNBit(int n, int r, int g, int b, int a) {
        return new Color(r / (float) n, g / (float) n, b / (float) n, a / (float) n);
    }

    public static Color getNBit(int n, int r, int g, int b) {
        return getNBit(n, r, g, b, 255);
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

    public static Color getRandomNBit(int n) {
        int r = RandomUtils.randomInt(0, n, true);
        int g = RandomUtils.randomInt(0, n, true);
        int b = RandomUtils.randomInt(0, n, true);
        return getNBit(n, r, g, b);
    }
}
