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
package top.zuoyu.mybatis.autoconfigure;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import top.zuoyu.mybatis.json.JsonObject;
import top.zuoyu.mybatis.ssist.StructureInit;


/**
 * 自动装配 .
 *
 * @author: zuoyu
 * @create: 2021-10-29 16:55
 */
@Configuration(proxyBeanMethods = false)
@EnableAspectJAutoProxy(exposeProxy = true)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties({MybatisProperties.class, EasyProperties.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore(name = "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration")
public class EasyMybatisAutoConfiguration implements InitializingBean {

    private final MybatisProperties properties;

    private final EasyProperties easyProperties;

    private final Interceptor[] interceptors;

    private final ResourceLoader resourceLoader;

    private final DatabaseIdProvider databaseIdProvider;

    private final List<ConfigurationCustomizer> configurationCustomizers;


    public EasyMybatisAutoConfiguration(@NonNull MybatisProperties properties,
                                        @NonNull EasyProperties easyProperties, @NonNull ObjectProvider<Interceptor[]> interceptorsProvider,
                                        @NonNull ResourceLoader resourceLoader,
                                        @NonNull ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                                        @NonNull ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
        this.properties = properties;
        this.easyProperties = easyProperties;
        this.interceptors = interceptorsProvider.getIfAvailable();
        this.resourceLoader = resourceLoader;
        this.databaseIdProvider = databaseIdProvider.getIfAvailable();
        this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
    }

    @Override
    public void afterPropertiesSet() {
        // 添加别名包
        String typeAliasesPackage = this.properties.getTypeAliasesPackage();
        String modelPackageName = ClassUtils.getPackageName(JsonObject.class);
        if (StringUtils.hasLength(typeAliasesPackage)) {
            this.properties.setTypeAliasesPackage(typeAliasesPackage + "," + modelPackageName);
        } else {
            this.properties.setTypeAliasesPackage(modelPackageName);
        }
    }


    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        Resource[] resources = StructureInit.register(dataSource);
        this.easyProperties.setResources(resources);
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.properties.getConfigLocation())) {
            factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        }
        applyConfiguration(factory);
        if (this.properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(this.properties.getConfigurationProperties());
        }
        if (!ObjectUtils.isEmpty(this.interceptors)) {
            factory.setPlugins(this.interceptors);
        }
        if (this.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(this.databaseIdProvider);
        }
        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        }
        if (this.properties.getTypeAliasesSuperType() != null) {
            factory.setTypeAliasesSuperType(this.properties.getTypeAliasesSuperType());
        }
        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
            Resource[] resolveMapperLocations = this.properties.resolveMapperLocations();
            Resource[] all = (Resource[]) ArrayUtils.addAll(resolveMapperLocations, this.easyProperties.getResources());
            factory.setMapperLocations(all);
        } else {
            factory.setMapperLocations(this.easyProperties.getResources());
        }

        return factory.getObject();
    }

    private void applyConfiguration(SqlSessionFactoryBean factory) {
        org.apache.ibatis.session.Configuration configuration = this.properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
            configuration = new org.apache.ibatis.session.Configuration();
        }
        if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
            for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
                customizer.customize(configuration);
            }
        }
        factory.setConfiguration(configuration);
    }

    @Configuration(proxyBeanMethods = false)
    @Import(MybatisAutoConfiguration.AutoConfiguredMapperScannerRegistrar.class)
    @ConditionalOnMissingBean({MapperFactoryBean.class, MapperScannerConfigurer.class})
    public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {

        }
    }


    /**
     * Support Devtools Restart.
     */
    @Configuration
    @ConditionalOnProperty(prefix = "spring.devtools.restart", name = "enabled", matchIfMissing = true)
    static class RestartConfiguration {


    }


}
