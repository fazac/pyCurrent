package com.stock.pycurrent.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

public class DateUtils {
    private DateUtils() {
        throw new IllegalStateException("DateUtils class");
    }

    private static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    private static final String COMMON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    private static final DateTimeFormatter COMMON_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(COMMON_DATE_FORMAT);


    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date == null ? LocalDateTime.now() : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    public static long getDayInterval(Date start, Date end) {
        return DAYS.between(toLocalDateTime(start), toLocalDateTime(end));
    }

    @SuppressWarnings("unused")
    public static long getMinuteInterval(String start, String end) {
        return Duration.between(LocalDateTime.parse(start, COMMON_DATE_TIME_FORMAT), LocalDateTime.parse(end, COMMON_DATE_TIME_FORMAT)).toMinutes();
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

    public static String commonNow() {
        return LocalDateTime.now().format(COMMON_DATE_TIME_FORMAT);
    }


    public static boolean before(String startDate, String endDate) {
        return DateUtils.getDayInterval(DateUtils.defaultFormat(startDate), DateUtils.defaultFormat(endDate)) > 0;
    }


    public static String getFriday(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY)).format(DATE_TIME_FORMAT);
    }

    public static String getM_D(LocalDateTime localDateTime) {
        return StockUtils.addOneZero(localDateTime.getMonthValue()) + "-" + StockUtils.addOneZero(localDateTime.getDayOfMonth());
    }

    public static String getH_M(LocalDateTime localDateTime) {
        return StockUtils.addOneZero(localDateTime.getHour()) + ":" + StockUtils.addOneZero(localDateTime.getMinute());
    }
}
