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
package top.zuoyu.mybatis.utils;

import java.io.InputStream;
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

    public static URL getResource() {
        return ClassUtil.class.getResource(Strings.EMPTY);
    }

    public static URL getBasePath(String file) {
        return ClassUtil.class.getClassLoader().getResource(file);
    }

    public static URL getResource(String file) {
        return ClassUtil.class.getResource(file);
    }

    public static InputStream getResourceInputStream(String file) {
        return ClassUtil.class.getClassLoader().getResourceAsStream(file);
    }


}
