package top.zuoyu.mybatis.jdk;

import org.junit.jupiter.api.Test;

/**
 * 测试方法 .
 *
 * @author: zuoyu
 * @create: 2021-11-15 10:36
 */
public class TestMethod {

    @Test
    public void testJdk(){
        Dog dog = new JdkProxy(new DogImpl()).getDog();
        dog.eat();
    }
}
