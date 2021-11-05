package top.zuoyu.mybatis;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.data.support.TablesCallback;

@SpringBootTest
class EasyMybatisApplicationTests {

    @Autowired
    DataSource dataSource;


    @Test
    void contextLoads() throws SQLException, MetaDataAccessException {


        List<Table> tables = JdbcUtils.extractDatabaseMetaData(dataSource, TablesCallback.getInstance());
        tables.forEach(table -> table.getColumns().forEach(System.out::println));
    }

}
