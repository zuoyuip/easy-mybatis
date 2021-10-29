package top.zuoyu.mybatis.autoconfigure;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.data.DataInfoLoad;
import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.ssist.ModelStructure;

/**
 * 自动装配 .
 *
 * @author: zuoyu
 * @create: 2021-10-29 16:55
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnSingleCandidate(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class EasyMybatisAutoConfiguration implements InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("--------------------------------------------------------");

    }


    public void initModels(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        List<Table> tables = DataInfoLoad.getTables(connection);
        tables.forEach(ModelStructure::registerModel);
    }
}
