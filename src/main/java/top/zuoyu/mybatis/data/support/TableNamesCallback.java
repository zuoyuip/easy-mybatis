package top.zuoyu.mybatis.data.support;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.data.DataInfoLoad;

/**
 * 获取表名 .
 *
 * @author: zuoyu
 * @create: 2021-11-04 10:00
 */
public class TableNamesCallback extends DataInfoLoad implements org.springframework.jdbc.support.DatabaseMetaDataCallback<List<String>> {


    private volatile static TableNamesCallback TABLE_NAMES_CALLBACK;

    public static TableNamesCallback getInstance() {
        if (Objects.isNull(TABLE_NAMES_CALLBACK)) {
            synchronized (TableNamesCallback.class) {
                if (Objects.isNull(TABLE_NAMES_CALLBACK)) {
                    TABLE_NAMES_CALLBACK = new TableNamesCallback();
                }
            }
        }
        return TABLE_NAMES_CALLBACK;
    }

    @Override
    public List<String> processMetaData(@NonNull DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {
        return getTableNames(databaseMetaData);
    }
}
