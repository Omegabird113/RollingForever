package io.github.omegabird113.rollingforever.utils;

import java.util.Random;

public class RandomUtils {
    private RandomUtils() {}

    public static final Random RANDOM = new Random();

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
