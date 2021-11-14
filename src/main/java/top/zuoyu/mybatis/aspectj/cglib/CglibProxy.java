package top.zuoyu.mybatis.aspectj.cglib;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.service.UnifyService;

/**
 * CGLib动态代理 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 13:32
 */
public class CglibProxy implements MethodInterceptor {

    private final Enhancer enhancer = new Enhancer();

    @Override
    public Object intercept(Object o, Method method, Object[] objects, @NonNull MethodProxy methodProxy) throws Throwable {
        return methodProxy.invokeSuper(o, objects);
    }

    public UnifyService getBean(Class<?> beanClass) {
        this.enhancer.setSuperclass(beanClass);
        this.enhancer.setCallback(this);
        return (UnifyService) this.enhancer.create();
    }
}
