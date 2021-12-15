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
package top.zuoyu.mybatis.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import top.zuoyu.mybatis.data.enums.TableMeta;
import top.zuoyu.mybatis.data.enums.TableType;
import top.zuoyu.mybatis.data.model.Column;
import top.zuoyu.mybatis.data.model.Index;
import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.exception.CustomException;

/**
 * 数据库元数据加载 .
 *
 * @author: zuoyu
 * @create: 2021-10-16 16:26
 */
public class DataInfoLoad {

    private static final String PRIMARY_KEY = "COLUMN_NAME";

    /**
     * 获取catalog
     *
     * @param connection - {@link Connection} 数据库连接
     * @return catalog
     */
    public static String getCatalog(@NonNull Connection connection) {
        try {
            return connection.getCatalog();
        } catch (SQLException e) {
            throw new CustomException("connection.getCatalog() is fail!", e);
        }
    }

    /**
     * 获取schema
     *
     * @param connection - {@link Connection} 数据库连接
     * @return schema
     */
    public static String getSchema(@NonNull Connection connection) {
        try {
            return connection.getSchema();
        } catch (SQLException e) {
            throw new CustomException("connection.getSchema() is fail!", e);
        }
    }

    /**
     * 获取catalog
     *
     * @param databaseMetaData - {@link DatabaseMetaData} 数据库元信息
     * @return catalog
     */
    public static String getCatalog(@NonNull DatabaseMetaData databaseMetaData) {
        try {
            return getCatalog(databaseMetaData.getConnection());
        } catch (SQLException e) {
            throw new CustomException("databaseMetaData.getConnection() is fail!", e);
        }
    }

    /**
     * 获取数据库类型
     *
     * @param databaseMetaData - {@link DatabaseMetaData} 数据库元信息
     * @return schema
     */
    public static String getDatabaseProductName(@NonNull DatabaseMetaData databaseMetaData) {
        try {
            return databaseMetaData.getDatabaseProductName();
        } catch (SQLException e) {
            throw new CustomException("databaseMetaData.getConnection() is fail!", e);
        }
    }

    /**
     * 获取schema
     *
     * @param databaseMetaData - {@link DatabaseMetaData} 数据库元信息
     * @return schema
     */
    public static String getSchema(@NonNull DatabaseMetaData databaseMetaData) {
        try {
            return getSchema(databaseMetaData.getConnection());
        } catch (SQLException e) {
            throw new CustomException("databaseMetaData.getConnection() is fail!", e);
        }
    }

    /**
     * 获取表的所有信息
     *
     * @param databaseMetaData - {@link DatabaseMetaData} 数据库元信息
     * @param tableName        - 表名
     * @return {@link Table} 表信息
     */
    @NonNull
    private static Table getTableInfo(@NonNull final DatabaseMetaData databaseMetaData, String tableName) throws SQLException {
        Table table = new Table();


//        获取表的元数据
        try (ResultSet tablesResultSet = databaseMetaData.getTables(getCatalog(databaseMetaData), getSchema(databaseMetaData), tableName, new String[]{TableType.TABLE.value()})) {
            if (Objects.nonNull(tablesResultSet)) {
                if (tablesResultSet.next()) {
                    table.loadValuesByTablesResultSet(tablesResultSet);
                }
            }
        }

//            获取表的主键
        try (ResultSet primaryKeysResultSet = databaseMetaData.getPrimaryKeys(getCatalog(databaseMetaData), getSchema(databaseMetaData), tableName)) {
            if (Objects.nonNull(primaryKeysResultSet)) {
                while (primaryKeysResultSet.next()) {
                    table.addPrimaryKey(primaryKeysResultSet.getString(PRIMARY_KEY));
                }
            }
        }

//            获取表的列
        try (ResultSet columnsResultSet = databaseMetaData.getColumns(getCatalog(databaseMetaData), getSchema(databaseMetaData), tableName, null)) {
            if (Objects.nonNull(columnsResultSet)) {
                while (columnsResultSet.next()) {
                    Column.create(table, columnsResultSet);
                }
            }
        }

//            获取表的索引
        try (ResultSet dataIndexInfo = databaseMetaData.getIndexInfo(getCatalog(databaseMetaData), getSchema(databaseMetaData), tableName, false, false)) {
            if (Objects.nonNull(dataIndexInfo)) {
                while (dataIndexInfo.next()) {
                    Index.create(table, dataIndexInfo);
                }
            }
        }

        return table;
    }

    /**
     * 获取所有表名
     *
     * @param databaseMetaData - {@link DatabaseMetaData} 数据库元数据
     * @return 所有名称
     */
    @NonNull
    protected static List<String> getTableNames(@NonNull DatabaseMetaData databaseMetaData) throws SQLException {
        final List<String> tableNames = new ArrayList<>();

        //        获取表的元数据
        try (ResultSet tablesResultSet = databaseMetaData.getTables(getCatalog(databaseMetaData), getSchema(databaseMetaData), "%", new String[]{TableType.TABLE.value()})) {
            if (Objects.nonNull(tablesResultSet)) {
                while (tablesResultSet.next()) {
                    String tableName = tablesResultSet.getString(TableMeta.TABLE_NAME.value());
                    if (Strings.isNotBlank(tableName)) {
                        tableNames.add(tableName);
                    }
                }
            }
        }
        return tableNames;
    }

    /**
     * 获取所有表信息
     *
     * @param databaseMetaData - {@link DatabaseMetaData} 数据库元数据
     * @return {@link Table} 所有表信息
     */
    @NonNull
    protected static List<Table> getTables(@NonNull DatabaseMetaData databaseMetaData) throws SQLException {
        final List<Table> tables = new ArrayList<>();
        List<String> tableNames = getTableNames(databaseMetaData);
        if (CollectionUtils.isEmpty(tableNames)) {
            return tables;
        }
        for (String tableName : tableNames) {
            tables.add(getTableInfo(databaseMetaData, tableName));
        }
        return tables;
    }

    /**
     * 获取指定表信息
     *
     * @param databaseMetaData - {@link DatabaseMetaData} 数据库元数据
     * @param tableNames       - 指定表名
     * @return {@link Table} 所有表信息
     */
    @NonNull
    protected static List<Table> getTables(@NonNull DatabaseMetaData databaseMetaData, List<String> tableNames) throws SQLException {
        final List<Table> tables = new ArrayList<>();
        if (CollectionUtils.isEmpty(tableNames)) {
            return tables;
        }
        for (String tableName : tableNames) {
            tables.add(getTableInfo(databaseMetaData, tableName));
        }
        return tables;
    }


}
