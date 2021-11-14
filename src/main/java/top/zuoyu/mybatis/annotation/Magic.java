package top.zuoyu.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口实现注入 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 13:46
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Magic {

    /**
     * 对应的表名称
     */
    String value();
}
