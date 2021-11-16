/*
 * Copyright (c) 2021, zuoyu (zuoyuip@foxmil.com).
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
package top.zuoyu.mybatis.utils;

import java.net.URL;

import org.apache.logging.log4j.util.Strings;

/**
 * 类工具包 .
 *
 * @author: zuoyu
 * @create: 2021-10-17 16:28
 */
public class ClassUtil {

    public static URL getBasePath() {
        return ClassUtil.class.getClassLoader().getResource(Strings.EMPTY);
    }
}
