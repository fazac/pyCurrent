package com.stock.pycurrent.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.pycurrent.entity.CodeLabel;
import com.stock.pycurrent.entity.CurConcernCode;
import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.entity.LimitCode;
import com.stock.pycurrent.entity.annotation.RequestLimit;
import com.stock.pycurrent.entity.jsonvalue.LimitCodeValue;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.entity.vo.DnVO;
import com.stock.pycurrent.entity.vo.LimitCodeVO;
import com.stock.pycurrent.entity.vo.OpenVO;
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
    private BoardIndustryConService boardIndustryConService;
    @Resource
    private BoardConceptConService boardConceptConService;
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

    @GetMapping("findDataByCode")
    @RequestLimit(key = "limit0",
            permitsPerSecond = 1,
            timeout = 500,
            msg = "访问频率已超限制")
    public ObjectNode findDataByCode(@Param("code") String code) {
        ObjectNode objectNode = JSONUtils.getNode();
        List<OpenVO> openVOList = ArrayUtils.convertOpenVO(emRealTimeStockService.findOpenByCode(code));
        List<String> labels = getLabelsByCode(code);
        labels.addFirst(openVOList.getFirst().getName());
        objectNode.putPOJO("label", labels);
        objectNode.putPOJO("open", openVOList);
        objectNode.putPOJO("dnDetail", emDNStockService.findByCodeCount(code, 30));
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
        List<DnVO> dnVOList = ArrayUtils.convertDnVO(emDNStockService.findByCodeCount(code, 30).reversed());
        objectNode.putPOJO("dnData", dnVOList);
        objectNode.putPOJO("dnPriMin", findPriMin(dnVOList));
        objectNode.putPOJO("dnPriMax", findPriMax(dnVOList));
        return objectNode;
    }

    @GetMapping("findOtherList")
    public List<CurConcernCode> findOtherList() {
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
                LimitCode limitCode = limitCodeService.findByDate(DateUtils.convertMillisecond(searchDate));
                List<LimitCodeVO> limitCodeVOList = new ArrayList<>();
                log.warn(new Date().getTime());
                if (limitCode != null && limitCode.getCodeValue() != null && !limitCode.getCodeValue().isEmpty()) {
                    List<LimitCodeValue> tmp = limitCode.getCodeValue();
                    tmp.sort(Comparator.comparing(LimitCodeValue::getCount));
                    tmp = tmp.reversed();
                    tmp.forEach(x -> {
                        LimitCodeVO limitCodeVO = new LimitCodeVO();
                        limitCodeVO.setCode(x.getCode());
                        limitCodeVO.setCount(x.getCount());
                        convertLimitVO(x.getCode(), limitCodeVO);
                        limitCodeVOList.add(limitCodeVO);
                    });
                }
                log.warn(new Date().getTime());
                objectNode.putPOJO("limitCodeVOList", limitCodeVOList);
            }
            //ophc
            case "4" -> {
                List<LimitCodeVO> limitCodeVOList = emDNStockService.findOPHC(count,
                        hand == null ? null : BigDecimal.valueOf(hand),
                        pch == null ? null : BigDecimal.valueOf(pch));
                if (limitCodeVOList != null && !limitCodeVOList.isEmpty()) {
                    limitCodeVOList.forEach(x -> convertLimitVO(x.getCode(), x));
                }
                objectNode.putPOJO("limitCodeVOList", limitCodeVOList);
            }
            //roc
            case "5" -> {
                List<LimitCodeVO> limitCodeVOList = rocModelService.findRocByLimit(
                        r2HighLimit == null ? null : BigDecimal.valueOf(r2HighLimit).negate(),
                        r2LowLimit == null ? null : BigDecimal.valueOf(r2LowLimit).negate(),
                        r1LowLimit == null ? null : BigDecimal.valueOf(r1LowLimit).negate(),
                        r1HighLimit == null ? null : BigDecimal.valueOf(r1HighLimit).negate()
                );
                if (limitCodeVOList != null && !limitCodeVOList.isEmpty()) {
                    convertROCLimitVO(limitCodeVOList);
                    limitCodeVOList = limitCodeVOList.stream().filter(x -> x.getCap() != null).toList();
                }
                log.warn("5: " + new Date().getTime());
                objectNode.putPOJO("limitCodeVOList", limitCodeVOList);
            }
        }
        return objectNode;
    }

    private void convertLimitVO(@RequestParam(value = "code", required = false) String code, LimitCodeVO limitCodeVO) {
        EmRealTimeStock emRealTimeStock = emRealTimeStockService.findOpenByCode(code).getFirst();
        limitCodeVO.setPe(emRealTimeStock.getPe());
        limitCodeVO.setPb(emRealTimeStock.getPb());
        if (emRealTimeStock.getCirculationMarketCap() != null) {
            limitCodeVO.setCap(emRealTimeStock.getCirculationMarketCap().divide(Constants.ONE_HUNDRED_MILLION, 2, RoundingMode.HALF_UP));
        }
        List<String> labels = getLabelsByCode(code);
        labels.addFirst(emRealTimeStock.getName());
        limitCodeVO.setLabels(String.join(",", labels));
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
                List<String> labels = getLabelsByCode(x.getCode());
                labels.addFirst(emRealTimeStock.getName());
                x.setLabels(String.join(",", labels));
            });
        }
    }

    private List<String> getLabelsByCode(@RequestParam(value = "code", required = false) String code) {
        List<String> labels = new ArrayList<>();
        CodeLabel codeLabel = codeLabelService.findByCode(code);
        if (codeLabel != null) {
            if (codeLabel.getConcept() != null && !codeLabel.getConcept().isEmpty()) {
                labels.addAll(Arrays.asList(codeLabel.getConcept().split(",")));
            }
            if (codeLabel.getIndustry() != null && !codeLabel.getIndustry().isEmpty()) {
                labels.addFirst(codeLabel.getIndustry());
            }
        }
        return labels;
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
