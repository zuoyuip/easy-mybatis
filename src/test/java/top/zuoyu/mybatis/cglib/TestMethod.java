package top.zuoyu.mybatis.cglib;

import org.junit.jupiter.api.Test;

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

    }
}
