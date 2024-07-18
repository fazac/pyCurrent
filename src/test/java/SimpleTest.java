import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.pycurrent.util.JSONUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.Test;

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
}
