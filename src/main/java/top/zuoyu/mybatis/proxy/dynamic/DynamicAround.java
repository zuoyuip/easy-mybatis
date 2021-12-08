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
