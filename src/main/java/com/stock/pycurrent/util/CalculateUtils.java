package com.stock.pycurrent.util;

import com.stock.pycurrent.entity.EmRealTimeStock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fzc
 * @date 2024/1/5 14:19
 * @description
 */

public class CalculateUtils {

    private static final BigDecimal TEN_LIMIT = BigDecimal.valueOf(1.1);

    private static final BigDecimal TWENTY_LIMIT = BigDecimal.valueOf(1.2);

    /**
     * 计算SMA
     *
     * @param values 数据集
     * @param n      周期
     * @param weight 权重
     * @return SMA集
     */
    @SuppressWarnings("unused")

    public static List<BigDecimal> calSMA(List<BigDecimal> values, int n, int weight) {
        List<BigDecimal> res = new ArrayList<>();
        if (values.size() > n) {
            for (int i = 0; i < n; i++) {
                res.add(getSMAStart(values, i + 1));
            }
            for (int i = n; i < values.size(); i++) {
                res.add(calSMANext(values.get(i), res.get(res.size() - 1), n, weight));
            }
        } else {
            res.add(values.stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP));
        }
        return res;
    }

    /**
     * 获取SMA起点
     *
     * @param pri 数据集
     * @param n   周期
     * @return 起点值
     */
    public static BigDecimal getSMAStart(List<BigDecimal> pri, int n) {
        BigDecimal total = BigDecimal.ZERO;
        int limit = Integer.min(n, pri.size());
        for (int i = 0; i < limit; i++) {
            total = total.add(pri.get(i));
        }
        return total.divide(BigDecimal.valueOf(limit), 3, RoundingMode.HALF_UP);
    }

    /**
     * 根据 SMA' 计算SMA, EMA(X,N) Y=(X*2 + Y'*(N-1))/(N+1)
     *
     * @param curValue 现在的值
     * @param lastSMA  上一个SMA
     * @param n        周期
     * @param weight   权重
     * @return SMA
     */
    public static BigDecimal calSMANext(BigDecimal curValue, BigDecimal lastSMA, int n, int weight) {
        return curValue.multiply(BigDecimal.valueOf(weight)).add(lastSMA.multiply(BigDecimal.valueOf((long) n - weight))).divide(BigDecimal.valueOf(n), 3, RoundingMode.HALF_UP);
    }

    public static boolean reachTenLimit(EmRealTimeStock emRealTimeStock) {
        return reachLimit(emRealTimeStock, TEN_LIMIT);
    }

    public static boolean reachTwentyLimit(EmRealTimeStock emRealTimeStock) {
        return reachLimit(emRealTimeStock, TWENTY_LIMIT);
    }

    private static boolean reachLimit(EmRealTimeStock emRealTimeStock, BigDecimal limit) {
        return emRealTimeStock.getCurrentPri()
                .compareTo(emRealTimeStock.getPriClosePre().multiply(limit).setScale(2, RoundingMode.HALF_UP)) == 0;
    }
}
