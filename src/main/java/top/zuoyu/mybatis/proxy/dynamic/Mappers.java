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
package top.zuoyu.mybatis.proxy.dynamic;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.CollectionUtils;

import top.zuoyu.mybatis.service.MapperRepository;
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
     * @param sqlSessionTemplate - {@link SqlSessionTemplate}
     */
    @Override
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
            MapperRepository mapperRepository = new DynamicProxy(mapper).getUnifyService();
            TABLE_NAME_UNIFY_SERVICE.put(tableName, mapperRepository);
        }
    }

}
