package top.zuoyu.mybatis.utils;

import org.springframework.lang.NonNull;

/**
 * 字符串工具类 .
 *
 * @author: zuoyu
 * @create: 2021-10-17 17:11
 */
public class StrUtil {

    /**
     * 将字符串的首字母转大写
     *
     * @param str 需要转换的字符串
     * @return 转换的字符串
     */
    @NonNull
    public static String captureName(@NonNull String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = str.toCharArray();
        if (Character.isLowerCase(cs[0])) {
            cs[0] -= 32;
            return String.valueOf(cs);
        }
        return str;
    }
}
