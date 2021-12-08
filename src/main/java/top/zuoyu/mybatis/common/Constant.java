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
package top.zuoyu.mybatis.common;

import java.io.File;

import org.springframework.util.ClassUtils;

import top.zuoyu.mybatis.temp.mapper.BaseMapper;
import top.zuoyu.mybatis.temp.model.BaseModel;

/**
 * 常量 .
 *
 * @author: zuoyu
 * @create: 2021-11-02 11:02
 */
public interface Constant {

    /**
     * 包间隔符
     */
    CharSequence PACKAGE_SEPARATOR = ".";

    /**
     * 名称间隔符
     */
    CharSequence NAME_SEPARATOR = "_";

    /**
     * 通配符
     */
    CharSequence WILDCARD_SEPARATOR = "*";

    /**
     * Mapper.xml后缀
     */
    String MAPPER_XML_SUFFIX = "%sMapper.xml";

    /**
     * Mapper后缀
     */
    String MAPPER_SUFFIX = "%sMapper";

    String YES = "YES";
    String NO = "NO";
    String NULL = "null";

    /**
     * mybatis的xml包路径
     */
    String MAPPER_XML_DIR_NAME = "zuoyu" + File.separator + "mapper";

    /**
     * mybatis的接口包路径
     */
    String MAPPER_PACKAGE_NAME = ClassUtils.getPackageName(BaseMapper.class);

    /**
     * model的路径
     */
    String MODEL_PACKAGE_NAME = ClassUtils.getPackageName(BaseModel.class);
}
