package com.github.miho73.lila.utils;

public class Verifiers {
    /**
     * Check if value is in bound [upper, lower]
     *
     * @param value value to check
     * @param upper upper bound
     * @param lower lower bound
     * @return true when value is in bound
     */
    public static boolean inRange(int value, int upper, int lower) {
        return value >= lower && value <= upper;
    }
}
