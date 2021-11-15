package top.zuoyu.mybatis.aspectj.cglib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    protected static final Map<String, UnifyService> TABLE_NAME_UNIFY_SERVICE = Collections.synchronizedMap(new HashMap<>());
    private volatile static Mappers MAPPERS;

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

    protected Mappers() {
    }

    public static Mappers getInstance() {
        if (Objects.isNull(MAPPERS)) {
            synchronized (Mappers.class) {
                if (Objects.isNull(MAPPERS)) {
                    MAPPERS = new Mappers();
                }
            }
        }
        return MAPPERS;
    }


    /**
     * 初始化
     *
     * @param sqlSession - {@link SqlSession}
     */
    public void init(SqlSession sqlSession) {
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
    public void clearMappers() {
        TABLE_NAME_UNIFY_SERVICE.clear();
    }

}
