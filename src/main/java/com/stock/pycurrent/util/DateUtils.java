package com.stock.pycurrent.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

public class DateUtils {
    private DateUtils() {
        throw new IllegalStateException("DateUtils class");
    }

    private static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);


    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date == null ? LocalDateTime.now() : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    public static long getDayInterval(Date start, Date end) {
        return DAYS.between(toLocalDateTime(start), toLocalDateTime(end));
    }

    public static Date defaultFormat(String dateStr) {
        return toDate(LocalDate.parse(dateStr, DATE_TIME_FORMAT).atStartOfDay());
    }

    /**
     * 获取指定日期时间 指定单位偏移量 的日期时间
     *
     * @param dateStr  指定日期
     * @param offset   偏移量
     * @param dateUnit 单位
     * @return 结果日期
     */
    public static String getDateAtOffset(String dateStr, long offset, ChronoUnit dateUnit) {
        return LocalDate.parse(dateStr, DATE_TIME_FORMAT).atStartOfDay().plus(offset, dateUnit).format(DATE_TIME_FORMAT);
    }

    public static String now() {
        return LocalDateTime.now().format(DATE_TIME_FORMAT);
    }


    public static boolean before(String startDate, String endDate) {
        return DateUtils.getDayInterval(DateUtils.defaultFormat(startDate), DateUtils.defaultFormat(endDate)) > 0;
    }

}