/*
 * Copyright (c) 2021, zuoyu (zuoyuip@foxmil.com).
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
package top.zuoyu.mybatis.proxy.dynamic;

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
public class Mappers extends top.zuoyu.mybatis.proxy.cglib.Mappers {

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
