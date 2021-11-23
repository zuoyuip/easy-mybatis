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
package top.zuoyu.mybatis.utils;

import org.springframework.lang.NonNull;

/**
 * 字符串工具类 .
 *
 * @author: zuoyu
 * @create: 2021-10-17 17:11
 */
public class StrUtil {

    /**
     * 将字符串的首字母转大写
     *
     * @param str 需要转换的字符串
     * @return 转换的字符串
     */
    @NonNull
    public static String captureName(@NonNull String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = str.toCharArray();
        if (Character.isLowerCase(cs[0])) {
            cs[0] -= 32;
            return String.valueOf(cs);
        }
        return str;
    }
}
