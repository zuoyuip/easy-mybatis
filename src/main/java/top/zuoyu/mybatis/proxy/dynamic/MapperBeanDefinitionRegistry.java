package top.zuoyu.mybatis.proxy.dynamic;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import top.zuoyu.mybatis.autoconfigure.EasyMybatisAutoConfiguration;
import top.zuoyu.mybatis.ssist.StructureInit;

/**
 * Mapper注册 .
 *
 * @author: zuoyu
 * @create: 2021-12-12 15:04
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(EasyMybatisAutoConfiguration.class)
public class MapperBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, ApplicationContextAware {


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Map<String, Class<?>> tableNameClass = StructureInit.getTableNameClass();
        if (tableNameClass.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Class<?>> classEntry : tableNameClass.entrySet()) {
            System.out.println("---------------------------------------------");
            System.out.println(classEntry.getKey() + "---------------------------------------------" + classEntry.getValue());
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(classEntry.getValue());
            GenericBeanDefinition rawBeanDefinition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();
            rawBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(classEntry.getValue());
            rawBeanDefinition.setBeanClass(DynamicFactory.class);
            rawBeanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
            registry.registerBeanDefinition(classEntry.getKey(), rawBeanDefinition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {

    }
}
