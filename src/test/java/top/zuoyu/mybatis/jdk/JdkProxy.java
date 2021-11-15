package top.zuoyu.mybatis.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.lang.NonNull;

/**
 * JDK动态代理 .
 *
 * @author: zuoyu
 * @create: 2021-11-15 10:29
 */
public class JdkProxy implements InvocationHandler {

    private final Object subject;

    public JdkProxy(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, @NonNull Method method, Object[] args) throws Throwable {
        return method.invoke(subject, args);
    }

    public Dog getDog() {
        Class<?> subjectClass = subject.getClass();
        return (Dog) Proxy.newProxyInstance(subjectClass.getClassLoader(), subjectClass.getInterfaces(), this);
    }
}
