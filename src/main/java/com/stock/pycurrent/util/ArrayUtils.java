package com.stock.pycurrent.util;

import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.entity.vo.DnVO;
import com.stock.pycurrent.entity.vo.RealTimeVO;
import lombok.extern.apachecommons.CommonsLog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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

    public static List<RealTimeVO> convertRealTimeVO(List<EmRealTimeStock> sources, Map<String, BigDecimal> barTimeMap) {
        if (sources != null && !sources.isEmpty()) {
            List<RealTimeVO> res = new ArrayList<>();
            EmRealTimeStock current = sources.get(0);
            RealTimeVO firstOne = new RealTimeVO();
            firstOne.setDi(0);
            firstOne.setCp(current.getCurrentPri());
            firstOne.setRt(current.getPctChg());
            firstOne.setVs(current.getVol());
            firstOne.setH(current.getChangeHand());
            firstOne.setBar(barTimeMap.getOrDefault(current.getTradeDate(), null));
            res.add(firstOne);
            for (int i = 1; i < sources.size(); i++) {
                current = sources.get(i);
                EmRealTimeStock last = sources.get(i - 1);
                RealTimeVO realTimeVO = new RealTimeVO();
                realTimeVO.setDi(i);
                realTimeVO.setCp(current.getCurrentPri());
                realTimeVO.setRt(current.getPctChg());
                realTimeVO.setVs(current.getVol() - last.getVol());
                realTimeVO.setH(current.getChangeHand());
                realTimeVO.setBar(barTimeMap.getOrDefault(current.getTradeDate(), null));
                res.add(realTimeVO);
            }
            return res;
        }
        return Collections.emptyList();
    }

    public static List<DnVO> convertDnVO(List<EmDNStock> sources) {
        if (sources != null && !sources.isEmpty()) {
            List<DnVO> res = new ArrayList<>();
            EmDNStock current;
            for (int i = 0; i < sources.size(); i++) {
                current = sources.get(i);
                DnVO dnVO = new DnVO();
                dnVO.setDi(i);
                dnVO.setLp(current.getPriLow());
                dnVO.setHp(current.getPriHigh());
                dnVO.setVol(current.getVol());
                dnVO.setAp(current.getAmount().divide(BigDecimal.valueOf(current.getVol()).multiply(Constants.HUNDRED), 2, RoundingMode.HALF_UP));
                dnVO.setCp(current.getPriClose());
                dnVO.setH(current.getChangeHand());
                res.add(dnVO);
            }
            return res;
        }
        return Collections.emptyList();
    }
}
