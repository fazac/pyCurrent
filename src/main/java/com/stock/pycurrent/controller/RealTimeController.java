package com.stock.pycurrent.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.entity.LimitCode;
import com.stock.pycurrent.entity.annotation.RequestLimit;
import com.stock.pycurrent.entity.jsonvalue.LimitCodeValue;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.entity.vo.CodeDataVO;
import com.stock.pycurrent.entity.vo.DnVO;
import com.stock.pycurrent.entity.vo.LimitCodeVO;
import com.stock.pycurrent.entity.vo.OpenVO;
import com.stock.pycurrent.schedule.PrepareData;
import com.stock.pycurrent.service.*;
import com.stock.pycurrent.util.ArrayUtils;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.JSONUtils;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fzc
 * @date 2024/6/11 17:00
 * @description
 */

@RestController
@RequestMapping("rt")
@CommonsLog
public class RealTimeController {
    @Resource
    private EmRealTimeStockService emRealTimeStockService;
    @Resource
    private EmDNStockService emDNStockService;
    @Resource
    private RocModelService rocModelService;
    @Resource
    private CurConcernCodeService curConcernCodeService;
    @Resource
    private RealBarService realBarService;
    @Resource
    private LimitCodeService limitCodeService;
    @Resource
    private CodeLabelService codeLabelService;

    public RealTimeController() {
    }

    @GetMapping("hi")
    public String hello() {
        return "hello world";
    }

    @GetMapping("label")
    public String label() {
        codeLabelService.createLabels();
        return "label";
    }

    @GetMapping("findDataByCode")
    @RequestLimit(key = "limit0",
            permitsPerSecond = 1,
            timeout = 500,
            msg = "访问频率已超限制")
    public ObjectNode findDataByCode(@Param("code") String code) {
        ObjectNode objectNode = JSONUtils.getNode();
        if (code == null || code.isEmpty()) {
            return objectNode;
        }
        List<OpenVO> openVOList = ArrayUtils.convertOpenVO(emRealTimeStockService.findOpenByCode(code));
        if (openVOList.isEmpty()) {
            log.warn("no current log ,code = " + code);
            return objectNode;
        }
        objectNode.putPOJO("label", PrepareData.findLabelList(code));
        objectNode.putPOJO("open", openVOList);
        objectNode.putPOJO("dnDetail", emDNStockService.findByCodeCount(code));
        objectNode.putPOJO("roc", rocModelService.findRocByCode(code));
        objectNode.putPOJO("current", ArrayUtils.convertRealTimeVO(
                emRealTimeStockService.findRBarStockByCode(code), realBarService.findIntradayBar(code)).reversed());
        return objectNode;
    }

    @GetMapping("findDataLineByCode")
    @RequestLimit(key = "limit1",
            permitsPerSecond = 1,
            timeout = 500,
            msg = "访问频率已超限制")
    public ObjectNode findDataLineByCode(@Param("code") String code) {
        ObjectNode objectNode = JSONUtils.getNode();
        List<DnVO> dnVOList = ArrayUtils.convertDnVO(emDNStockService.findByCodeCount(code).reversed());
        objectNode.putPOJO("dnData", dnVOList);
        objectNode.putPOJO("dnPriMin", findPriMin(dnVOList));
        objectNode.putPOJO("dnPriMax", findPriMax(dnVOList));
        return objectNode;
    }

    @GetMapping("findOtherList")
    public List<CodeDataVO> findOtherList() {
        return curConcernCodeService.findLast("0");
    }

