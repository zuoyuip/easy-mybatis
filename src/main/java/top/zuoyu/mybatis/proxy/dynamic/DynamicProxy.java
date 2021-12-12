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
package top.zuoyu.mybatis.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.lang.NonNull;

/**
 * JDK动态代理 .
 *
 * @author: zuoyu
 * @create: 2021-11-15 10:48
 */
class DynamicProxy<T> implements InvocationHandler {

    private final Class<T> interfaceType;

    private final DynamicAround dynamicAround;

    public DynamicProxy(Class<T> intefaceType) {
        this.interfaceType = intefaceType;
        this.dynamicAround = new DynamicAround() {
            @Override
            public void before(Object proxy, Method method, Object[] args) {
            }

            @Override
            public void after(Object proxy, Method method, Object[] args) {
            }
        };
    }

    @Override
    public Object invoke(Object proxy, @NonNull Method method, Object[] args) throws Throwable {
        dynamicAround.before(proxy, method, args);
        Object invoke = method.invoke(this, args);
        dynamicAround.after(proxy, method, args);
        return invoke;
    }

}
