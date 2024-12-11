import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.pycurrent.util.JSONUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.Test;

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
        String time = "2024-07-17 09:15:00";
        log.info(time.substring(11, 19));
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
    public void test4() {
        List<Integer> res = Arrays.asList("53,4,99,36,20,67,86,47,32,90".split(",")).stream().map(Integer::parseInt).toList();
        int min = res.getFirst();
        int max = res.getFirst();
        int minIndex = 0;
        int maxIndex = 0;
        for (int i = 0; i < res.size(); i++) {
            if (min > res.get(i)) {
                min = res.get(i);
                minIndex = i;
            }
            if (max < res.get(i)) {
                max = res.get(i);
                maxIndex = i;
            }
        }
        if (minIndex != 0 && maxIndex != 0) {
            if (minIndex < maxIndex) {
                calR(0, minIndex);
            }
        }

    }

    private void calR(int start, int end) {

    }
}
