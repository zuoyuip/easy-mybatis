package top.zuoyu.mybatis.controller;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.ClassPool;
import javassist.NotFoundException;
import top.zuoyu.mybatis.annotation.Magic;
import top.zuoyu.mybatis.aspectj.cglib.CglibProxy;
import top.zuoyu.mybatis.aspectj.cglib.Mappers;
import top.zuoyu.mybatis.json.JsonObject;
import top.zuoyu.mybatis.service.UnifyService;
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

    @Magic("wechatinfo")
    private UnifyService unifyService;

    private final SqlSession sqlSession;

    public TestController(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @GetMapping
    public void testSqlSessionFactory() {


        try {
            Class<?> aClass = Class.forName("top.zuoyu.mybatis.temp.mapper.WechatinfoMapper");
            Object mapper = sqlSession.getMapper(aClass);
//            Class<?> mapperClass = mapper.getClass();
                UnifyService unifyService = (UnifyService) mapper;
                unifyService.selectList().forEach(System.out::println);
//            CglibProxy cglibProxy = new CglibProxy();
//            UnifyService unifyService = cglibProxy.getBean(mapperClass);
//            ClassPool classPool = ClassPool.getDefault();
//            classPool.appendClassPath(ClassUtil.getBasePath().getPath());
//            TypeAliasRegistry typeAliasRegistry = sqlSession.getConfiguration().getTypeAliasRegistry();
//            Map<String, Class<?>> typeAliases = typeAliasRegistry.getTypeAliases();
//            Class<?> wechatinfoClass = typeAliases.get("jsonobject");
//            Method selectList = ReflectionUtils.findMethod(mapperClass, "selectList");
//            List<JsonObject> o = (List) ReflectionUtils.invokeMethod(selectList, mapper);
//            o.forEach(System.out::println);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    @GetMapping("service")
    public void testUnifyService(){
        UnifyService unifyService = Mappers.getUnifyService("wechatinfo");
        unifyService.selectListByExample(new JsonObject()).forEach(System.out::println);
    }

}
