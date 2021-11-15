package top.zuoyu.mybatis.aspectj.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.exception.EasyMybatisException;
import top.zuoyu.mybatis.service.UnifyService;

/**
 * JDK动态代理 .
 *
 * @author: zuoyu
 * @create: 2021-11-15 10:48
 */
public class DynamicProxy implements InvocationHandler {

    private final Object subject;

    private final DynamicAround dynamicAround;

    public DynamicProxy(Object subject, DynamicAround dynamicAround) {
        this.subject = subject;
        this.dynamicAround = dynamicAround;
    }

    public DynamicProxy(Object subject) {
        this.subject = subject;
        this.dynamicAround = new DynamicAround() {
            @Override
            public void before(Object proxy, Method method, Object[] args) {}

            @Override
            public void after(Object proxy, Method method, Object[] args) {}
        };
    }

    @Override
    public Object invoke(Object proxy, @NonNull Method method, Object[] args) throws Throwable {
        dynamicAround.before(proxy, method, args);
        Object invoke = method.invoke(subject, args);
        dynamicAround.after(proxy, method, args);
        return invoke;
    }

    public UnifyService getUnifyService() {
        Class<?> subjectClass = subject.getClass();
        Object proxyInstance = Proxy.newProxyInstance(subjectClass.getClassLoader(), subjectClass.getInterfaces(), this);
        if (proxyInstance instanceof UnifyService) {
            return (UnifyService) proxyInstance;
        }
        throw new EasyMybatisException(subjectClass.getTypeName() + " can't cast " + UnifyService.class.getTypeName());
    }
}
