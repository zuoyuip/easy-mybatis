/*
 * Copyright (c) 2021, zuoyu (zuoyuip@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
