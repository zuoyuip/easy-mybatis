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
