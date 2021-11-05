package top.zuoyu.mybatis.ssist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

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
public class StructureInit {

    private static final List<String> TABLE_NAME_LIST = Collections.synchronizedList(new ArrayList<>());

    public static void register(@NonNull DataSource dataSource) throws MetaDataAccessException {
        TABLE_NAME_LIST.clear();
        List<Table> tables = JdbcUtils.extractDatabaseMetaData(dataSource, TablesCallback.getInstance());
        tables.forEach(table -> {
            ModelStructure.registerModel(table);
            MapperXmlStructure.registerMapperXml(table);
            MapperStructure.registerMapper(table);
            TABLE_NAME_LIST.add(table.getTableName());
        });
    }

    public static List<String> getTableNameList() {
        return TABLE_NAME_LIST;
    }
}
