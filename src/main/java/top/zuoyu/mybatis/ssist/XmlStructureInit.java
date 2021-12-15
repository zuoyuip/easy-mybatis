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
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.data.support.DatabaseProductNameCallback;
import top.zuoyu.mybatis.data.support.TableNamesCallback;
import top.zuoyu.mybatis.data.support.TablesCallback;

/**
 * 构建初始化 .
 *
 * @author: zuoyu
 * @create: 2021-11-02 10:25
 */
public final class XmlStructureInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlStructureInit.class);

    @NonNull
    public static Resource[] register(@NonNull DataSource dataSource, @NonNull String[] tableNames) throws MetaDataAccessException {
        List<String> allTableNames = JdbcUtils.extractDatabaseMetaData(dataSource, TableNamesCallback.getInstance());
        if (ArrayUtils.isEmpty(tableNames)) {
            LOGGER.warn(() -> "No tables was found, please check your configuration.");
            return new Resource[0];
        }
        List<String> tableNameList = new ArrayList<>();
        for (String tableName : tableNames) {
            if (allTableNames.contains(tableName)) {
                tableNameList.add(tableName);
                continue;
            }
            LOGGER.warn(() -> "table:" + tableName + " was found, please check your configuration.");
        }
        String databaseProductName = JdbcUtils.extractDatabaseMetaData(dataSource, DatabaseProductNameCallback.getInstance());
        List<Table> tables = JdbcUtils.extractDatabaseMetaData(dataSource, TablesCallback.getInstance(tableNameList));
        Resource[] resources = new Resource[tables.size()];
        for (int i = 0; i < tables.size(); i++) {
            String mapperXml = XmlStructure.registerMapperXml(tables.get(i), databaseProductName);
            InputStream mapperXmlInputStream = new ByteArrayInputStream(mapperXml.getBytes(StandardCharsets.UTF_8));
            resources[i] = new InputStreamResource(mapperXmlInputStream, tables.get(i).getTableName() + "MapperXmlInputStream");
        }
        return resources;
    }

}
