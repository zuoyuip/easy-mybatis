package top.zuoyu.mybatis.utils;

import java.net.URL;

import org.apache.logging.log4j.util.Strings;

/**
 * 类工具包 .
 *
 * @author: zuoyu
 * @create: 2021-10-17 16:28
 */
public class ClassUtil {

    public static URL getBasePath() {
        return ClassUtil.class.getClassLoader().getResource(Strings.EMPTY);
    }
}
