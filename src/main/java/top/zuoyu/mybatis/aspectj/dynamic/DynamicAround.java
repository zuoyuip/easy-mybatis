package top.zuoyu.mybatis.aspectj.dynamic;

import java.lang.reflect.Method;

import org.springframework.lang.NonNull;

/**
 * 动态代理方法环绕 .
 *
 * @author: zuoyu
 * @create: 2021-11-15 10:53
 */
public interface DynamicAround {

    /**
     * 前置方法
     * @param proxy - 代理对象
     * @param method - 代理方法
     * @param args - 代理参数
     */
    void before(Object proxy, @NonNull Method method, Object[] args);

    /**
     * 后置方法
     * @param proxy - 代理对象
     * @param method - 代理方法
     * @param args - 代理参数
     */
    void after(Object proxy, @NonNull Method method, Object[] args);
}
