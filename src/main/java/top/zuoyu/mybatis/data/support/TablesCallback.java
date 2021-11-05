package top.zuoyu.mybatis.data.support;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.support.MetaDataAccessException;

import top.zuoyu.mybatis.data.DataInfoLoad;
import top.zuoyu.mybatis.data.model.Table;

/**
 * 获取表信息 .
 *
 * @author: zuoyu
 * @create: 2021-11-05 09:41
 */
public class TablesCallback extends DataInfoLoad implements org.springframework.jdbc.support.DatabaseMetaDataCallback<List<Table>> {

    private volatile static TablesCallback TABLES_CALLBACK;

    public static TablesCallback getInstance() {
        if (Objects.isNull(TABLES_CALLBACK)) {
            synchronized (TablesCallback.class) {
                if (Objects.isNull(TABLES_CALLBACK)) {
                    TABLES_CALLBACK = new TablesCallback();
                }
            }
        }
        return TABLES_CALLBACK;
    }

    @Override
    public List<Table> processMetaData(DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {
        return getTables(databaseMetaData);
    }
}
