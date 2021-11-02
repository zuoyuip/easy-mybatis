package top.zuoyu.mybatis.controller;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import top.zuoyu.mybatis.utils.ClassUtil;

/**
 * 测试 .
 *
 * @author: zuoyu
 * @create: 2021-10-29 11:15
 */
@RestController
@RequestMapping("test")
public class TestController {

    private final SqlSessionFactory sqlSessionFactory;

    public TestController(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @GetMapping
    public void testSqlSessionFactory() {


        try {
            Class<?> aClass = Class.forName("top.zuoyu.mybatis.temp.mapper.WechatinfoMapper");
            SqlSession sqlSession = sqlSessionFactory.openSession();
            Object mapper = sqlSession.getMapper(aClass);
            Class<?> mapperClass = mapper.getClass();
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(ClassUtil.getBasePath().getPath());
            TypeAliasRegistry typeAliasRegistry = sqlSession.getConfiguration().getTypeAliasRegistry();
            Map<String, Class<?>> typeAliases = typeAliasRegistry.getTypeAliases();
            Class<?> wechatinfoClass = typeAliases.get("wechatinfo");
            Method selectList = ReflectionUtils.findMethod(mapperClass, "selectList", wechatinfoClass);
            List o = (List) ReflectionUtils.invokeMethod(selectList, mapper, wechatinfoClass.newInstance());
            ObjectMapper objectMapper = new ObjectMapper();
            o.forEach(o1 -> {
                try {
                    String s = objectMapper.writeValueAsString(o1);
                    System.out.println(s);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });

        } catch (ClassNotFoundException | NotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }



    }

}
