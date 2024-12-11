package com.stock.pycurrent.util;

import com.stock.pycurrent.entity.BasicStock;
import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.entity.StockCalModel;
import com.stock.pycurrent.entity.vo.DnVO;
import com.stock.pycurrent.entity.vo.OpenVO;
import com.stock.pycurrent.entity.vo.RealTimeVO;
import com.stock.pycurrent.schedule.PrepareData;
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
                DnVO dnVO = generateDnVO(i, current);
                res.add(dnVO);
            }
            return res;
        }
        return Collections.emptyList();
    }

    public static List<StockCalModel> convertCalModels(List<? extends BasicStock> sources) {
        if (sources != null && !sources.isEmpty()) {
            List<StockCalModel> res = new ArrayList<>();
            for (BasicStock source : sources) {
                StockCalModel stockCalModel = new StockCalModel();
                stockCalModel.setPrice(source.getPriClose());
                stockCalModel.setTsCode(source.getTsCode());
                stockCalModel.setTradeDate(source.getTradeDate());
                res.add(stockCalModel);
            }
            return res;
        }
        return Collections.emptyList();
    }


    private static DnVO generateDnVO(int i, EmDNStock current) {
        DnVO dnVO = new DnVO();
        dnVO.setDi(i);
        dnVO.setTradeDate(current.getTradeDate().substring(4, 8));
        dnVO.setLp(current.getPriLow());
        dnVO.setHp(current.getPriHigh());
        dnVO.setVol(current.getVol());
        dnVO.setAp(current.getAmount().divide(BigDecimal.valueOf(current.getVol()).multiply(Constants.HUNDRED), 2, RoundingMode.HALF_UP));
        dnVO.setCp(current.getPriClose());
        dnVO.setH(current.getChangeHand());
        return dnVO;
    }

    public static List<OpenVO> convertOpenVO(List<EmRealTimeStock> emRealTimeStocks) {
        if (emRealTimeStocks != null && !emRealTimeStocks.isEmpty()) {
            List<OpenVO> res = new ArrayList<>();
            for (EmRealTimeStock emRealTimeStock : emRealTimeStocks) {
                OpenVO openVO = new OpenVO();
                openVO.setTs_code(emRealTimeStock.getTsCode());
                openVO.setName(emRealTimeStock.getName());
                openVO.setPct_chg(emRealTimeStock.getPctChg());
                openVO.setChange_hand(emRealTimeStock.getChangeHand());
                openVO.setPe(emRealTimeStock.getPe());
                openVO.setPb(emRealTimeStock.getPb());
                if (emRealTimeStock.getCirculationMarketCap() != null) {
                    openVO.setCap(emRealTimeStock.getCirculationMarketCap().divide(Constants.ONE_HUNDRED_MILLION, 2, RoundingMode.HALF_UP));
                }
                if (emRealTimeStock.getPriClosePre() != null && emRealTimeStock.getPriOpen() != null) {
                    openVO.setOpen(emRealTimeStock.getPriOpen().subtract(emRealTimeStock.getPriClosePre()).multiply(Constants.HUNDRED)
                            .divide(emRealTimeStock.getPriClosePre(), 3, RoundingMode.HALF_UP));
                    openVO.setLow(emRealTimeStock.getPriLow().subtract(emRealTimeStock.getPriClosePre()).multiply(Constants.HUNDRED)
                            .divide(emRealTimeStock.getPriClosePre(), 3, RoundingMode.HALF_UP));
                    openVO.setHigh(emRealTimeStock.getPriHigh().subtract(emRealTimeStock.getPriClosePre()).multiply(Constants.HUNDRED)
                            .divide(emRealTimeStock.getPriClosePre(), 3, RoundingMode.HALF_UP));
                } else {
                    openVO.setOpen(emRealTimeStock.getPriOpen());
                    openVO.setLow(emRealTimeStock.getPriLow());
                    openVO.setHigh(emRealTimeStock.getPriHigh());
                }
                openVO.setCurrent_pri(emRealTimeStock.getCurrentPri());
                if (emRealTimeStock.getAmount() != null) {
                    openVO.setAvg_pri(emRealTimeStock.getAmount()
                            .divide(BigDecimal.valueOf(emRealTimeStock.getVol()).multiply(Constants.HUNDRED), 3, RoundingMode.HALF_UP));
                }
                openVO.setPri_pre(emRealTimeStock.getPriClosePre());
                openVO.setLabels(PrepareData.findLabelStr(emRealTimeStock.getTsCode()));
                res.add(openVO);
            }
            return res;
        }
        return Collections.emptyList();
    }

}
