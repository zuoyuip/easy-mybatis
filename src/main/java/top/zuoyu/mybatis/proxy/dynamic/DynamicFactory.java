package top.zuoyu.mybatis.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import top.zuoyu.mybatis.service.MapperRepository;

/**
 * 动态工厂 .
 *
 * @author: zuoyu
 * @create: 2021-12-12 15:40
 */
public class DynamicFactory<T> implements FactoryBean<T> {

    private final Class<T> interfaceType;

    public DynamicFactory(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public T getObject() throws Exception {
        InvocationHandler invocationHandler = new DynamicProxy<>(interfaceType);
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), interfaceType.getInterfaces(), invocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return MapperRepository.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
