package com.stock.pycurrent.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private DateUtils() {
        throw new IllegalStateException("DateUtils class");
    }

    private static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);


    public static String now() {
        return LocalDateTime.now().format(DATE_TIME_FORMAT);
    }


}
