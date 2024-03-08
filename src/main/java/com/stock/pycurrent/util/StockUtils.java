package com.stock.pycurrent.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.pycurrent.entity.EmDAStock;
import com.stock.pycurrent.entity.RocModel;
import com.stock.pycurrent.entity.emum.EMSymbolEnum;
import com.stock.pycurrent.entity.emum.PyFuncEnum;
import com.stock.pycurrent.entity.model.Constants;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@CommonsLog
public class StockUtils {
    private StockUtils() {
        throw new IllegalStateException("StockUtils class");
    }

    public static void initEmBasicData(String startDate, String endDate, String reType, String period, PyFuncEnum pyFuncEnum) {
        ObjectNode objectNode = JSONUtils.getNode();
        objectNode.put("start_date", DateUtils.getDateAtOffset(startDate, 1, ChronoUnit.DAYS));
        objectNode.put("end_date", endDate);
        objectNode.put("adjust", reType);
        objectNode.put("period", period);
        PythonScriptUtils.execThreadPY(Constants.AKSHARE_EM_HIS, pyFuncEnum + EMSymbolEnum.EM_STOCK.toString() + " --params=" + JSONUtils.toDoubleJson(objectNode));
    }

    public static String getPullHourEndDate() {
        String endDate = DateUtils.now();
        boolean afterPullHour = LocalDateTime.now().getHour() >= Constants.EM_PULL_HOUR;
        if (afterPullHour) {
            return endDate;
        } else {
            return DateUtils.getDateAtOffset(endDate, -1, ChronoUnit.DAYS);
        }
    }

    public static boolean afterPullHour() {
        return LocalDateTime.now().getHour() == 15
               && LocalDateTime.now().getMinute() >= 30
               || LocalDateTime.now().getHour() > 15;
    }

    @SuppressWarnings("unused")
    public static boolean inStartTimePeriod() {
        return LocalDateTime.now().getHour() <= 15
               && LocalDateTime.now().getMinute() <= 40;
    }

    public static boolean isNotRest() {
        return LocalDateTime.now().getDayOfWeek() != DayOfWeek.SATURDAY
               && LocalDateTime.now().getDayOfWeek() != DayOfWeek.SUNDAY;
    }

    public static boolean checkEmpty(String str) {
        return str == null || str.isBlank();
    }

    public static boolean availableCode(String code) {
        return !checkEmpty(code) && (code.startsWith("30") || code.startsWith("00") || code.startsWith("60"));
    }

    public static List<RocModel> findPeekPoint(List<EmDAStock> ts, String params, Date now) {
        List<Integer> points = new ArrayList<>();
        points.add(0);
        points.add(ts.size() - 1);
        return findCyclePeekPoint(ts, points, new ArrayList<>(), params, now);
    }

    @SneakyThrows
    public static List<RocModel> findCyclePeekPoint(List<EmDAStock> ts, List<Integer> points, List<RocModel> res, String params, Date now) {
        int oldSize = points.size();
        List<Integer> pointCopy = new ArrayList<>(points);
        for (int i = 0; i < pointCopy.size() - 1; i++) {
            int startP = pointCopy.get(i);
            List<EmDAStock> tmpTs = ts.subList(pointCopy.get(i), pointCopy.get(i + 1) + 1);
            List<Integer> tmpPoints = findSectionPeekIndex(tmpTs).stream().filter(x -> x != 0 && x != tmpTs.size() - 1).toList();
            if (!tmpPoints.isEmpty()) {
                points.addAll(tmpPoints.stream().map(x -> x + startP).toList());
                Collections.sort(points);
            }
        }
        if (points.size() == oldSize) {
            String code = ts.get(0).getTsCode();
            for (int i = 0; i < points.size() - 1; i++) {
                RocModel roc = new RocModel();
                roc.setTsCode(code);
                roc.setDoorPri(ts.get(points.get(i)).getPriClose());
                roc.setCurClosePri(ts.get(points.get(i + 1)).getPriClose());
                roc.setCount(points.get(i + 1) - points.get(i));
                roc.setStartDate(ts.get(points.get(i)).getTradeDate());
                roc.setEndDate(ts.get(points.get(i + 1)).getTradeDate());
                roc.setParams(params);
                roc.setCreateTime(now);
                roc.setRatio(roc.getCurClosePri().subtract(roc.getDoorPri()).multiply(Constants.HUNDRED).divide(roc.getDoorPri(), 2, RoundingMode.HALF_UP));
                res.add(roc);
            }
            return res;
        } else {
            return findCyclePeekPoint(ts, points, res, params, now);
        }
    }

    @SneakyThrows
    public static List<Integer> findSectionPeekIndex(List<EmDAStock> ts) {
        BigDecimal doorPri;
        BigDecimal min = BigDecimal.valueOf(9999);
        BigDecimal max = BigDecimal.valueOf(-9999);
        int minIndex = 0;
        int maxIndex = 0;
        BigDecimal curClosePri;
        doorPri = ts.get(0).getPriClose();
        for (int i = 1; i < ts.size(); i++) {
            curClosePri = ts.get(i).getPriClose();
            if (curClosePri.compareTo(min) < 0 && curClosePri.compareTo(doorPri) < 0) {
                min = curClosePri;
                minIndex = i;
            }
            if (curClosePri.compareTo(max) > 0 && curClosePri.compareTo(doorPri) > 0) {
                max = curClosePri;
                maxIndex = i;
            }
        }
        return List.of(maxIndex, minIndex);
    }

}
