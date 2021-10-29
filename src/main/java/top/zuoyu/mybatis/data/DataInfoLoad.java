package top.zuoyu.mybatis.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
    private static String getCatalog(@NonNull Connection connection) {
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
    private static String getSchema(@NonNull Connection connection) {
        try {
            return connection.getSchema();
        } catch (SQLException e) {
            throw new CustomException("connection.getSchema() is fail!", e);
        }
    }

    /**
     * 获取表的所有信息
     *
     * @param connection - {@link Connection} 数据库连接
     * @param catalog    - catalog
     * @param schema     - 表数据库名
     * @param tableName  - 表名
     * @return {@link Table} 表信息
     */
    @NonNull
    public static Table getTableInfo(@NonNull Connection connection, String catalog, String schema, String tableName) {
        Table table = new Table();
        if (Strings.isEmpty(catalog)) {
            catalog = getCatalog(connection);
        }
        if (Strings.isEmpty(schema)) {
            schema = getSchema(connection);
        }


        try {
            final DatabaseMetaData databaseMetaData = connection.getMetaData();
//        获取表的元数据
            try (ResultSet tablesResultSet = databaseMetaData.getTables(catalog, schema, tableName, new String[]{TableType.TABLE.value()})) {
                if (Objects.nonNull(tablesResultSet)) {
                    if (tablesResultSet.next()) {
                        table.loadValuesByTablesResultSet(tablesResultSet);
                    }
                }
            }

//            获取表的主键
            try (ResultSet primaryKeysResultSet = databaseMetaData.getPrimaryKeys(catalog, schema, tableName)) {
                if (Objects.nonNull(primaryKeysResultSet)) {
                    while (primaryKeysResultSet.next()) {
                        table.addPrimaryKey(primaryKeysResultSet.getString(PRIMARY_KEY));
                    }
                }
            }

//            获取表的列
            try (ResultSet columnsResultSet = databaseMetaData.getColumns(catalog, schema, tableName, null)) {
                if (Objects.nonNull(columnsResultSet)) {
                    while (columnsResultSet.next()) {
                        Column.create(table, columnsResultSet);
                    }
                }
            }

//            获取表的索引
            try (ResultSet dataIndexInfo = databaseMetaData.getIndexInfo(catalog, schema, tableName, false, false)) {
                if (Objects.nonNull(dataIndexInfo)) {
                    while (dataIndexInfo.next()) {
                        Index.create(table, dataIndexInfo);
                    }
                }
            }

        } catch (SQLException e) {
            throw new CustomException("connection.getMetaData() is fail!", e);
        }
        return table;
    }

    /**
     * 获取所有表名称
     *
     * @param connection - {@link Connection} 数据库连接
     * @param catalog    - catalog
     * @param schema     - 表数据库名
     * @return 所有表名称
     */
    @NonNull
    public static List<String> getTableNames(@NonNull Connection connection, String catalog, String schema) {
        final List<String> tableNames = new ArrayList<>();
        if (Strings.isEmpty(catalog)) {
            catalog = getCatalog(connection);
        }
        if (Strings.isEmpty(schema)) {
            schema = getSchema(connection);
        }
        try {
            final DatabaseMetaData databaseMetaData = connection.getMetaData();
//        获取表的元数据
            try (ResultSet tablesResultSet = databaseMetaData.getTables(catalog, schema, "%", new String[]{TableType.TABLE.value()})) {
                if (Objects.nonNull(tablesResultSet)) {
                    while (tablesResultSet.next()) {
                        String tableName = tablesResultSet.getString(TableMeta.TABLE_NAME.value());
                        if (Strings.isNotBlank(tableName)) {
                            tableNames.add(tableName);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new CustomException("connection.getMetaData() is fail!", e);
        }
        return tableNames;
    }

    /**
     * 获取所有表信息
     *
     * @param connection - {@link Connection} 数据库连接
     * @return {@link Table} 所有表信息
     */
    @NonNull
    public static List<Table> getTables(@NonNull Connection connection) {
        final List<Table> tables = new ArrayList<>();
        final String catalog = getCatalog(connection);
        final String schema = getSchema(connection);
        List<String> tableNames = getTableNames(connection, catalog, schema);
        if (CollectionUtils.isEmpty(tableNames)) {
            return tables;
        }
        tableNames.forEach(tableName -> tables.add(getTableInfo(connection, catalog, schema, tableName)));
        return tables;
    }


}