    @GetMapping("searchSome")
    public ObjectNode searchSome(@RequestParam(value = "code", required = false) String code,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "searchDate", required = false) Long searchDate,
                                 @RequestParam(value = "hand", required = false) Double hand,
                                 @RequestParam(value = "pch", required = false) Double pch,
                                 @RequestParam(value = "count", required = false) Integer count,
                                 @RequestParam(value = "r2LowLimit", required = false) Double r2LowLimit,
                                 @RequestParam(value = "r2HighLimit", required = false) Double r2HighLimit,
                                 @RequestParam(value = "r1LowLimit", required = false) Double r1LowLimit,
                                 @RequestParam(value = "r1HighLimit", required = false) Double r1HighLimit,
                                 @RequestParam(value = "type") String type) {
        ObjectNode objectNode = JSONUtils.getNode();

        switch (type) {
            //code
            case "1" -> objectNode = findDataByCode(code);
            //realName
            case "2" -> {
                List<OpenVO> openVOList = ArrayUtils.convertOpenVO(emRealTimeStockService.findOpenByName(name));
                objectNode.putPOJO("openVOList", openVOList);
            }
            //limit
            case "3" -> {
                LimitCode limitCode;
                if (searchDate == null) {
                    limitCode = limitCodeService.findLastOne(DateUtils.now());
                } else {
                    limitCode = limitCodeService.findByDate(DateUtils.convertMillisecond(searchDate));
                }

                List<CodeDataVO> codeDataVOList = new ArrayList<>();
                if (limitCode != null && limitCode.getCodeValue() != null && !limitCode.getCodeValue().isEmpty()) {
                    List<LimitCodeValue> tmp = limitCode.getCodeValue();
                    tmp.sort(Comparator.comparing(LimitCodeValue::getCount));
                    tmp = tmp.reversed();
                    tmp.forEach(x -> {
                        CodeDataVO codeDataVO = convertCodeDataVO(x.getCode());
                        ObjectNode extraNode = JSONUtils.getNode();
                        extraNode.put("count", x.getCount());
                        codeDataVO.setExtraNode(extraNode);
                        codeDataVOList.add(codeDataVO);
                    });
                }
                objectNode.putPOJO("codeDataVOList", codeDataVOList);
            }
            //ophc
            case "4" -> {
                List<CodeDataVO> codeDataVOList = emDNStockService.findOPHC(count,
                        hand == null ? null : BigDecimal.valueOf(hand),
                        pch == null ? null : BigDecimal.valueOf(pch));
                objectNode.putPOJO("codeDataVOList", codeDataVOList);
            }
            //roc
            case "5" -> {
                List<CodeDataVO> codeDataVOList = rocModelService.findRocByLimit(
                        r2HighLimit == null ? null : BigDecimal.valueOf(r2HighLimit).negate(),
                        r2LowLimit == null ? null : BigDecimal.valueOf(r2LowLimit).negate(),
                        r1LowLimit == null ? null : BigDecimal.valueOf(r1LowLimit),
                        r1HighLimit == null ? null : BigDecimal.valueOf(r1HighLimit)
                );
                objectNode.putPOJO("codeDataVOList", codeDataVOList);
            }
        }
        return objectNode;
    }

    @GetMapping("findLast")
    public List<CodeDataVO> findLast() {
        return convertCodeDataVO(emRealTimeStockService.findLast());
    }

    @GetMapping("findCptr")
    public List<CodeDataVO> findCptr(@RequestParam(value = "symbol") String symbol) {
        return convertCodeDataVO(emRealTimeStockService.findCptr(symbol));
    }

    private CodeDataVO convertCodeDataVO(@RequestParam(value = "code", required = false) String code) {
        CodeDataVO vo = new CodeDataVO();
        vo.setCode(code);
        emRealTimeStockService.fillCodeData(vo);
        return vo;
    }

    private List<CodeDataVO> convertCodeDataVO(List<EmRealTimeStock> emRealTimeStockList) {
        if (emRealTimeStockList != null && !emRealTimeStockList.isEmpty()) {
            List<CodeDataVO> res = new ArrayList<>();
            for (EmRealTimeStock emRealTimeStock : emRealTimeStockList) {
                CodeDataVO codeDataVO = new CodeDataVO();
                codeDataVO.setCode(emRealTimeStock.getTsCode());
                codeDataVO.setCurrentPri(emRealTimeStock.getCurrentPri());
                ObjectNode extraNode = JSONUtils.getNode();
                extraNode.put("pch", emRealTimeStock.getPctChg());
                extraNode.put("hand", emRealTimeStock.getChangeHand());
                codeDataVO.setPe(emRealTimeStock.getPe());
                codeDataVO.setPb(emRealTimeStock.getPb());
                codeDataVO.setCm(emRealTimeStock.getCirculationMarketCap());
                codeDataVO.setLabels(PrepareData.findLabelStr(emRealTimeStock.getTsCode()));
                codeDataVO.setExtraNode(extraNode);
                res.add(codeDataVO);
            }
            return res;
        }
        return Collections.emptyList();
    }

    private void convertLimitVO(@RequestParam(value = "code", required = false) String code, LimitCodeVO limitCodeVO) {
        EmRealTimeStock emRealTimeStock = emRealTimeStockService.findOpenByCode(code).getFirst();
        limitCodeVO.setPe(emRealTimeStock.getPe());
        limitCodeVO.setPb(emRealTimeStock.getPb());
        if (emRealTimeStock.getCirculationMarketCap() != null) {
            limitCodeVO.setCap(emRealTimeStock.getCirculationMarketCap().divide(Constants.ONE_HUNDRED_MILLION, 2, RoundingMode.HALF_UP));
        }
        limitCodeVO.setLabels(PrepareData.findLabelStr(code));
    }

    private void convertROCLimitVO(List<LimitCodeVO> limitCodeVOS) {
        if (limitCodeVOS == null || limitCodeVOS.isEmpty()) {
            return;
        }
        List<EmRealTimeStock> emRealTimeStockList = emRealTimeStockService.findLast();
        if (emRealTimeStockList != null && !emRealTimeStockList.isEmpty()) {
            Map<String, EmRealTimeStock> emRealTimeStockMap = emRealTimeStockList.stream().collect(Collectors.toMap(EmRealTimeStock::getTsCode, Function.identity()));
            limitCodeVOS.forEach(x -> {
                EmRealTimeStock emRealTimeStock = emRealTimeStockMap.get(x.getCode());
                x.setPe(emRealTimeStock.getPe());
                x.setPb(emRealTimeStock.getPb());
                if (emRealTimeStock.getCirculationMarketCap() != null) {
                    x.setCap(emRealTimeStock.getCirculationMarketCap().divide(Constants.ONE_HUNDRED_MILLION, 2, RoundingMode.HALF_UP));
                }
                x.setLabels(PrepareData.findLabelStr(x.getCode()));
            });
        }
    }

    public BigDecimal findPriMin(List<DnVO> dnVOList) {
        BigDecimal min = Constants.PRICE_MAX;
        for (DnVO dnVO : dnVOList) {
            min = min.min(dnVO.getLp());
        }
        return min.setScale(0, RoundingMode.FLOOR);
    }

    public BigDecimal findPriMax(List<DnVO> dnVOList) {
        BigDecimal max = Constants.PRICE_MIN;
        for (DnVO dnVO : dnVOList) {
            max = max.max(dnVO.getHp());
        }
        return max.setScale(0, RoundingMode.CEILING);
    }

}
