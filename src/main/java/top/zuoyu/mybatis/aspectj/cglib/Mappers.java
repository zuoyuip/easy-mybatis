package top.zuoyu.mybatis.aspectj.cglib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.springframework.util.CollectionUtils;

import top.zuoyu.mybatis.exception.EasyMybatisException;
import top.zuoyu.mybatis.service.UnifyService;
import top.zuoyu.mybatis.ssist.StructureInit;

/**
 * EasyMybatis工具包 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 14:52
 */
public class Mappers {

    private static final Map<String, UnifyService> TABLE_NAME_UNIFY_SERVICE = Collections.synchronizedMap(new HashMap<>());

    /**
     * 根据表名获取相应的 {@link UnifyService}
     *
     * @param tableName - 表名
     * @return 相应的 {@link UnifyService}
     */
    public static UnifyService getUnifyService(String tableName) {
        if (TABLE_NAME_UNIFY_SERVICE.containsKey(tableName)) {
            return TABLE_NAME_UNIFY_SERVICE.get(tableName);
        }
        throw new EasyMybatisException("non-existent tableName: " + tableName);
    }

    // TODO  可以不使用CGLIB代理的方式进行转型

    /**
     * 初始化
     *
     * @param sqlSession - {@link SqlSession}
     */
    public static void init(SqlSession sqlSession) {
        Map<String, Class<?>> tableNameClass = StructureInit.getTableNameClass();
        if (CollectionUtils.isEmpty(tableNameClass)) {
            return;
        }
        Set<Map.Entry<String, Class<?>>> entries = tableNameClass.entrySet();
        for (Map.Entry<String, Class<?>> entry : entries) {
            String tableName = entry.getKey();
            Class<?> mapperInterfaceClass = entry.getValue();
            Object mapper = sqlSession.getMapper(mapperInterfaceClass);
            Class<?> mapperClass = mapper.getClass();
            CglibProxy cglibProxy = new CglibProxy();
            UnifyService unifyService = cglibProxy.getBean(mapperClass);
            TABLE_NAME_UNIFY_SERVICE.put(tableName, unifyService);
        }
    }

    /**
     * 清空服务存储（慎用）
     */
    public static void clearMappers() {
        TABLE_NAME_UNIFY_SERVICE.clear();
    }
}
