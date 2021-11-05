package top.zuoyu.mybatis.cglib;

import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 测试 .
 *
 * @author: zuoyu
 * @create: 2021-11-03 16:45
 */
public class TestMethod {

    @Test
    public void testCglib() {
        CGLibProxy cgLibProxy = new CGLibProxy();
        Say ben = cgLibProxy.getBen(SayHello.class);
        ben.say();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        ObjectMapper objectMapper = new ObjectMapper();

    }
}
