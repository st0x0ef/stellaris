package com.st0x0ef.stellaris.common.utils;

public class MathUtils
{
    /**
     * Subtracts b from a and returns zero if the result is negative.
     *
     * @param a The minuend.
     * @param b The subtrahend.
     * @return The result of a - b, or 0 if the result is negative.
     */
    public static float subtractToZero(float a, float b)
    {
        float result = a - b;
        return Math.max(result, 0.0F);
    }
    public static float convertToTAU(float a){
        /** return TAU without function PI * 2.00 */
        return (float) (Math.TAU *  a);
    }
}
