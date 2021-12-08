/*
 * MIT License
 *
 * Copyright (c) 2021 zuoyuip@foxmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package top.zuoyu.mybatis.proxy.cglib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.CollectionUtils;

import top.zuoyu.mybatis.exception.EasyMybatisException;
import top.zuoyu.mybatis.service.MapperRepository;
import top.zuoyu.mybatis.ssist.StructureInit;

/**
 * EasyMybatis工具包 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 14:52
 */
public class Mappers {

    protected static final Map<String, MapperRepository> TABLE_NAME_UNIFY_SERVICE = Collections.synchronizedMap(new HashMap<>());
    private volatile static Mappers MAPPERS;

    /**
     * 根据表名获取相应的 {@link MapperRepository}
     *
     * @param tableName - 表名
     * @return 相应的 {@link MapperRepository}
     */
    public static MapperRepository getUnifyService(String tableName) {
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
     * @param sqlSessionTemplate - {@link SqlSessionTemplate}
     */
    public void init(SqlSessionTemplate sqlSessionTemplate) {
        Map<String, Class<?>> tableNameClass = StructureInit.getTableNameClass();
        if (CollectionUtils.isEmpty(tableNameClass)) {
            return;
        }
        Set<Map.Entry<String, Class<?>>> entries = tableNameClass.entrySet();
        for (Map.Entry<String, Class<?>> entry : entries) {
            String tableName = entry.getKey();
            Class<?> mapperInterfaceClass = entry.getValue();
            Object mapper = sqlSessionTemplate.getMapper(mapperInterfaceClass);
            Class<?> mapperClass = mapper.getClass();
            CglibProxy cglibProxy = new CglibProxy();
            MapperRepository unifyService = cglibProxy.getBean(mapperClass);
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
