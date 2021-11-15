package top.zuoyu.mybatis.aspectj.dynamic;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.springframework.util.CollectionUtils;

import top.zuoyu.mybatis.service.UnifyService;
import top.zuoyu.mybatis.ssist.StructureInit;

/**
 * EasyMybatis工具包 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 14:52
 */
public class Mappers extends top.zuoyu.mybatis.aspectj.cglib.Mappers {

    private volatile static Mappers MAPPERS;

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
    @Override
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
            UnifyService unifyService = new DynamicProxy(mapper).getUnifyService();
            TABLE_NAME_UNIFY_SERVICE.put(tableName, unifyService);
        }
    }

}
