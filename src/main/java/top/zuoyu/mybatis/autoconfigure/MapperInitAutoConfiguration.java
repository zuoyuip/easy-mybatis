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

import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.ssist.StructureInit;

/**
 * Mapper加载自动配置 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 15:32
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(EasyMybatisAutoConfiguration.class)
public class MapperInitAutoConfiguration implements ApplicationContextAware, BeanNameAware {

    private final SqlSessionTemplate sqlSessionTemplate;
    private final SqlSessionFactory sqlSessionFactory;
    private String beanName;
    private String basePackage;
    private String sqlSessionFactoryBeanName;
    private String sqlSessionTemplateBeanName;
    private String lazyInitialization;
    private String defaultScope;
    private boolean processPropertyPlaceHolders;

    public MapperInitAutoConfiguration(SqlSessionTemplate sqlSessionTemplate, SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        processPropertyPlaceHolders(applicationContext);
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        Map<String, Class<?>> tableNameClass = StructureInit.getTableNameClass();
        for (Map.Entry<String, Class<?>> mapperClassEntry : tableNameClass.entrySet()) {
            BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(mapperClassEntry.getValue());
            AbstractBeanDefinition abstractBeanDefinition = definition.getBeanDefinition();
            abstractBeanDefinition.setBeanClass(MapperFactoryBean.class);
            abstractBeanDefinition.getPropertyValues().add("addToConfig", true);
            abstractBeanDefinition.getPropertyValues().add("sqlSessionFactory", this.sqlSessionFactory);
            abstractBeanDefinition.getPropertyValues().add("sqlSessionTemplate", this.sqlSessionTemplate);
            abstractBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);
            abstractBeanDefinition.setLazyInit(Boolean.parseBoolean(this.lazyInitialization));
            abstractBeanDefinition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
            System.out.println("------------------------" + mapperClassEntry.getKey() + "-------------------------");
            beanFactory.registerBeanDefinition(mapperClassEntry.getKey(), abstractBeanDefinition);
        }
    }


    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    private void processPropertyPlaceHolders(@NonNull ApplicationContext applicationContext) {
        Map<String, PropertyResourceConfigurer> contextBeansOfType = applicationContext.getBeansOfType(PropertyResourceConfigurer.class,
                false, false);

        if (!contextBeansOfType.isEmpty() && applicationContext instanceof ConfigurableApplicationContext) {
            BeanDefinition mapperScannerBean = ((ConfigurableApplicationContext) applicationContext).getBeanFactory()
                    .getBeanDefinition(beanName);

            DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
            factory.registerBeanDefinition(beanName, mapperScannerBean);

            for (PropertyResourceConfigurer prc : contextBeansOfType.values()) {
                prc.postProcessBeanFactory(factory);
            }

            PropertyValues values = mapperScannerBean.getPropertyValues();

            this.basePackage = getPropertyValue("basePackage", values);
            this.sqlSessionFactoryBeanName = getPropertyValue("sqlSessionFactoryBeanName", values);
            this.sqlSessionTemplateBeanName = getPropertyValue("sqlSessionTemplateBeanName", values);
            this.lazyInitialization = getPropertyValue("lazyInitialization", values);
            this.defaultScope = getPropertyValue("defaultScope", values);
        }
        this.basePackage = Optional.ofNullable(this.basePackage).map(applicationContext.getEnvironment()::resolvePlaceholders).orElse(null);
        this.sqlSessionFactoryBeanName = Optional.ofNullable(this.sqlSessionFactoryBeanName)
                .map(applicationContext.getEnvironment()::resolvePlaceholders).orElse(null);
        this.sqlSessionTemplateBeanName = Optional.ofNullable(this.sqlSessionTemplateBeanName)
                .map(applicationContext.getEnvironment()::resolvePlaceholders).orElse(null);
        this.lazyInitialization = Optional.ofNullable(this.lazyInitialization).map(applicationContext.getEnvironment()::resolvePlaceholders)
                .orElse(null);
        this.defaultScope = Optional.ofNullable(this.defaultScope).map(applicationContext.getEnvironment()::resolvePlaceholders).orElse(null);
    }

    private String getPropertyValue(String propertyName, @NonNull PropertyValues values) {
        PropertyValue property = values.getPropertyValue(propertyName);

        if (property == null) {
            return null;
        }

        Object value = property.getValue();

        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return value.toString();
        } else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else {
            return null;
        }
    }
}
