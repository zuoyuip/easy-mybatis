package top.zuoyu.mybatis.common;

import java.io.File;

import org.springframework.util.ClassUtils;

import top.zuoyu.mybatis.temp.mapper.BaseMapper;
import top.zuoyu.mybatis.temp.model.BaseModel;

/**
 * 常量 .
 *
 * @author: zuoyu
 * @create: 2021-11-02 11:02
 */
public interface Constant {

    /**
     * 包间隔符
     */
    CharSequence PACKAGE_SEPARATOR = ".";

    /**
     * 名称间隔符
     */
    CharSequence NAME_SEPARATOR = "_";

    /**
     * 通配符
     */
    CharSequence WILDCARD_SEPARATOR = "*";

    /**
     * Mapper.xml后缀
     */
    String MAPPER_XML_SUFFIX = "%sMapper.xml";

    /**
     * Mapper后缀
     */
    String MAPPER_SUFFIX = "%sMapper";

    String YES = "YES";
    String NO = "NO";
    String NULL = "null";

    /**
     * mybatis的xml包路径
     */
    String MAPPER_XML_DIR_NAME = "zuoyu" + File.separator + "mapper";

    /**
     * mybatis的接口包路径
     */
    String MAPPER_PACKAGE_NAME = ClassUtils.getPackageName(BaseMapper.class);

    /**
     * model的路径
     */
    String MODEL_PACKAGE_NAME = ClassUtils.getPackageName(BaseModel.class);
}
