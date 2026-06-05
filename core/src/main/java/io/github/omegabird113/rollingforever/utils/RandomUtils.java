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

package io.github.omegabird113.rollingforever.utils;

import java.util.Random;

public class RandomUtils {
    public static final Random RANDOM = new Random();

    private RandomUtils() {
    }

    public static Random getJavaRandomObject() {
        return RANDOM;
    }

    public static int randomInt(int min, int max, boolean maxInclusive) {
        return RANDOM.nextInt(min, maxInclusive ? max + 1 : max);
    }

    public static float randomFloat(float min, float max, boolean maxInclusive) {
        return RANDOM.nextFloat(min, maxInclusive ? max + 1 : max);
    }
}
