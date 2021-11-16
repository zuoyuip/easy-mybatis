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
package top.zuoyu.mybatis.autoconfigure;

import java.lang.reflect.Field;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.annotation.Magic;
import top.zuoyu.mybatis.exception.EasyMybatisException;
import top.zuoyu.mybatis.proxy.dynamic.Mappers;
import top.zuoyu.mybatis.service.UnifyService;

/**
 * Mapper加载自动配置 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 15:32
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(EasyMybatisAutoConfiguration.class)
public class MapperInitAutoConfiguration {

    public MapperInitAutoConfiguration(SqlSession sqlSession) {
        Mappers.getInstance().init(sqlSession);
    }

    /**
     * 依赖注入
     */
    @Configuration(proxyBeanMethods = false)
    class MapperInjectProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
            Class<?> beanClass = bean.getClass();
            Field[] declaredFields = beanClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Magic.class)) {
                    if (!field.getType().isAssignableFrom(UnifyService.class)) {
                        throw new EasyMybatisException("Magic field must be declared as an interface:" + UnifyService.class.getTypeName());
                    }
                    field.setAccessible(true);
                    String tableName = field.getAnnotation(Magic.class).value();
                    UnifyService unifyService = Mappers.getUnifyService(tableName);
                    try {
                        field.set(bean, unifyService);
                    } catch (IllegalAccessException e) {
                        throw new EasyMybatisException(e.getMessage());
                    }
                }
            }
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }


    }
}
