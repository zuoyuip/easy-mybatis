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
package top.zuoyu.mybatis.ssist;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.exception.EasyMybatisException;
import top.zuoyu.mybatis.utils.ClassUtil;
import top.zuoyu.mybatis.utils.StrUtil;
import top.zuoyu.mybatis.utils.VelocityUtils;

/**
 * mapper的xml构建 .
 *
 * @author: zuoyu
 * @create: 2021-11-01 14:25
 */
class XmlStructure {


    @NonNull
    static String registerMapperXml(@NonNull Table table, @NonNull String dateFormat, @NonNull String databaseProductName) {

        VelocityContext context = VelocityUtils.prepareContext(table, dateFormat);

        StringWriter stringWriter = new StringWriter();

        InputStream resourceInputStream = ClassUtil.getResourceInputStream(VelocityUtils.getXmlTemplate(databaseProductName));
        try {
            String stringFromStream = StrUtil.getStringFromStream(resourceInputStream);
            // 模板渲染
            Velocity.evaluate(context, stringWriter, "", stringFromStream);
        } catch (IOException e) {
            throw new EasyMybatisException(e.getMessage(), e);
        }
        System.out.println(stringWriter.toString());
        return stringWriter.toString();
    }
}
