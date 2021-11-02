package top.zuoyu.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对应的model .
 *
 * @author: zuoyu
 * @create: 2021-11-02 16:46
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Model {

    /**
     * 对应的model名称
     */
    String value() default "";
}
