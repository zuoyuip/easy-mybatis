package top.zuoyu.mybatis.cglib;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.NonNull;

/**
 * CGLib动态代理 .
 *
 * @author: zuoyu
 * @create: 2021-11-03 16:28
 */
public class CGLibProxy implements MethodInterceptor {

    public Enhancer enhancer = new Enhancer();

    @Override
    public Object intercept(Object o, Method method, Object[] objects, @NonNull MethodProxy methodProxy) throws Throwable {
        Object invokeSuper = methodProxy.invokeSuper(o, objects);
        return invokeSuper;
    }

    public Say getBen(Class<?> cls) {
        enhancer.setSuperclass(cls);
        enhancer.setInterfaces(new Class[]{Say.class});
        enhancer.setCallback(this);
        return (Say) enhancer.create();
    }
}
