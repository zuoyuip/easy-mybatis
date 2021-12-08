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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.data.support.TablesCallback;

/**
 * 构建初始化 .
 *
 * @author: zuoyu
 * @create: 2021-11-02 10:25
 */
public final class StructureInit {

    private static final Map<String, Class<?>> TABLE_NAME_CLASS = Collections.synchronizedMap(new HashMap<>());

    @NonNull
    public static Resource[] register(@NonNull DataSource dataSource) throws MetaDataAccessException {
        TABLE_NAME_CLASS.clear();
        List<Table> tables = JdbcUtils.extractDatabaseMetaData(dataSource, TablesCallback.getInstance());
        Resource[] resources = new Resource[tables.size()];
        for (int i = 0; i < tables.size(); i++) {
            String mapperXml = MapperXmlStructure.registerMapperXml(tables.get(i));
            InputStream mapperXmlInputStream = new ByteArrayInputStream(mapperXml.getBytes(StandardCharsets.UTF_8));
            resources[i] = new InputStreamResource(mapperXmlInputStream, tables.get(i).getTableName() + "MapperXmlInputStream");
            Class<?> mapperClass = MapperStructure.registerMapper(tables.get(i));
            TABLE_NAME_CLASS.put(tables.get(i).getTableName(), mapperClass);
        }
        return resources;
    }

    public static Map<String, Class<?>> getTableNameClass() {
        return TABLE_NAME_CLASS;
    }
}
