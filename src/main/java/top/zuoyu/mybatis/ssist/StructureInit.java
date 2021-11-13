package top.zuoyu.mybatis.ssist;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
public class StructureInit {

    private static final List<String> TABLE_NAME_LIST = Collections.synchronizedList(new ArrayList<>());

    @NonNull
    public static Resource[] register(@NonNull DataSource dataSource) throws MetaDataAccessException {
        TABLE_NAME_LIST.clear();
        List<Table> tables = JdbcUtils.extractDatabaseMetaData(dataSource, TablesCallback.getInstance());
        Resource[] resources = new Resource[tables.size()];
        for (int i = 0; i < tables.size(); i++) {
//            ModelStructure.registerModel(table);
                String mapperXml = MapperXmlStructure.registerMapperXml(tables.get(i));
                InputStream mapperXmlInputStream = new ByteArrayInputStream(mapperXml.getBytes(StandardCharsets.UTF_8));
                resources[i] = new InputStreamResource(mapperXmlInputStream, tables.get(i).getTableName() + "mapperXmlInputStream");
                MapperStructure.registerMapper(tables.get(i));
                TABLE_NAME_LIST.add(tables.get(i).getTableName());
        }
        return resources;
    }

    public static List<String> getTableNameList() {
        return TABLE_NAME_LIST;
    }
}
