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
package top.zuoyu.mybatis.proxy.cglib;

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
class CglibProxy implements MethodInterceptor {

    private final Enhancer enhancer = new Enhancer();

    private void before(Object o, Method method, Object[] args) {

    }

    private void after(Object o, Method method, Object[] args) {

    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, @NonNull MethodProxy methodProxy) throws Throwable {
        before(o, method, args);
        Object invokeSuper = methodProxy.invokeSuper(o, args);
        after(o, method, args);
        return invokeSuper;
    }



    public UnifyService getBean(Class<?> beanClass) {
        this.enhancer.setSuperclass(beanClass);
        this.enhancer.setInterfaces(new Class[]{UnifyService.class});
        this.enhancer.setCallback(this);
        return (UnifyService) this.enhancer.create();
    }
}
