package com.stock.pycurrent.util;

import lombok.extern.apachecommons.CommonsLog;

import java.util.Arrays;

@CommonsLog
public class ArrayUtils {

    private ArrayUtils() {
        throw new IllegalStateException("ArrayUtils class");
    }

    @SafeVarargs
    public static <T> T[] concat(T[] first, T[]... rest) {
        int totalLength = first.length;

        for (T[] array : rest) {
            totalLength += array.length;
        }

        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;

        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }
}
