package top.zuoyu.mybatis;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Service;

import top.zuoyu.mybatis.annotation.Magic;
import top.zuoyu.mybatis.service.UnifyService;

@SpringBootTest
class EasyMybatisApplicationTests {

    @Resource

    @Magic("wechatinfo")
    private UnifyService unifyService;

    @Test
    void contextLoads() throws SQLException, MetaDataAccessException {

    }

}
