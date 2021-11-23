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
