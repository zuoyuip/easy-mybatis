package top.zuoyu.mybatis.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * Mybatis配置 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 12:25
 */
@ConfigurationProperties(prefix = EasyProperties.EASY_PREFIX)
public class EasyProperties {

    public static final String EASY_PREFIX = "easy";

    private Resource[] resources;

    public Resource[] getResources() {
        return resources;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }
}
