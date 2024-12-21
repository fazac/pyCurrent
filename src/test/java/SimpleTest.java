import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.util.JSONUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author fzc
 * @date 2024/6/30 16:45
 * @description
 */
@CommonsLog
public class SimpleTest {
    @Test
    public void test1() {
        ObjectNode objectNode = JSONUtils.getNode();
        objectNode.put("start_date", "20240629");
        objectNode.put("end_date", "20240630");
        objectNode.put("adjust", "");
        objectNode.put("period", "daily");
        log.info(JSONUtils.toDoubleJson(objectNode));
    }

    @Test
    public void test2() {
//        String time = "2024-07-17 09:15:00";
//        log.info(time.substring(11, 19));
//        BigDecimal a = BigDecimal.ZERO;
//        BigDecimal b = BigDecimal.ZERO;
//        for (int i = 0; i < 10; i++) {
//            b = a;
//            a = a.add(BigDecimal.valueOf(i));
//            log.info(b.toPlainString());
//            log.info("hei");
//            log.info(a.toPlainString());
//        }
//        log.info("voe");
//        Long c = 0L;
//        Long d = 0L;
//        for (int i = 0; i < 10; i++) {
//            c = d;
//            d = d + i;
//            log.info(c);
//            log.info("hei");
//            log.info(d);
//        }
        log.info(calAvgLevelPrice(BigDecimal.valueOf(3784240477.46), BigDecimal.valueOf(16.44), 911007L,
                BigDecimal.valueOf(3784240477.46).add(BigDecimal.valueOf(2944832923.00)), BigDecimal.valueOf(16.44 + 13.74), 761793L+911007L, BigDecimal.valueOf(20)));

    }

    private BigDecimal calAvgPrice(BigDecimal amount, Long vol) {
        return amount.divide(BigDecimal.valueOf(vol).multiply(Constants.HUNDRED), 3, RoundingMode.HALF_UP);
    }

    private BigDecimal calAvgLevelPrice(BigDecimal leftAmount, BigDecimal leftHand, Long leftVol,
                                        BigDecimal rightAmount, BigDecimal rightHand, Long rightVol,
                                        BigDecimal level) {
        BigDecimal rightPrice = calAvgPrice(rightAmount, rightVol);
        if (leftVol == 0) {
            return rightPrice;
        }
        BigDecimal leftPrice = calAvgPrice(leftAmount, leftVol);
        return rightPrice.subtract(leftPrice).divide(rightHand.subtract(leftHand), 3, RoundingMode.HALF_UP)
                .multiply(level.subtract(leftHand)).add(leftPrice);
    }

    @Test
    public void test3() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            res.add(random.nextInt(100));
        }
        log.info(JSONUtils.toJson(res));
    }
    @Test
    public void test5(){
        log.info(LocalDateTime.now());
    }

}
