/*
 * @Author: zuoyu
 * @Email: zuoyuip@foxmail.com
 * @Date: 2023-04-20 15:21:25
 * @LastEditTime: 2023-04-20 15:27:09
 * @Description: 配置赋值
 */
package top.zuoyu.mybatis.utils;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.properties.PropertyMapper;

public final class ConfigurationAppTo {

    private ConfigurationAppTo() {
    }

    /**
     * mybatis配置赋值
     * 
     * @param coreConfiguration springboot读取到的配置
     * @param configuration     mybatis原生配置
     */
    public static void mybatisConfiguration(MybatisProperties.CoreConfiguration coreConfiguration,
            Configuration configuration) {
        PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        mapper.from(coreConfiguration::getSafeRowBoundsEnabled).to(configuration::setSafeRowBoundsEnabled);
        mapper.from(coreConfiguration::getSafeResultHandlerEnabled).to(configuration::setSafeResultHandlerEnabled);
        mapper.from(coreConfiguration::getMapUnderscoreToCamelCase).to(configuration::setMapUnderscoreToCamelCase);
        mapper.from(coreConfiguration::getAggressiveLazyLoading).to(configuration::setAggressiveLazyLoading);
        mapper.from(coreConfiguration::getMultipleResultSetsEnabled).to(configuration::setMultipleResultSetsEnabled);
        mapper.from(coreConfiguration::getUseGeneratedKeys).to(configuration::setUseGeneratedKeys);
        mapper.from(coreConfiguration::getUseColumnLabel).to(configuration::setUseColumnLabel);
        mapper.from(coreConfiguration::getCacheEnabled).to(configuration::setCacheEnabled);
        mapper.from(coreConfiguration::getCallSettersOnNulls).to(configuration::setCallSettersOnNulls);
        mapper.from(coreConfiguration::getUseActualParamName).to(configuration::setUseActualParamName);
        mapper.from(coreConfiguration::getReturnInstanceForEmptyRow).to(configuration::setReturnInstanceForEmptyRow);
        mapper.from(coreConfiguration::getShrinkWhitespacesInSql).to(configuration::setShrinkWhitespacesInSql);
        mapper.from(coreConfiguration::getNullableOnForEach).to(configuration::setNullableOnForEach);
        mapper.from(coreConfiguration::getArgNameBasedConstructorAutoMapping)
                .to(configuration::setArgNameBasedConstructorAutoMapping);
        mapper.from(coreConfiguration::getLazyLoadingEnabled).to(configuration::setLazyLoadingEnabled);
        mapper.from(coreConfiguration::getLogPrefix).to(configuration::setLogPrefix);
        mapper.from(coreConfiguration::getLazyLoadTriggerMethods).to(configuration::setLazyLoadTriggerMethods);
        mapper.from(coreConfiguration::getDefaultStatementTimeout).to(configuration::setDefaultStatementTimeout);
        mapper.from(coreConfiguration::getDefaultFetchSize).to(configuration::setDefaultFetchSize);
        mapper.from(coreConfiguration::getLocalCacheScope).to(configuration::setLocalCacheScope);
        mapper.from(coreConfiguration::getJdbcTypeForNull).to(configuration::setJdbcTypeForNull);
        mapper.from(coreConfiguration::getDefaultResultSetType).to(configuration::setDefaultResultSetType);
        mapper.from(coreConfiguration::getDefaultExecutorType).to(configuration::setDefaultExecutorType);
        mapper.from(coreConfiguration::getAutoMappingBehavior).to(configuration::setAutoMappingBehavior);
        mapper.from(coreConfiguration::getAutoMappingUnknownColumnBehavior)
                .to(configuration::setAutoMappingUnknownColumnBehavior);
        mapper.from(coreConfiguration::getVariables).to(configuration::setVariables);
        mapper.from(coreConfiguration::getLogImpl).to(configuration::setLogImpl);
        mapper.from(coreConfiguration::getVfsImpl).to(configuration::setVfsImpl);
        mapper.from(coreConfiguration::getDefaultSqlProviderType).to(configuration::setDefaultSqlProviderType);
        mapper.from(coreConfiguration::getConfigurationFactory).to(configuration::setConfigurationFactory);
        mapper.from(coreConfiguration::getDefaultEnumTypeHandler).to(configuration::setDefaultEnumTypeHandler);
    }
}
