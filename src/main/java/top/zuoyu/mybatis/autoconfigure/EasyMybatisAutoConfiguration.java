package top.zuoyu.mybatis.autoconfigure;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import top.zuoyu.mybatis.data.DataInfoLoad;
import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.ssist.MapperStructure;
import top.zuoyu.mybatis.ssist.MapperXmlStructure;
import top.zuoyu.mybatis.ssist.ModelStructure;

/**
 * 自动装配 .
 *
 * @author: zuoyu
 * @create: 2021-10-29 16:55
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore(name = "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration")
public class EasyMybatisAutoConfiguration implements InitializingBean {

    private final MybatisProperties properties;

    private final Interceptor[] interceptors;

    private final ResourceLoader resourceLoader;

    private final DatabaseIdProvider databaseIdProvider;

    private final List<ConfigurationCustomizer> configurationCustomizers;


    public EasyMybatisAutoConfiguration(@NonNull MybatisProperties properties,
                                        @NonNull ObjectProvider<Interceptor[]> interceptorsProvider,
                                        @NonNull ResourceLoader resourceLoader,
                                        @NonNull ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                                        @NonNull ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) throws SQLException {
        this.properties = properties;
        this.interceptors = interceptorsProvider.getIfAvailable();
        this.resourceLoader = resourceLoader;
        this.databaseIdProvider = databaseIdProvider.getIfAvailable();
        this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
    }

    @Override
    public void afterPropertiesSet() throws Exception {


    }


    public void initModels(@NonNull DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        List<Table> tables = DataInfoLoad.getTables(connection);
        tables.forEach(ModelStructure::registerModel);
        tables.forEach(MapperXmlStructure::registerMapperXml);
        tables.forEach(MapperStructure::registerMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        initModels(dataSource);
        System.out.println("-----------------------------initModels---------------------------");
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
            factory.setMapperLocations(this.properties.resolveMapperLocations());
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

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        ExecutorType executorType = this.properties.getExecutorType();
        if (executorType != null) {
            return new SqlSessionTemplate(sqlSessionFactory, executorType);
        } else {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

    /**
     * This will just scan the same base package as Spring Boot does. If you want
     * more power, you can explicitly use
     * {@link org.mybatis.spring.annotation.MapperScan} but this will get typed
     * mappers working correctly, out-of-the-box, similar to using Spring Data JPA
     * repositories.
     */
    public static class AutoConfiguredMapperScannerRegistrar
            implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;

        private Environment environment;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {


            ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
            try {
                if (this.resourceLoader != null) {
                    scanner.setResourceLoader(this.resourceLoader);
                }
                List<String> packages = AutoConfigurationPackages.get(this.beanFactory);

                // TODO 添加包名
                packages.addAll(Arrays.asList());

                scanner.registerFilters();
                scanner.doScan(StringUtils.toStringArray(packages));
            } catch (IllegalStateException ex) {

            }
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }

    /**
     * {@link org.mybatis.spring.annotation.MapperScan} ultimately ends up
     * creating instances of {@link MapperFactoryBean}. If
     * {@link org.mybatis.spring.annotation.MapperScan} is used then this
     * auto-configuration is not needed. If it is _not_ used, however, then this
     * will bring in a bean registrar and automatically register components based
     * on the same component-scanning path as Spring Boot itself.
     */
    @org.springframework.context.annotation.Configuration
    @Import({ AutoConfiguredMapperScannerRegistrar.class })
    @ConditionalOnMissingBean(MapperFactoryBean.class)
    public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {

        }
    }


}
