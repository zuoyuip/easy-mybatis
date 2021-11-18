package top.zuoyu.mybatis.json.convert;

/**
 * 转换器 .
 *
 * @author: zuoyu
 * @create: 2021-11-18 14:13
 */
public interface ConvertClass<T> {

    /**
     * 将给定的 {@code value} 对象转换为 {@code T}
     *
     * @param value - 给定对象
     * @return 目标转换类型的对象
     */
    T convert(Object value);

    /**
     * 将给定的 {@code value} 对象转换为 {@code T}， 否则返回 {@code defaultValue}
     *
     * @param value        - 给定对象
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    T convert(Object value, T defaultValue);
}
