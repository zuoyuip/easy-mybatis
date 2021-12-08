/*
 * MIT License
 *
 * Copyright (c) 2021 zuoyuip@foxmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package top.zuoyu.mybatis.proxy.cglib;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.service.MapperRepository;

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



    public MapperRepository getBean(Class<?> beanClass) {
        this.enhancer.setSuperclass(beanClass);
        this.enhancer.setInterfaces(new Class[]{MapperRepository.class});
        this.enhancer.setCallback(this);
        return (MapperRepository) this.enhancer.create();
    }
}
