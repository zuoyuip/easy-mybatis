/*
 * Copyright (c) 2021, zuoyu (zuoyuip@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.zuoyu.mybatis.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.exception.EasyMybatisException;
import top.zuoyu.mybatis.service.MapperRepository;

/**
 * JDK动态代理 .
 *
 * @author: zuoyu
 * @create: 2021-11-15 10:48
 */
class DynamicProxy implements InvocationHandler {

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

    public MapperRepository getUnifyService() {
        Class<?> subjectClass = subject.getClass();
        Object proxyInstance = Proxy.newProxyInstance(subjectClass.getClassLoader(), subjectClass.getInterfaces(), this);
        if (proxyInstance instanceof MapperRepository) {
            return (MapperRepository) proxyInstance;
        }
        throw new EasyMybatisException(subjectClass.getTypeName() + " can't cast " + MapperRepository.class.getTypeName());
    }
}
