package com.stock.pycurrent.util;

import lombok.extern.apachecommons.CommonsLog;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@CommonsLog
public class StockUtils {

    private StockUtils() {
        throw new IllegalStateException("StockUtils class");
    }

    public static boolean isNotRest() {
        return LocalDateTime.now().getDayOfWeek() != DayOfWeek.SATURDAY
               && LocalDateTime.now().getDayOfWeek() != DayOfWeek.SUNDAY;
    }

    public static String concatChar(String value, int length) {
        return value.repeat(length);
    }

}
