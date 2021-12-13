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
package top.zuoyu.mybatis.autoconfigure;

import java.lang.reflect.Field;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.annotation.Magic;
import top.zuoyu.mybatis.exception.EasyMybatisException;
import top.zuoyu.mybatis.proxy.dynamic.Mappers;
import top.zuoyu.mybatis.service.MapperRepository;
import top.zuoyu.mybatis.ssist.StructureInit;

/**
 * Mapper加载自动配置 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 15:32
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(EasyMybatisAutoConfiguration.class)
public class MapperInitAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public MapperInitAutoConfiguration(SqlSessionTemplate sqlSessionTemplate) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        Map<String, Class<?>> tableNameClass = StructureInit.getTableNameClass();
        for (Map.Entry<String, Class<?>> mapperClassEntry : tableNameClass.entrySet()) {
            BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(mapperClassEntry.getValue());
            BeanDefinition beanDefinition = definitionBuilder.getRawBeanDefinition();
            
        }
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
                    if (!field.getType().isAssignableFrom(MapperRepository.class)) {
                        throw new EasyMybatisException("Magic field must be declared as an interface:" + MapperRepository.class.getTypeName());
                    }
                    field.setAccessible(true);
                    String tableName = field.getAnnotation(Magic.class).value();
                    MapperRepository unifyService = Mappers.getUnifyService(tableName);
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
