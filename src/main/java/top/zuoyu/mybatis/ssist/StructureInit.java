package top.zuoyu.mybatis.ssist;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.data.DataInfoLoad;
import top.zuoyu.mybatis.data.model.Table;

/**
 * 构建初始化 .
 *
 * @author: zuoyu
 * @create: 2021-11-02 10:25
 */
public class StructureInit {

    private static final List<String> TABLE_NAME_LIST = Collections.synchronizedList(new ArrayList<>());

    public static void register(@NonNull DataSource dataSource) throws SQLException {
        TABLE_NAME_LIST.clear();
        Connection connection = dataSource.getConnection();
        List<Table> tables = DataInfoLoad.getTables(connection);
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
